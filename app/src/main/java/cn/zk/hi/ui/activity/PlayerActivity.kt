package cn.zk.hi.ui.activity

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Handler
import android.os.Message
import android.widget.SeekBar
import cn.zk.hi.R
import cn.zk.hi.base.BaseActivity
import cn.zk.hi.model.FavoriteBean
import cn.zk.hi.widget.MTextureView
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.android.synthetic.main.player_contrl_view.*

class PlayerActivity :BaseActivity(), SeekBar.OnSeekBarChangeListener {

    private val MSG_PROGRESS = 0//更新进度的标识
    val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what){
                MSG_PROGRESS -> startUpdateProgress()
            }
        }
    }

    override fun getLayoutId(): Int  = R.layout.activity_player

    override fun initView() {
        //获取影片数据
        var movieBean = intent.getParcelableExtra<FavoriteBean>("FAVORITEBEAN")
        //初始化播放器播放
        initListener()
        player_controller.showPlaytitle(movieBean.title)
        textureView.setVideoPath(movieBean.url)
    }

    /**
     * 初始化播放器监听事件
     */
    private fun initListener() {
        textureView.apply {
            setMpreparedListener {
                //播放成功
                player_controller.initPrefared(textureView.getDuration())
                //开启进度条更新
                startUpdateProgress()
            }
            setMcompletionListener {
                //播放完成 ,循环播放
                textureView.rePlay()
                //重置控制栏
                player_controller.reset()
            }
            setMinfoListener {
                //播放状态
                when(it){
                    MediaPlayer.MEDIA_INFO_BUFFERING_START -> player_controller.showLoading()
                    MediaPlayer.MEDIA_INFO_BUFFERING_END -> player_controller.hideLoading()
                }
            }
            setMerrorListener {
                //播放失败
                stopUpdataProgress()
            }
        }
        player_controller.setPlayState {
            when(textureView.getPlayState()){
                MTextureView.STATE_PREPARED ,MTextureView.STATE_PLAYING -> pause()
                MTextureView.STATE_PAUSED -> start()
            }
        }
        progress_sk.setOnSeekBarChangeListener(this)
    }

    /**
     * 开启进度更新
     */
    private fun startUpdateProgress() {
        var progress = textureView.getCurrentPosition()
        player_controller.updateProgress(progress)
        handler.sendEmptyMessageDelayed(MSG_PROGRESS , 1000)
    }

    /**
     * 暂停进度更新
     */
    private fun stopUpdataProgress(){
        handler.removeCallbacksAndMessages(null)
    }

    /**
     * 播放
     */
    private fun start(){
        textureView.start()
        player_controller.updateSate(true)
        startUpdateProgress()
    }

    /**
     * 暂停
     */
    private fun pause(){
        textureView.pause()
        player_controller.updateSate(false)
        stopUpdataProgress()
    }

    /**
     * 进度条监听
     */
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        //判断是否是用户操作
        if(fromUser){
            player_controller.updateProgress(progress)
            textureView.seekTo(progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        stopUpdataProgress()
        textureView.stop()
    }
}