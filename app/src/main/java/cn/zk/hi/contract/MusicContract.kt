package cn.zk.hi.contract

import cn.zk.hi.base.BasePresenter

/**
 * 作者: zhukun on $(DATE)$(TIME).
 * music 界面 的mvp协议
 */
interface MusicContract {

    interface View {

    }


    interface Presenter : BasePresenter {
        //获取歌单
        fun loadSongs()
    }
}