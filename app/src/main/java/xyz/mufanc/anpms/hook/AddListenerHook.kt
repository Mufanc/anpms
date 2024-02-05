package xyz.mufanc.anpms.hook

import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.annotations.BeforeInvocation
import io.github.libxposed.api.annotations.XposedHooker
import xyz.mufanc.anpms.util.findMethod

@XposedHooker
class AddListenerHook : XposedInterface.Hooker {
    companion object {

        lateinit var ixp: XposedInterface

        @BeforeInvocation
        @JvmStatic
        @Suppress("Unused")
        fun handle(callback: XposedInterface.BeforeHookCallback): AddListenerHook? {
            val listener = callback.args[0]
            ixp.hook(listener.javaClass.findMethod("onMediaDataLoaded")!!, OnMediaDataLoadedHook::class.java)
            return null
        }
    }
}
