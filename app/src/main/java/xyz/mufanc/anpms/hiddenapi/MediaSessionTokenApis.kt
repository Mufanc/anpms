package xyz.mufanc.anpms.hiddenapi

import android.media.session.ISessionController
import android.media.session.MediaSession
import android.media.session.MediaSessionTokenHidden

@Suppress("Cast_Never_Succeeds")
object MediaSessionTokenApis {
    fun getUid(token: MediaSession.Token): Int {
        return (token as MediaSessionTokenHidden).uid
    }

    fun getBinder(token: MediaSession.Token): ISessionController? {
        return (token as MediaSessionTokenHidden).binder
    }
}
