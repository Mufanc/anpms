package xyz.mufanc.anpms.util

import java.lang.reflect.Method

class ClassHelper(
    private val mClassLoader: ClassLoader
) {
    fun findMethod(info: Pair<String, String>, isStubProxy: Boolean = false): Method {
        val className = info.first + if (isStubProxy) "\$Stub\$Proxy" else ""
        return mClassLoader.loadClass(className)
            .declaredMethods
            .find { it.name == info.second }!!
    }
}
