package xyz.mufanc.anpms.util

import android.util.Log
import org.joor.Reflect
import xyz.mufanc.anpms.ModuleMain.Companion.TAG

object MagicClassLoader : ClassLoader() {

    private lateinit var cl: ClassLoader

    fun init(cl: ClassLoader) {
        this.cl = cl

        val mine = javaClass.classLoader!!
        val parent: ClassLoader = Reflect.on(mine).get("parent")

        Reflect.on(mine).set("parent", this)
        Reflect.on(this).set("parent", parent)
    }

    override fun findClass(name: String?): Class<*> {
        Log.d(TAG, "loading class: $name")
        return cl.loadClass(name)
    }
}
