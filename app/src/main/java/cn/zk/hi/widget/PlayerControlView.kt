package cn.zk.hi.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import cn.zk.hi.R
import cn.zk.hi.utils.ProgressUtil
import kotlinx.android.synthetic.main.player_contrl_view.view.*

/**
 * 播放控制栏
 */
class PlayerControlView : RelativeLayout {
    //loading的旋转动画
    val rotate by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_anim) }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.player_contrl_view, this)
    }

    /**
     * 播放成功,初始化界面
     */
    fun initPrefared(duration: Int) {
        hidePlaytitle()
        play_state.background = resources.getDrawable(R.mipmap.icon_movie_pause)
        durtion_tiem.text = ProgressUtil.parseDuration(duration)
        progress_sk.max = duration
        play_state.setOnClickListener {
            listener?.invoke()
        }
    }

    /**
     * 更新当前进度
     */
    fun updateProgress(progress: Int) {
        current_tiem.text = ProgressUtil.parseDuration(progress)
        progress_sk.progress = progress
    }

    /**
     * 播放按钮的点击事件
     */
    var listener: (() -> Unit)? = null

    fun setPlayState(listener: () -> Unit) {
        this.listener = listener
    }

    /**
     * 修改播放state的ui
     * true代表播放 , false 代表暂停
     */
    fun updateSate(b: Boolean) {
        if (b) play_state.background = resources.getDrawable(R.mipmap.icon_movie_pause)
        else play_state.background = resources.getDrawable(R.mipmap.icon_movie_play)
    }

    /**
     * 重置player_control
     */

    fun reset() {
        title_tv.visibility = View.VISIBLE
        durtion_tiem.text = "00:00"
        current_tiem.text = "00:00"
        progress_sk.progress = 0
        play_state.background = resources.getDrawable(R.mipmap.icon_movie_play)
    }

    /**
     * 显示缓冲
     */
    fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    fun hideLoading(){
        loading.visibility = View.GONE
    }

    /**
     * 显示即将播放提示页
     */
    fun showPlaytitle(title : String){
        title_tv.text = "即将播放 : $title"
        title_tv.visibility = View.VISIBLE
    }
    /**
     * 隐藏即将播放提升页
     */
    fun hidePlaytitle(){
        title_tv.visibility = View.GONE
    }

    fun setError(){
        title_tv.text = "播放失败,请重试..."
        title_tv.visibility = View.VISIBLE
    }
}