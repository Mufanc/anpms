package xyz.mufanc.anpms.hook

import android.media.session.MediaSession
import android.os.IInterface
import android.util.ArrayMap
import android.util.Log
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.annotations.BeforeInvocation
import io.github.libxposed.api.annotations.XposedHooker
import xyz.mufanc.anpms.HookConfigs
import xyz.mufanc.anpms.util.Utils
import java.lang.reflect.Member
import java.lang.reflect.Method

@XposedHooker
class CallbackArgumentSanitizer : XposedInterface.Hooker {
    companion object {

        private val mArgsIndexes = ArrayMap<Member, Int>()

        @BeforeInvocation
        @JvmStatic
        @Suppress("Unused")
        fun handle(callback: XposedInterface.BeforeHookCallback): CallbackArgumentSanitizer? {
            run {
                val pid = Utils.getRemotePid((callback.thisObject as IInterface).asBinder())
                if (!Utils.isSystemUi(pid)) return@run

                val index = mArgsIndexes[callback.member]!!
                val arg = callback.args[index]
                var modified = false

                if (List::class.java.isInstance(arg)) {
                    val (newArgs, m) = Utils.sanitizeTokens(arg as List<*>)
                    callback.args[index] = newArgs
                    if (m) {
                        modified = true
                    }
                } else {
                    val m = Utils.sanitizeToken(callback.args[index] as MediaSession.Token).second
                    if (m) {
                        modified = true
                        callback.returnAndSkip(null)
                    }
                }

                if (modified) {
                    Log.i(HookConfigs.TAG, "[CallbackArgumentSanitizer] sanitized argument $index for: ${callback.member.name}")
                }
            }

            return null
        }

        fun hook(ixp: XposedInterface, target: Method, index: Int) {
            mArgsIndexes[target] = index
            ixp.hook(target, CallbackArgumentSanitizer::class.java)
        }
    }
}
