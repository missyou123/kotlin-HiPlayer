package cn.zk.hi.presenter

import cn.zk.hi.contract.MusicContract

/**
 * 作者: zhukun on $(DATE)$(TIME).
 */
class MusicPresenter(var view : MusicContract.View?) : MusicContract.Presenter {
    /**
     * 加载数据库中的歌单
     */
    override fun loadSongs() {
    }

    /**
     * 解绑view
     */
    override fun destoryView() {
        if(view !=null) view = null
    }
}