package xyz.mufanc.anpms.wrapper

import android.content.Context
import android.os.ServiceManager
import org.joor.Reflect

object NotificationManager {

    private val mService = ServiceManager.getService(Context.NOTIFICATION_SERVICE)

    fun areNotificationsEnabledForPackage(pkg: String, uid: Int): Boolean {
        return Reflect.on(mService).call("areNotificationsEnabledForPackage", pkg, uid).get()
    }
}
