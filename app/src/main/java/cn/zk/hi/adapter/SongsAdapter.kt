package cn.zk.hi.adapter

import android.content.Context
import android.database.Cursor
import android.support.v4.widget.CursorAdapter
import android.view.View
import android.view.ViewGroup
import cn.zk.hi.model.SongBean
import cn.zk.hi.widget.SongItemView


/**
 * 歌单的adapter
 */
class SongsAdapter(context: Context?, c: Cursor?) : CursorAdapter(context, c) {
    override fun newView(p0: Context?, p1: Cursor?, p2: ViewGroup?): View {
        return SongItemView(p0)
    }

    override fun bindView(p0: View?, p1: Context?, p2: Cursor?) {
        //通过cursor获取数据,刷新view
        var songBean = SongBean.getSongBean(p2)
        (p0 as SongItemView).refreshView(songBean)
    }
}