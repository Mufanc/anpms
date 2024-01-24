package xyz.mufanc.anpms.util

import android.annotation.SuppressLint
import java.lang.reflect.Field
import java.lang.reflect.Modifier

object ArtHelper {

    @SuppressLint("DiscouragedPrivateApi")
    private val sFieldAccessFlags = Field::class.java.getDeclaredField("accessFlags")
        .apply { isAccessible = true }

    fun markNonfinal(field: Field) {
        sFieldAccessFlags.set(field, field.modifiers and (Modifier.FINAL xor -1))
    }
}
