package xyz.mufanc.anpms.hiddenapi

import android.media.session.MediaSession

class TokenWrapper(private val token: MediaSession.Token) {

    val mUid get() = MediaSessionTokenApis.getUid(token)

    val mBinder get() = MediaSessionTokenApis.getBinder(token)

    override fun toString(): String {
        return "MediaSession.Token { mUid = $mUid, mBinder = $mBinder }"
    }
}
