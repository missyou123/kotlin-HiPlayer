package cn.zk.hi.widget

import android.content.Context
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.Log
import android.view.Surface
import android.view.TextureView

class MTextureView : TextureView, TextureView.SurfaceTextureListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener {

    private var TAG = "MTextureView"
    //当前的播放状态
    private var mCurrentState = STATE_IDLE
    //播放器
    private var mediaPlayer: MediaPlayer? = null
    private var mSurface: Surface? = null
    //播放地址
    private var url: String? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        //播放状态
        val STATE_ERROR = -1
        val STATE_IDLE = 0
        val STATE_PREPARING = 1
        val STATE_PREPARED = 2
        val STATE_PLAYING = 3
        val STATE_PAUSED = 4
        val STATE_PLAYBACK_COMPLETED = 5
    }


    init {
        mCurrentState = STATE_IDLE
        surfaceTextureListener = this
    }

    /**
     * 尺寸改变
     */
    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
    }

    /**
     * 更新
     */
    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    /**
     * 销毁
     */
    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        Log.e(TAG, "onSurfaceTextureDestroyed")
        mSurface = null
        release()
        return true
    }


    /**
     * 创建
     */
    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        Log.e(TAG, "onSurfaceTextureAvailable")
        //初始化
        mSurface = Surface(surface)
        openVideo()
    }

    /**
     * 影片加载成功
     */
    override fun onPrepared(mp: MediaPlayer?) {
        Log.e(TAG, "onPrepared")
        mCurrentState = STATE_PREPARED
        mediaPlayer?.start()
        this.preparedListener?.invoke()
    }

    /**
     * 播放状态回调
     */
    override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        this.infoListener?.invoke(what)
        return true
    }

    /**
     * 播放完成回调
     */
    override fun onCompletion(mp: MediaPlayer?) {
        mCurrentState = STATE_PLAYBACK_COMPLETED
        this.completionListener?.invoke()
    }

    /**
     * 播放出错回调
     */
    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        mCurrentState = STATE_ERROR
        this.errorListener?.invoke(what)
        return true
    }


    /**
     *设置播放地址
     */
    fun setVideoPath(path: String) {
        this.url = path
        openVideo()
    }

    /**
     * 播放影片
     */
    private fun openVideo() {
        Log.e(TAG, "URL : $url")
        if (url == null || mSurface == null) return
        mCurrentState = STATE_PREPARING
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
        mediaPlayer = MediaPlayer()
        mediaPlayer?.let {
            it.setOnPreparedListener(this)
            it.setOnErrorListener(this)
            it.setOnCompletionListener(this)
            it.setOnInfoListener(this)
            it.setScreenOnWhilePlaying(true)
            it.setDataSource(url)
            it.setSurface(mSurface)
            it.prepareAsync()
        }

    }

    /**
     * 播放
     */
    fun start() {
        if (isInPlayState()) {
            mediaPlayer?.start()
            mCurrentState = STATE_PLAYING
        }
    }

    /**
     * 暂停
     */
    fun pause() {
        if (isInPlayState()) {
            mediaPlayer?.pause()
            mCurrentState = STATE_PAUSED
        }
    }

    /**
     * 停止播放
     */
    fun stop() {
        release()
    }

    /**
     * 重新播放
     */
    fun rePlay() {
        mediaPlayer?.reset()
        openVideo()
    }

    /**
     * 获取影片总时长
     */
    fun getDuration(): Int {
        return if (isInPlayState()) mediaPlayer?.duration ?: 0 else 0
    }

    /**
     * 获取影片当前时长
     */
    fun getCurrentPosition(): Int {
        return if (isInPlayState()) mediaPlayer?.currentPosition ?: 0 else 0
    }

    /**
     * 快进,快退
     */
    fun seekTo(position: Int) {
        if (isInPlayState()) mediaPlayer?.seekTo(position)

    }

    /**
     * 判断是否是播放状态
     */

    fun getPlayState(): Int = mCurrentState

    /**
     * 根据播放状态判断此时是否可以播放和暂停
     */
    private fun isInPlayState(): Boolean {
        return (this.mediaPlayer != null && mCurrentState !== STATE_ERROR
                && mCurrentState !== STATE_IDLE && mCurrentState !== STATE_PREPARING)
    }

    /**
     * 释放播放器资源
     */

    private fun release() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    /**
     * 监听事件回调
     */
    //播放开始
    var preparedListener: (() -> Unit)? = null

    fun setMpreparedListener(listener: () -> Unit) {
        this.preparedListener = listener
    }

    //播放完成
    var completionListener: (() -> Unit)? = null

    fun setMcompletionListener(listener: () -> Unit) {
        this.completionListener = listener
    }

    //播放状态
    var infoListener: ((what: Int) -> Unit)? = null

    fun setMinfoListener(listener: (what: Int) -> Unit) {
        this.infoListener = listener
    }

    //播放失败
    var errorListener: ((what: Int) -> Unit)? = null

    fun setMerrorListener(listener: (what: Int) -> Unit) {
        this.errorListener = listener
    }

}

