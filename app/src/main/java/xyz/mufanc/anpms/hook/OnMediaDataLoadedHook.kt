package xyz.mufanc.anpms.hook

import android.app.ActivityManagerHidden
import android.app.ActivityThread
import android.os.Process
import android.util.Log
import com.android.systemui.media.MediaData
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.annotations.BeforeInvocation
import io.github.libxposed.api.annotations.XposedHooker
import rikka.hidden.compat.ActivityManagerApis
import rikka.hidden.compat.PackageManagerApis
import rikka.hidden.compat.adapter.UidObserverAdapter
import xyz.mufanc.anpms.ModuleMain.Companion.TAG
import xyz.mufanc.anpms.hiddenapi.NotificationManagerApis
import xyz.mufanc.anpms.hiddenapi.TokenWrapper
import xyz.mufanc.anpms.util.ArtHelper

@XposedHooker
class OnMediaDataLoadedHook : XposedInterface.Hooker {
    companion object {

        private val sArtwork = MediaData::class.java.getDeclaredField("artwork")
            .apply { ArtHelper.markNonfinal(this) }

        private val mPermissionCache = HashMap<Int, Boolean>()
        private val mAppObserver = AppObserver(mPermissionCache)

        private val currentApplication by lazy {
            ActivityThread.currentActivityThread().application
        }

        init {
            ActivityManagerApis.registerUidObserver(
                mAppObserver,
                AppObserver.WHICH,
                ActivityManagerHidden.PROCESS_STATE_UNKNOWN,
                currentApplication.packageName
            )
        }

        @BeforeInvocation
        @JvmStatic
        @Suppress("Unused")
        fun handle(callback: XposedInterface.BeforeHookCallback): OnMediaDataLoadedHook? {
            val data = callback.args.filterIsInstance(MediaData::class.java).first()
            val token = TokenWrapper(data.token)

            if (!isNotificationAllowed(token.mUid)) {
//                    callback.returnAndSkip(null)
                sArtwork.set(data, null)

                Log.i(TAG, "uid `${token.mUid}` has no permission to post notifications, skip.")
            }

            return null
        }

        private fun isNotificationAllowed(uid: Int): Boolean = synchronized(mPermissionCache) {
            var cached = false
            val allowed = run {
                val cache = mPermissionCache[uid]

                if (cache != null) {
                    cached = true
                    return@run cache
                }

                // always allow system apps
                if (uid < Process.FIRST_APPLICATION_UID) return@run true

                val pkg = PackageManagerApis.getPackagesForUid(uid)?.firstOrNull() ?: return@run true

                // check permission
                return@run NotificationManagerApis.areNotificationsEnabledForPackage(pkg, uid)
            }

            Log.i(TAG, "check permission for uid $uid => $allowed (cached=$cached)")

            if (!cached) {
                mPermissionCache[uid] = allowed
            }

            allowed
        }
    }

    class AppObserver(
        private val cache: MutableMap<Int, Boolean>
    ) : UidObserverAdapter() {

        companion object {
            val WHICH = ActivityManagerHidden.UID_OBSERVER_GONE
        }

        override fun onUidGone(uid: Int, disabled: Boolean) {
            synchronized(cache) {
                if (cache.remove(uid) != null) {
                    Log.i(TAG, "uid $uid was gone, remove it from cache.")
                }
            }
        }
    }
}
