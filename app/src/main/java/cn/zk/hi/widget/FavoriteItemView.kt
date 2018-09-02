package cn.zk.hi.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import cn.zk.hi.R
import cn.zk.hi.model.FavoriteBean
import kotlinx.android.synthetic.main.favorite_item_view.view.*
import cn.zk.hi.utils.CropSquareTransformation
import com.squareup.picasso.Picasso
import java.util.*


/**
 * 推荐的itemview
 */
class FavoriteItemView : RelativeLayout {


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.favorite_item_view, this)
    }

    //刷新view
    fun refreshView(favoriteBean: FavoriteBean) {
        des.text = favoriteBean.title
        name.text = favoriteBean.description
        bang_num.text = Random().nextInt(2000).toString()
        message_num.text = Random().nextInt(2000).toString()
        play_num.text = "${Random().nextInt(100) / 10f}万"
        Picasso.with(context)
                .load(favoriteBean.posterPic)
                .transform(CropSquareTransformation())
                .placeholder(R.mipmap.default_poster_740x417)
                .into(movie_img)
    }

}