package cn.zk.hi.service

import cn.zk.hi.model.SongBean

/**
 * 作者: zhukun on $(DATE)$(TIME).
 * songBinder 的 接口类
 */
interface ISongBinder {
    fun playSong()
    fun playSong(position: Int)
    fun changePlayState()
    fun getPlayState(): Boolean?
    fun getDurtion(): Int
    fun getCurrentProgress(): Int
    fun seekTo(progress: Int)
    fun playRewind()
    fun playForward()
    fun changePlayMode()
    fun getPlayMode(): Int
    fun getSongList(): ArrayList<SongBean>?
}