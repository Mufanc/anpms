package xyz.mufanc.anpms.wrapper

import android.os.Parcel
import android.os.Parcelable

class MediaSessionToken(private val token: Parcelable) {
    val uid: Int
        get() = run {
            val parcel = Parcel.obtain()
            try {
                token.writeToParcel(parcel, 0)
                parcel.setDataPosition(0)
                parcel.readInt()
            } finally {
                parcel.recycle()
            }
        }
}
