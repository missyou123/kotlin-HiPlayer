package cn.zk.hi.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.RemoteViews
import cn.zk.hi.R
import cn.zk.hi.model.MusicEVentBean
import cn.zk.hi.model.SongBean
import cn.zk.hi.service.SongService.Companion.FROM_MUSIC_CONTENT
import cn.zk.hi.service.SongService.Companion.MODE_ALL
import cn.zk.hi.service.SongService.Companion.MODE_RANDOM
import cn.zk.hi.service.SongService.Companion.MODE_SINGLE
import cn.zk.hi.ui.activity.MainActivity
import cn.zk.hi.ui.activity.SongPlayerActivity
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.collections.ArrayList

/**
 * 作者: zhukun on $(DATE)$(TIME).
 * song播放器 service
 */
class SongService : Service() {
    //歌单列表
    var list: ArrayList<SongBean>? = null
    //当前播放position
    var currentPosition = -1
    //内部binder
    val songBinder by lazy { SongBinder() }
    // 播放器
    var mediaPlayer: MediaPlayer? = null
    var mode = MODE_ALL  //播放模式
    val sp by lazy { getSharedPreferences("config", Context.MODE_PRIVATE) }
    //通知的manager
    var notificationManager: NotificationManager? = null
    var notification: Notification? = null

    companion object {
        //播放模式
        val MODE_ALL = 1
        val MODE_SINGLE = 2
        val MODE_RANDOM = 3

        //从哪里进入的标识
        val FROM_MUSIC_FRAGMENT = 1
        val FROM_MUSIC_CONTENT = 2
        val FROM_MUSIC_NEXT = 3
        val FROM_MUSIC_STATE = 4
        val FROM_MUSIC_PRE = 5
        //标示现在是否播放
        var isPlayMusic = false
        var isliving = false
    }


    override fun onCreate() {
        super.onCreate()
        //更新mode
        mode = sp.getInt("MODE", MODE_ALL)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            //判断从哪里开启
            var from = intent.getIntExtra("from", -1)
            when (from) {
                FROM_MUSIC_FRAGMENT, FROM_MUSIC_CONTENT -> songBinder.notifyUpdateUi()
                FROM_MUSIC_NEXT -> songBinder.playForward()
                FROM_MUSIC_STATE -> songBinder.changePlayState()
                FROM_MUSIC_PRE -> songBinder.playRewind()
                else -> {
                    //判断要播放的position和正在播放的position是否相等
                    var position = it.getIntExtra("position", -1)
                    if (position != currentPosition) {
                        currentPosition = position
                        list = it.getParcelableArrayListExtra<SongBean>("SONG_LIST")
                        //播放Song
                        songBinder.playSong()
                    } else {
                        //通知更新界面
                        songBinder.notifyUpdateUi()
                    }
                }
            }
        }
        return START_NOT_STICKY//非粘性
    }

    override fun onBind(intent: Intent?): IBinder {
        return songBinder
    }

    /**
     * 持有binder
     */
    inner class SongBinder : Binder(), ISongBinder, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
        /**
         * 播放指定歌曲
         */
        override fun playSong(position: Int) {
            currentPosition = position
            playSong()
        }

        /**
         * 返回当前歌单
         */
        override fun getSongList(): ArrayList<SongBean>? {
            return list
        }

        /**
         * 获取当前播放模式
         */
        override fun getPlayMode(): Int {
            return mode
        }

        /**
         * 上一曲
         */
        override fun playRewind() {

            list?.let {
                when (mode) {
                    MODE_RANDOM -> currentPosition = Random().nextInt(it.size)
                    else -> {
                        currentPosition -= 1
                        if (currentPosition < 0) currentPosition = it.size - 1
                    }
                }
                playSong()
            }
        }

        /**
         * 下一曲
         */
        override fun playForward() {
            list?.let {
                when (mode) {
                    MODE_RANDOM -> currentPosition = Random().nextInt(it.size)
                    else -> currentPosition = (currentPosition + 1) % it.size
                }
                playSong()
            }
        }

        /**
         * 改变播放模式
         */
        override fun changePlayMode() {
            when (mode) {
                MODE_ALL -> mode = MODE_RANDOM
                MODE_RANDOM -> mode = MODE_SINGLE
                MODE_SINGLE -> mode = MODE_ALL
            }
            sp.edit().putInt("MODE", mode)
        }

        /**
         * seekto.快进 , 快退
         */
        override fun seekTo(progress: Int) {
            mediaPlayer?.seekTo(progress)
        }

        /**
         * 获取当前进度
         */
        override fun getCurrentProgress(): Int {
            return mediaPlayer?.currentPosition ?: 0
        }

        /**
         * 获取影片总时长
         */
        override fun getDurtion(): Int {
            return mediaPlayer?.duration ?: 0
        }

        /**
         * 获取当前播放状态
         */
        override fun getPlayState(): Boolean? {
            return mediaPlayer?.isPlaying
        }

        /**
         * 改变播放器播放中状态
         */
        override fun changePlayState() {
            var state = getPlayState()
            state?.let {
                if (it) {
                    mediaPlayer?.pause()
                    //通知播放暂停
                    isPlayMusic = false
                    notifyUpdateUi()
                    //通知播放按钮改变
                    notification?.contentView?.setImageViewResource(R.id.state, R.mipmap.note_btn_play_white)
                    notificationManager?.notify(1, notification)

                } else {
                    mediaPlayer?.start()
                    //通知播放开始
                    isPlayMusic = true
                    notifyUpdateUi()
                    //通知播放按钮改变
                    notification?.contentView?.setImageViewResource(R.id.state, R.mipmap.note_btn_pause_white)
                    notificationManager?.notify(1, notification)

                }
            }
        }

        /**
         * 播放
         */
        override fun playSong() {
            //判断mediaplayer是否存在 , 存在 就释放
            if (mediaPlayer != null) {
                mediaPlayer?.reset()
                mediaPlayer?.release()
                mediaPlayer = null
            }
            mediaPlayer = MediaPlayer()
            mediaPlayer?.let {
                it.setDataSource(list?.get(currentPosition)?.data)
                it.setOnPreparedListener(this)
                it.setOnCompletionListener(this)
                it.setOnErrorListener(this)
                it.prepareAsync()
            }
        }


        //播放错误回调
        override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
            return true
        }

        //播放完成回调
        override fun onCompletion(mp: MediaPlayer?) {
            list?.let {
                when (mode) {
                    MODE_ALL -> currentPosition = (currentPosition + 1) % it.size
                    MODE_RANDOM -> currentPosition = Random().nextInt(it.size)
                }
                playSong()
            }

        }

        //加载完成回调
        override fun onPrepared(mp: MediaPlayer?) {
            mediaPlayer?.start()
            //通知播放界面更新
            notifyUpdateUi()
            //通知播放开始
            isPlayMusic = true
            isliving = true
            //弹出通知栏
            showNotification()
            //通知播放按钮改变
            notification?.contentView?.setImageViewResource(R.id.state, R.mipmap.note_btn_pause_white)
            notificationManager?.notify(1, notification)

        }

        /**
         * 展示通知
         */
        private fun showNotification() {
            //获取通知manager
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            //创建通知
            notification = createNotification()
            notificationManager?.notify(1, notification)
        }

        /**
         * 创建通知
         */
        private fun createNotification(): Notification {
            return NotificationCompat.Builder(this@SongService)
                    .setTicker("正在播放${list?.get(currentPosition)?.display_name}")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setOngoing(true)//设置不能滑动删除通知
                    .setCustomContentView(createContentView())
                    .setContentIntent(getPendingIntent())
                    .build()
        }

        /**
         * 创建通知view
         */
        private fun createContentView(): RemoteViews? {
            var remoteView = RemoteViews(packageName, R.layout.notification)
            remoteView.setTextViewText(R.id.title, list?.get(currentPosition)?.display_name)
            remoteView.setTextViewText(R.id.artist, list?.get(currentPosition)?.artist)
            remoteView.setOnClickPendingIntent(R.id.pre, getPrePendingIntent())
            remoteView.setOnClickPendingIntent(R.id.state, getStatePendingIntent())
            remoteView.setOnClickPendingIntent(R.id.next, getNextPendingIntent())
            return remoteView
        }

        /**
         * 通知主体点击事件
         */
        private fun getPendingIntent(): PendingIntent? {
            var intentSong = Intent(this@SongService, SongPlayerActivity::class.java)
            intentSong.putExtra("from", FROM_MUSIC_CONTENT)
            return PendingIntent.getActivity(this@SongService
                    , 2, intentSong, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        /**
         * 通知中下一曲点击事件
         */
        private fun getNextPendingIntent(): PendingIntent? {
            var intent = Intent(this@SongService, SongService::class.java)
            intent.putExtra("from", FROM_MUSIC_NEXT)
            return PendingIntent.getService(this@SongService,
                    3, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        /**
         * 通知中播放状态点击事件
         */
        private fun getStatePendingIntent(): PendingIntent? {
            var intent = Intent(this@SongService, SongService::class.java)
            intent.putExtra("from", FROM_MUSIC_STATE)
            return PendingIntent.getService(this@SongService,
                    4, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        }

        /**
         * 通知中上一曲点击事件
         */
        private fun getPrePendingIntent(): PendingIntent? {
            var intent = Intent(this@SongService, SongService::class.java)
            intent.putExtra("from", FROM_MUSIC_PRE)
            return PendingIntent.getService(this@SongService,
                    5, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }


        /**
         * 通知界面更新
         */
        fun notifyUpdateUi() {
            EventBus.getDefault().post(list?.get(currentPosition))
        }

    }


    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        //取消所有的notification
        notificationManager?.cancelAll()
    }

}