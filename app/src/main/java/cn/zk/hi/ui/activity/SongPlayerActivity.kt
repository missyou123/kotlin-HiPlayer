package cn.zk.hi.ui.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.view.View
import android.widget.SeekBar
import cn.zk.hi.R
import cn.zk.hi.adapter.SongListPopAdapter
import cn.zk.hi.base.BaseActivity
import cn.zk.hi.model.SongBean
import cn.zk.hi.service.ISongBinder
import cn.zk.hi.service.SongService
import cn.zk.hi.utils.ProgressUtil
import cn.zk.hi.widget.SongListPopWindow
import kotlinx.android.synthetic.main.activity_song_player.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.imageResource

/**
 *播放页
 */
class SongPlayerActivity : BaseActivity(), View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private var songConnection = SongConnection()
    private val MSG_PROGRESS = 0//更新进度的标识
    private val MSG_CENTER_LINE = 1//更新居中选中线的标识
    private var center_line_progress = 0//当前居中线选中的progress
    //处理消息
    private val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                MSG_PROGRESS -> startUpdateProgress()
                MSG_CENTER_LINE -> {
                    center_line_controller.visibility = View.GONE
                    lyricView.changeUpdateSate(true)
                }
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_song_player

    override fun initView() {
        //初始化bar
        initListener()
    }

    /**
     * 初始化监听事件
     */
    private fun initListener() {
        play_state.setOnClickListener(this)
        play_rewind.setOnClickListener(this)
        play_forward.setOnClickListener(this)
        play_mode.setOnClickListener(this)
        play_list.setOnClickListener(this)
        progress_sk.setOnSeekBarChangeListener(this)
        left_img.setOnClickListener(this)
        center_line_play.setOnClickListener(this)
        lyricView.setProgressListener {
            if (it == -1) {
                //开启延时 关闭选中线
                handler.sendEmptyMessageDelayed(MSG_CENTER_LINE, 4000)
            } else if (it == -2) {
                //关闭居中线的定时任务
                handler.removeMessages(MSG_CENTER_LINE)
            } else {
                center_line_progress = it
                //更新居中线的ui
                selected_time.text = ProgressUtil.parseDuration(it)
                center_line_controller.visibility = View.VISIBLE
            }
        }

    }

    override fun initData() {
        //注册eventbus
        EventBus.getDefault().register(this)
        //先绑定songService ,然后开启
        var intent = intent.setClass(this, SongService::class.java)
        bindService(intent, songConnection, Context.BIND_AUTO_CREATE)
        startService(intent)
    }


    /**
     * eventbus  回调方法
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(songBean: SongBean) {
        //获取歌词
        lyricView.getLyrics()
        //播放成功,更新界面
        //更新歌名
        middle_text.text = songBean.display_name
        //1.更新播放状态
        changePlayState()
        //2.更新进度
        var durtion = iSongBinder?.getDurtion() ?: 0
        progress_sk.max = durtion
        durtion_tiem.text = ProgressUtil.parseDuration(durtion)
        lyricView.setDurtion(durtion)
        startUpdateProgress()
        //更新播放模式
        changePlayMode()
    }

    /**
     * 更新seek和currentTime
     */
    private fun startUpdateProgress() {
        var progress = iSongBinder?.getCurrentProgress() ?: 0
        updateProgress(progress)
//        handler.sendEmptyMessageDelayed(MSG_PROGRESS, 500)
        handler.sendEmptyMessage(MSG_PROGRESS)
    }

    /**
     * 更新进度
     */
    private fun updateProgress(progress: Int) {
        current_time.text = ProgressUtil.parseDuration(progress)
        progress_sk.progress = progress
        lyricView.updateLineView(progress)
    }


    /**
     * 播放控制栏点击事件
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.play_state -> {
                //通知播放器改变播放状态
                iSongBinder?.changePlayState()
                //更新uistate
                changePlayState()
            }
            R.id.play_rewind -> iSongBinder?.playRewind()
            R.id.play_forward -> iSongBinder?.playForward()
            R.id.play_mode -> {
                iSongBinder?.changePlayMode()
                changePlayMode()
            }
            R.id.play_list -> showPopList()
            R.id.left_img -> finish()
            R.id.center_line_play -> {
                //按居中线的progress更新进度
                iSongBinder?.seekTo(center_line_progress)
                updateProgress(center_line_progress)
                //隐藏居中线
                center_line_controller.visibility = View.GONE
            }
        }
    }

    /**
     * 弹出歌单
     */
    private fun showPopList() {
        var list = iSongBinder?.getSongList()
        list?.let {
            var adapter = SongListPopAdapter(it)
            adapter.setClickListener {
                iSongBinder?.playSong(it)
            }
            var songPop = SongListPopWindow(this, adapter)
            //获取player_controller的高度
            var h = player_controller.height
            songPop.showAsDropDown(player_controller, 0, -h)
        }
    }

    /**
     * 更新播放模式
     */
    private fun changePlayMode() {
        when (iSongBinder?.getPlayMode()) {
            SongService.MODE_ALL -> play_mode.imageResource = R.mipmap.icon_mode_1
            SongService.MODE_SINGLE -> play_mode.imageResource = R.mipmap.icon_mode_2
            SongService.MODE_RANDOM -> play_mode.imageResource = R.mipmap.icon_mode_3
        }
    }

    /**
     * 更新播放状态
     */
    private fun changePlayState() {
        var state: Boolean? = iSongBinder?.getPlayState()
        state?.let {
            //true为播放状态 ,false为暂停状态
            if (it) play_state.imageResource = R.mipmap.icon_pause
            else play_state.imageResource = R.mipmap.icon_play
        }
    }


    /**
     * seekbar滑动监听
     */
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        //判断是否是用户操作
        if (fromUser) {
            //更新ui ,通知播放器seek
            updateProgress(progress)
            iSongBinder?.seekTo(progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }


    /**
     * songService 绑定回调
     *
     */
    var iSongBinder: ISongBinder? = null

    inner class SongConnection : ServiceConnection {
        //断开
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        //成功绑定
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            service?.let {
                iSongBinder = it as ISongBinder
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        unbindService(songConnection)
        handler.removeCallbacksAndMessages(null)
        //注销evnetBus
        EventBus.getDefault().unregister(this)
    }

}