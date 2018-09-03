package cn.zk.hi.ui.activity

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import cn.zk.hi.R
import cn.zk.hi.base.BaseActivity
import cn.zk.hi.service.SongService
import cn.zk.hi.utils.DisplayUtils
import cn.zk.hi.utils.FragmentFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_bar.*
import org.jetbrains.anko.startActivity

/**
 * 主页 容器
 */
class MainActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView() {
        //绑定BottomBar和fragment
        bindBarAndFrag()
        //初始化第一个tab选中
        changeChildState(0)
        initListener()

    }

    /**
     * 初始化各组件的监听事件
     */
    private fun initListener() {
        music_icon_animation.setOnClickListener {
            //判断是否播放过. 没有则跳转歌单页
            if (SongService.isliving){
                startActivity<SongPlayerActivity>("from" to SongService.FROM_MUSIC_FRAGMENT)
            }  else changeChildState(2)
        }
    }

    /**
     * 绑定BottomBar和fragment
     */
    private fun bindBarAndFrag() {
        var childCount = bottom_bar.childCount
        for (i in 0 until childCount) {
            bottom_bar.getChildAt(i).setOnClickListener {
                changeChildState(i)
            }
        }
    }

    /**
     * 更新tab中子控件的状态 , 切换对应的fragment
     */
    private fun changeChildState(position: Int) {
        for (i in 0 until bottom_bar.childCount) {
            var child = bottom_bar.getChildAt(i)
            //找出当前选中tab
            if (i == position) setEnable(child, false) else setEnable(child, true)
        }
        FragmentFactory.fragmentFactory.showFragment(R.id.container ,position ,supportFragmentManager.beginTransaction())
//                getFragment(position)?.let {
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, it).commit()
//        }

    }

    /**
     * 修改选中,未选中状态
     */
    private fun setEnable(child: View?, b: Boolean) {
        child?.let {
            it.isEnabled = b
            if (it is ViewGroup) {
                for (i in 0 until it.childCount) {
                    it.getChildAt(i).isEnabled = b
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //判断音乐是否播放， 显示动画
        if(SongService.isPlayMusic){
            var animationDrawable = music_icon_animation.drawable as AnimationDrawable
            animationDrawable.start()
        }
    }

    override fun onPause() {
        super.onPause()
        var animationDrawable = music_icon_animation.drawable as AnimationDrawable
        animationDrawable.stop()
    }

}
