package xyz.mufanc.anpms

import android.annotation.SuppressLint
import android.util.Log
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface
import xyz.mufanc.anpms.HookConfigs.TAG
import xyz.mufanc.anpms.hook.CallbackArgumentSanitizer
import xyz.mufanc.anpms.hook.ReturnValueSanitizer
import xyz.mufanc.anpms.util.ClassHelper
import xyz.mufanc.autox.annotation.XposedEntry

@XposedEntry(["system"])
@Suppress("Unused")
class ModuleMain(
    private val ixp: XposedInterface,
    private val mlp: XposedModuleInterface.ModuleLoadedParam
) : XposedModule(ixp, mlp) {
    private lateinit var mClassHelper: ClassHelper

    @SuppressLint("PrivateApi")
    override fun onSystemServerLoaded(param: XposedModuleInterface.SystemServerLoadedParam) {
        Log.i(TAG, "module loaded in ${mlp.processName}")

        mClassHelper = ClassHelper(param.classLoader)
        
        ReturnValueSanitizer.hook(ixp, mClassHelper.findMethod(HookConfigs.GET_SESSIONS))
        ReturnValueSanitizer.hook(ixp, mClassHelper.findMethod(HookConfigs.GET_MEDIA_KEY_EVENT_SESSION))

        CallbackArgumentSanitizer.hook(ixp, mClassHelper.findMethod(HookConfigs.ON_ACTIVE_SESSIONS_CHANGED), 0)
        CallbackArgumentSanitizer.hook(ixp, mClassHelper.findMethod(HookConfigs.ON_MEDIA_KEY_EVENT_DISPATCHED), 2)
        CallbackArgumentSanitizer.hook(ixp, mClassHelper.findMethod(HookConfigs.ON_MEDIA_KEY_EVENT_SESSION_CHANGED), 1)

        CallbackArgumentSanitizer.hook(ixp, mClassHelper.findMethod(HookConfigs.ON_CONNECT), 1)

        CallbackArgumentSanitizer.hook(ixp, mClassHelper.findMethod(HookConfigs.ON_VOLUME_CHANGED), 0)
        CallbackArgumentSanitizer.hook(ixp, mClassHelper.findMethod(HookConfigs.ON_SESSION_CHANGED), 0)
    }
}
