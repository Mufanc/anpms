package xyz.mufanc.anpms

import android.annotation.SuppressLint
import android.util.Log
import com.android.systemui.media.MediaData
import com.android.systemui.media.MediaDataManager
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedInterface.BeforeHookCallback
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface
import io.github.libxposed.api.annotations.BeforeInvocation
import io.github.libxposed.api.annotations.XposedHooker
import org.joor.Reflect
import rikka.hidden.compat.PackageManagerApis
import xyz.mufanc.anpms.hiddenapi.NotificationManagerApis
import xyz.mufanc.anpms.hiddenapi.TokenWrapper
import xyz.mufanc.anpms.util.MagicClassLoader
import xyz.mufanc.anpms.util.findMethod
import xyz.mufanc.autox.annotation.XposedEntry

@XposedEntry(["com.android.systemui"])
@Suppress("Unused")
class ModuleMain(
    private val ixp: XposedInterface,
    private val mlp: XposedModuleInterface.ModuleLoadedParam
) : XposedModule(ixp, mlp) {

    companion object {
        const val TAG = "anpms"
    }

    @SuppressLint("PrivateApi")
    override fun onPackageLoaded(param: XposedModuleInterface.PackageLoadedParam) {
        if (!param.isFirstPackage) return

        MagicClassLoader.init(param.classLoader)

        AddListenerHook.ixp = ixp
        hook(MediaDataManager::class.java.findMethod("addListener")!!, AddListenerHook::class.java)
    }

    @XposedHooker
    class AddListenerHook : XposedInterface.Hooker {
        companion object {

            lateinit var ixp: XposedInterface

            @BeforeInvocation
            @JvmStatic
            @Suppress("Unused")
            fun handle(callback: BeforeHookCallback): AddListenerHook? {
                val listener = callback.args[0]
                ixp.hook(listener.javaClass.findMethod("onMediaDataLoaded")!!, OnMediaDataLoadedHook::class.java)
                return null
            }
        }
    }

    @XposedHooker
    class OnMediaDataLoadedHook : XposedInterface.Hooker {
        companion object {
            @BeforeInvocation
            @JvmStatic
            @Suppress("Unused")
            fun handle(callback: BeforeHookCallback): OnMediaDataLoadedHook? {
                val data = callback.args.filterIsInstance(MediaData::class.java).first()

                val token = TokenWrapper(Reflect.on(data).get("token"))

                val uid = token.mUid
                val pkg = PackageManagerApis.getPackagesForUid(uid)?.firstOrNull()

                if (pkg != null && !NotificationManagerApis.areNotificationsEnabledForPackage(pkg, uid)) {
                    callback.returnAndSkip(null)
                    Log.i(TAG, "package `$pkg` has no permission to post notifications, skip.")
                }

                return null
            }

            private fun isNotificationAllowed(uid: Int): Boolean {
                val pkg = PackageManagerApis.getPackagesForUid(uid)?.firstOrNull() ?: return true
                return NotificationManagerApis.areNotificationsEnabledForPackage(pkg, uid)
            }
        }
    }
}
