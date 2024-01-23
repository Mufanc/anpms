package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

public interface INotificationManager extends IInterface  {
    boolean areNotificationsEnabledForPackage(String pkg, int uid);

    abstract class Stub extends Binder implements INotificationManager {
        public static INotificationManager asInterface(IBinder binder) {
            throw new RuntimeException("stub");
        }
    }
}
