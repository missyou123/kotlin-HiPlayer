package cn.zk.hi.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import cn.zk.hi.R
import cn.zk.hi.adapter.MusicModeAdapter
import cn.zk.hi.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_music_mode.*

/**
 * music功能界面
 */
class MusicModeFragment:BaseFragment() {
    //模块的适配器
    private val musicAdapter by lazy { MusicModeAdapter() }

    override fun getLayoutId(): Int = R.layout.fragment_music_mode

    override fun initView() {
        //初始化toolbar
        toolBar.initMusicModeBar()
        //初始化模块  列表
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            musicAdapter.setClickListener {
                //判断action 跳转对应界面
                clickActionListener?.invoke(it)
            }
            adapter = musicAdapter
        }
    }

    //功能栏点击事件
    private var clickActionListener:((action:String) ->Unit) ? = null

    fun setClickActionListener(listener : (action:String) ->Unit){
        this.clickActionListener = listener
    }

}