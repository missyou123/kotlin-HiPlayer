package cn.zk.hi.model

import android.database.Cursor
import android.os.Parcel
import android.os.Parcelable
import android.provider.MediaStore

/**
 * 歌 信息
 */
data class SongBean(var id: Int, var data: String, var size: Long,
                    var display_name: String, var artist: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(data)
        parcel.writeLong(size)
        parcel.writeString(display_name)
        parcel.writeString(artist)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SongBean> {
        override fun createFromParcel(parcel: Parcel): SongBean {
            return SongBean(parcel)
        }

        override fun newArray(size: Int): Array<SongBean?> {
            return arrayOfNulls(size)
        }


        /**
         * 将cursor转换成bean
         */
        fun getSongBean(cursor: Cursor?): SongBean {
            var songBean = SongBean(1, "", 0, "", "")
            cursor?.let {
                //解析cursor并且设置到bean对象中
                songBean.id = cursor.position + 1
                songBean.data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                songBean.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE))
                songBean.display_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                songBean.display_name = songBean.display_name.substring(0, songBean.display_name.lastIndexOf("."))
                songBean.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            }
            return songBean
        }

        /**
         * 通过cursor获取整个歌单
         */
        fun getSongs(cursor: Cursor): ArrayList<SongBean> {
            var list = ArrayList<SongBean>()
            cursor?.let {
                cursor.moveToPosition(-1)
                while (cursor.moveToNext()) {
                    list.add(getSongBean(cursor))
                }
            }
            return list
        }
    }
}