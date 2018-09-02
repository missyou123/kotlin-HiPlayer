package cn.zk.hi.model

import android.os.Parcel
import android.os.Parcelable

/**
 * 推荐位 bean
 */
data class FavoriteBean(var type: String, var id: Int,
                            var title: String, var description: String?,
                            var posterPic: String?,var thumbnailPic: String?,
                            var url: String,var hdUrl: String?,var videoSize: Int,
                            var hdVideoSize: Int,var uhdVideoSize: Int,var status: Int,
                            var traceUrl: String?,var clickUrl: String?,var uhdUrl: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(posterPic)
        parcel.writeString(thumbnailPic)
        parcel.writeString(url)
        parcel.writeString(hdUrl)
        parcel.writeInt(videoSize)
        parcel.writeInt(hdVideoSize)
        parcel.writeInt(uhdVideoSize)
        parcel.writeInt(status)
        parcel.writeString(traceUrl)
        parcel.writeString(clickUrl)
        parcel.writeString(uhdUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavoriteBean> {
        override fun createFromParcel(parcel: Parcel): FavoriteBean {
            return FavoriteBean(parcel)
        }

        override fun newArray(size: Int): Array<FavoriteBean?> {
            return arrayOfNulls(size)
        }
    }
}
