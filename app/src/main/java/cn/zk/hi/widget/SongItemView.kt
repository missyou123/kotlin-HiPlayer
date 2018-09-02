package cn.zk.hi.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import cn.zk.hi.R
import cn.zk.hi.model.SongBean
import kotlinx.android.synthetic.main.song_item_view.view.*

/**
 * 歌单的itemView
 */
class SongItemView :RelativeLayout{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context , R.layout.song_item_view , this)
    }

    /**
     * 填充数据
     */
    fun refreshView(songBean: SongBean) {
        song_index.text = songBean.id.toString()
        song_name.text = songBean.display_name
        song_artist.text = songBean.artist
    }

}