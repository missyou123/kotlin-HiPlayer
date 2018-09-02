package cn.zk.hi.utils

import android.graphics.Bitmap
import com.squareup.picasso.Transformation


class CropSquareTransformation : Transformation {
    override fun key(): String {
        return "square()"
    }

    override fun transform(source: Bitmap?): Bitmap? {
        var result: Bitmap? = null
        source?.let {
            val size = Math.min(source.width, source.height)
            val x = (source.width - size) / 2
            val y = (source.height - size) / 2
            result = Bitmap.createBitmap(source, x, y, size, size)
            if (result != source) {
                source.recycle()
            }
        }

        return result
    }

}