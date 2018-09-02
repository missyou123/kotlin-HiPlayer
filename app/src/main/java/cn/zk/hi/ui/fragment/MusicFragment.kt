package cn.zk.hi.ui.fragment

import android.graphics.drawable.AnimationDrawable
import cn.zk.hi.R
import cn.zk.hi.base.BaseFragment
import cn.zk.hi.contract.MusicContract
import cn.zk.hi.model.MusicEVentBean
import cn.zk.hi.service.SongService
import cn.zk.hi.ui.activity.SongPlayerActivity
import kotlinx.android.synthetic.main.fragment_music.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.support.v4.startActivity

/**
 * 作者: zhukun on $(DATE)$(TIME).
 * 音乐主界面
 */
class MusicFragment : BaseFragment(), MusicContract.View {
    //music功能界面
    private val musicModeFragment by lazy { MusicModeFragment() }
    //歌单界面
    private val songsFragment by lazy { SongsFragment() }

    override fun getLayoutId(): Int = R.layout.fragment_music

    override fun initView() {
        //添加功能栏页面
        addMusicModeFrag()
        //添加点击事件  mode页面
        musicModeFragment.setClickActionListener {
            changeChildFrag(it)
        }
    }

    /**
     * 添加功能栏页面
     */
    private fun addMusicModeFrag() {
        childFragmentManager.beginTransaction()
                .add(R.id.container, musicModeFragment).commit()
    }

    /**
     * 根据action切换页面
     */
    private fun changeChildFrag(type: String) {
        //判断页面类型
        when (type) {
            "OPEN_MODE" -> musicModeFragment
            "OPEN_SONG" -> songsFragment
            else -> null
        }?.let {
            childFragmentManager.beginTransaction()
                    .setCustomAnimations(
                            R.animator.slide_right_in,
                            R.animator.slide_left_out,
                            R.animator.slide_left_in,
                            R.animator.slide_right_out
                    )
                    .replace(R.id.container, it).addToBackStack(null)
                    .commit()
        }
    }

    override fun onStart() {
        super.onStart()
        //注册eventbus
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        //注销evnetBus
        EventBus.getDefault().unregister(this)
    }

    /**
     * eventbus  回调方法
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(musicEVentBean: MusicEVentBean) {
        //type 1 表示退出歌单fragment
        //     2 标识  播放音乐开始   3 标识  暂停
        when (musicEVentBean.type) {
            1 -> childFragmentManager.popBackStack()
        }

    }
}