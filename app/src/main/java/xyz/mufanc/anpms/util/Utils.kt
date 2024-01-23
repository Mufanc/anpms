package xyz.mufanc.anpms.util

import android.media.session.MediaSession
import android.os.IBinder
import android.os.Parcel
import android.os.Process
import android.util.Log
import rikka.hidden.compat.PackageManagerApis
import xyz.mufanc.anpms.HookConfigs.TAG
import xyz.mufanc.anpms.wrapper.MediaSessionToken
import xyz.mufanc.anpms.wrapper.NotificationManager
import java.io.File

object Utils {

    fun isSystemUi(pid: Int): Boolean {
        return true
        val cmdline = File("/proc/$pid/cmdline").readText().trim('\u0000')
        Log.v(TAG, "[Utils] isSystemUi($pid) => $cmdline")
        return cmdline == "com.android.systemui"
    }

    @Suppress("Name_Shadowing")
    fun isNotificationAllowed(uid: Int, pkg: String? = null): Boolean {
        return false
        if (uid < Process.FIRST_APPLICATION_UID) return true
        val pkg = pkg ?: PackageManagerApis.getPackagesForUid(uid)?.firstOrNull() ?: return true
        return NotificationManager.areNotificationsEnabledForPackage(pkg, uid)
    }

    fun sanitizeToken(token: MediaSession.Token): Pair<MediaSession.Token?, Boolean> {
        if (isNotificationAllowed(MediaSessionToken(token).uid)) {
            return Pair(token, false)
        }

        return Pair(null, true)
    }

    fun sanitizeTokens(tokens: List<*>): Pair<List<*>, Boolean> {
        var modified = false
        val newTokens = tokens.filter {
            val (token, m) = sanitizeToken(it as MediaSession.Token)

            if (m) modified = true

            token != null
        }

        return Pair(newTokens, modified)
    }

    fun getRemotePid(binder: IBinder): Int {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()

        val remotePid = try {
            binder.transact(1599097156 /* _PID */, data, reply, 0)
            reply.readInt()
        } finally {
            data.recycle()
            reply.recycle()
        }

        Log.v(TAG, "[Utils] getRemotePid($binder) = $remotePid")

        return remotePid
    }
}
