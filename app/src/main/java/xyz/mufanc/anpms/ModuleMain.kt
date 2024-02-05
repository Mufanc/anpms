package xyz.mufanc.anpms

import android.annotation.SuppressLint
import com.android.systemui.media.MediaDataManager
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface
import xyz.mufanc.anpms.hook.AddListenerHook
import xyz.mufanc.anpms.util.MagicClassLoader
import xyz.mufanc.anpms.util.findMethod
import xyz.mufanc.autox.annotation.XposedEntry

@XposedEntry(["com.android.systemui"])
@Suppress("Unused")
class ModuleMain(
    private val ixp: XposedInterface,
    private val mlp: XposedModuleInterface.ModuleLoadedParam
) : XposedModule(ixp, mlp) {

    companion object {
        const val TAG = "anpms"
    }

    @SuppressLint("PrivateApi")
    override fun onPackageLoaded(param: XposedModuleInterface.PackageLoadedParam) {
        if (!param.isFirstPackage) return

        MagicClassLoader.init(param.classLoader)

        AddListenerHook.ixp = ixp
        hook(MediaDataManager::class.java.findMethod("addListener")!!, AddListenerHook::class.java)
    }
}
