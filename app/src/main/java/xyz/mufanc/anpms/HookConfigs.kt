package xyz.mufanc.anpms

object HookConfigs {
    const val TAG = "anpms"

    val GET_SESSIONS = Pair("com.android.server.media.MediaSessionService\$SessionManagerImpl", "getSessions")
    val GET_MEDIA_KEY_EVENT_SESSION = Pair("com.android.server.media.MediaSessionService\$SessionManagerImpl", "getMediaKeyEventSession")
    val ON_ACTIVE_SESSIONS_CHANGED = Pair("android.media.session.IActiveSessionsListener\$Stub\$Proxy", "onActiveSessionsChanged")
    val ON_MEDIA_KEY_EVENT_DISPATCHED = Pair("android.media.session.IOnMediaKeyEventDispatchedListener\$Stub\$Proxy", "onMediaKeyEventDispatched")
    val ON_MEDIA_KEY_EVENT_SESSION_CHANGED = Pair("android.media.session.IOnMediaKeyEventSessionChangedListener\$Stub\$Proxy", "onMediaKeyEventSessionChanged")
    val ON_CONNECT = Pair("android.service.media.IMediaBrowserServiceCallbacks\$Stub\$Proxy", "onConnect")
    val ON_VOLUME_CHANGED = Pair("android.media.IRemoteSessionCallback\$Stub\$Proxy", "onVolumeChanged")
    val ON_SESSION_CHANGED = Pair("android.media.IRemoteSessionCallback\$Stub\$Proxy", "onSessionChanged")
}
