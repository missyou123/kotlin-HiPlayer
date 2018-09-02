package cn.zk.hi.ui.fragment

import android.annotation.SuppressLint
import android.content.AsyncQueryHandler
import android.database.Cursor
import android.provider.MediaStore
import cn.zk.hi.R
import cn.zk.hi.adapter.SongsAdapter
import cn.zk.hi.base.BaseFragment
import cn.zk.hi.model.SongBean
import cn.zk.hi.ui.activity.SongPlayerActivity
import kotlinx.android.synthetic.main.fragment_songs.*
import org.jetbrains.anko.support.v4.startActivity


/**
 * 作者: zhukun on $(DATE)$(TIME).
 * 歌单界面
 */
class SongsFragment : BaseFragment() {
    //歌单适配器
    val cursorAdapter by lazy { SongsAdapter(context, null) }

    override fun getLayoutId(): Int = R.layout.fragment_songs

    override fun initView() {
        //初始化toolbar
        toolBar.initMusicSongsBar()
        //初始化listView  ,通过cursorAdapter适配
        listView.adapter = cursorAdapter
        listView.setOnItemClickListener { _, _, i, _ ->
            //通过cursor获取整个歌单 ,传入播放器
            var cursor = cursorAdapter.getItem(i) as Cursor
            var songList = SongBean.getSongs(cursor)
            //bfq
            startActivity<SongPlayerActivity>("SONG_LIST" to songList ,"position" to i)
        }
    }

    override fun initData() {
        //初始化歌单数据
        initSongsView()
    }

    /**
     * 通过MediaProvider查询歌单
     */
    private fun initSongsView() {
        //注册AsyncQueryHandler
        var asyncQueryHandler = @SuppressLint("HandlerLeak")
        object : AsyncQueryHandler(context?.contentResolver) {
            override fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor?) {
                cookie?.let {
                    //刷新adapter
                    var adapter = it as SongsAdapter
                    adapter.swapCursor(cursor)
                }
            }
        }

        //去查询数据
        asyncQueryHandler.startQuery(1, cursorAdapter,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.ARTIST), null, null, null)
    }

}