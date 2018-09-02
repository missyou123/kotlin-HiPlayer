package cn.zk.hi.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import cn.zk.hi.utils.DataUtil
import cn.zk.hi.widget.MusicModeItemView

/**
 * 作者: zhukun on $(DATE)$(TIME).
 * music 主界面 适配器
 */
class MusicModeAdapter : RecyclerView.Adapter<MusicModeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(MusicModeItemView(p0.context))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        //初始化itemView
        var itemView = p0.itemView as MusicModeItemView
        itemView.refreshView(list[p1])
        //设置点击事件
        itemView.setOnClickListener {
            listenter?.invoke(list[p1].action)
        }
    }


    //获取数据
    val list by lazy { DataUtil.getMusicModeBeans() }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    //点击事件回调
    private var listenter: ((action: String) -> Unit)? = null

    fun setClickListener(listener: (action: String) -> Unit) {
        this.listenter = listener
    }

}