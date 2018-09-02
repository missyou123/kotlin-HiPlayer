package cn.zk.hi.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import cn.zk.hi.model.SongBean
import cn.zk.hi.widget.SongPopItemView

/**
 * 作者: zhukun on $(DATE)$(TIME).
 * pop歌单的adapter
 */
class SongListPopAdapter(var list: ArrayList<SongBean>) : RecyclerView.Adapter<SongListPopAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(SongPopItemView(p0.context))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        var itemView = p0.itemView as SongPopItemView
        itemView.refreshView(list[p1])
        itemView.setOnClickListener{
            listenter?.invoke(p1)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    //点击事件回调
    private var listenter: ((position: Int) -> Unit)? = null

    fun setClickListener(listener: (position: Int) -> Unit) {
        this.listenter = listener
    }
}