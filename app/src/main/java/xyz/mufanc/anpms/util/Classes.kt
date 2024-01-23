package xyz.mufanc.anpms.util

import java.lang.reflect.Method

fun Class<*>.findMethod(name: String): Method? {
    return declaredMethods.find { it.name == name }
}
