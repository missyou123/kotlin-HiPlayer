package cn.zk.hi.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import cn.zk.hi.R
import cn.zk.hi.model.MusicEVentBean
import kotlinx.android.synthetic.main.toolbar.view.*
import org.jetbrains.anko.imageResource
import org.greenrobot.eventbus.EventBus


class ToolBarView : RelativeLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.toolbar, this)
    }

    /**
     * 初始化favorites页面的bar
     */
    fun initFavoritesBar(){
        initBar(R.mipmap.icon_bar_3, "个性推荐", R.mipmap.icon_bar_2)
    }

    /**
     * 初始化视频页面的bar
     */
    fun initMovieBar() {
        initBar(R.mipmap.icon_bar_4, "我的视频", R.mipmap.icon_bar_2)
    }

    /**
     *   初始化music 中 模块栏的 bar
     */
    fun initMusicModeBar() {
        initBar(R.mipmap.icon_bar_1, "我的音乐", R.mipmap.icon_bar_2)
    }

    /**
     * 初始化歌单页面 的 bar
     */
    fun initMusicSongsBar() {
        initBar(R.mipmap.icon_back, "歌单", R.mipmap.icon_bar_2)
        left_img.setOnClickListener {
            //退出回调通知
            EventBus.getDefault().post(MusicEVentBean(1))
        }
    }

    /**
     * 初始化歌播放器页面的 bar
     */
    fun initSongPlayerBar() {
        initBar(R.mipmap.left_arrow, "最美的太阳", R.mipmap.icon_bar_2)
        left_img.setOnClickListener {
            listener?.invoke()
        }
    }

    fun changeTitle(title :String){
        middle_text.text = title
    }

    //初始化bar数据
    private fun initBar(res1: Int, text: String, res2: Int) {
        left_img.imageResource = res1
        middle_text.text = text
        right_img.imageResource = res2
    }

    var listener: (() -> Unit)? = null

    fun setCallBack(listener: () -> Unit) {
        this.listener = listener
    }


}