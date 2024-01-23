package xyz.mufanc.anpms.hiddenapi

import android.app.INotificationManager
import android.content.Context
import android.os.ServiceManager

object NotificationManagerApis {

    private val mBinder = ServiceManager.getService(Context.NOTIFICATION_SERVICE)

    private val mService = INotificationManager.Stub.asInterface(mBinder)

    fun areNotificationsEnabledForPackage(pkg: String, uid: Int): Boolean {
        return mService.areNotificationsEnabledForPackage(pkg, uid)
    }
}
