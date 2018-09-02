package cn.zk.hi.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import cn.zk.hi.R
import cn.zk.hi.model.MusicModeBean
import kotlinx.android.synthetic.main.music_mode_item_view.view.*
import org.jetbrains.anko.imageResource
import java.util.*

/**
 * 作者: zhukun on $(DATE)$(TIME).
 * 音乐 界面  itemView 类型 1
 */
class MusicModeItemView : RelativeLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.music_mode_item_view, this)
    }

    fun refreshView(musicModeBean: MusicModeBean) {
        title.text = musicModeBean.title
        title_icon.imageResource = musicModeBean.titleIcon
        right_text.text = "${Random().nextInt(100)}"
        right_icon.imageResource = musicModeBean.rightIcon
    }
}