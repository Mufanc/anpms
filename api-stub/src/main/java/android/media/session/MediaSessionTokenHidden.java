package android.media.session;

import dev.rikka.tools.refine.RefineAs;

@RefineAs(MediaSession.Token.class)
public class MediaSessionTokenHidden {
    public int getUid() {
        throw new RuntimeException("stub");
    }

    public ISessionController getBinder() {
        throw new RuntimeException("stub");
    }
}
