package cn.zk.hi.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import cn.zk.hi.model.FavoriteBean
import cn.zk.hi.widget.FavoriteItemView
import cn.zk.hi.widget.LoadingView

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.Viewholder>() {

    //数据集合
    val list = ArrayList<FavoriteBean>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Viewholder {
        if(p1 == 2){
            return Viewholder(LoadingView(p0.context))
        }
        return Viewholder(FavoriteItemView(p0.context))
    }

    override fun getItemCount(): Int = list.size + 1

    override fun onBindViewHolder(p0: Viewholder, p1: Int) {
        if(p1 == list.size) return
        var itemView = p0.itemView as FavoriteItemView
        itemView.refreshView(list[p1])
        //点击事件
        itemView.setOnClickListener{
            clickListener?.invoke(list[p1])
        }
    }


    //初始化数据
    fun setData(data: List<FavoriteBean>) {
        this.list.clear()
        this.list.addAll(data)
        notifyDataSetChanged()
    }

    //加载更多数据
    fun setMoreData(list: List<FavoriteBean>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (position == list.size) return 2
        return 1
    }


    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {}


    //点击事件
    var clickListener :((favoriteBean :FavoriteBean)->Unit) ? = null

    fun setListener(listener : (favoriteBean :FavoriteBean)->Unit){
        this.clickListener = listener
    }

}