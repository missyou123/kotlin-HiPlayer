package cn.zk.hi.widget

import android.content.Context
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import cn.zk.hi.R
import cn.zk.hi.adapter.SongListPopAdapter
import org.jetbrains.anko.find

/**
 * 作者: zhukun on $(DATE)$(TIME).
 * 歌单
 */
class SongListPopWindow(context: Context, songListPopAdapter: SongListPopAdapter) : PopupWindow() {

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.pop_song_list, null, false)
        val recyclerView = view.find<RecyclerView>(R.id.recyclerView)
        //加载歌单列表
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = songListPopAdapter
        }
        contentView = view
        //设置pop的宽高
        width = ViewGroup.LayoutParams.MATCH_PARENT
        //设置高度为屏幕高度4/7
        val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        manager.defaultDisplay.getSize(point)
        val windowH = point.y
        height = (windowH * 4) / 7
        //设置获取焦点
        isFocusable = true
        //设置外部点击
        isOutsideTouchable = true
        //能够响应返回按钮(低版本popwindow点击返回按钮能够dismiss关键)
        setBackgroundDrawable(ColorDrawable())
    }
}