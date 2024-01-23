package xyz.mufanc.anpms.hook

import android.media.session.MediaSession
import android.os.Binder
import android.util.Log
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedInterface.AfterHookCallback
import io.github.libxposed.api.annotations.AfterInvocation
import io.github.libxposed.api.annotations.XposedHooker
import xyz.mufanc.anpms.HookConfigs.TAG
import xyz.mufanc.anpms.util.Utils
import java.lang.reflect.Method

@XposedHooker
class ReturnValueSanitizer : XposedInterface.Hooker {
    companion object {
        @AfterInvocation
        @JvmStatic
        @Suppress("Unused")
        fun handle(callback: AfterHookCallback, ctx: ReturnValueSanitizer?) {
            val pid = Binder.getCallingPid()
            if (!Utils.isSystemUi(pid)) return

            val result = callback.result
            val (newResult, modified) = if (List::class.java.isInstance(result)) {
                Utils.sanitizeTokens(result as List<*>)
            } else {
                Utils.sanitizeToken(result as MediaSession.Token)
            }

            callback.result = newResult

            if (modified) {
                Log.i(TAG, "[ReturnValueSanitizer] sanitized return value for: ${callback.member.name}")
            }
        }

        fun hook(ixp: XposedInterface, target: Method) {
            ixp.hook(target, ReturnValueSanitizer::class.java)
        }
    }
}
