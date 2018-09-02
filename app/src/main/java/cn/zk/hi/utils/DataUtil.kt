package cn.zk.hi.utils

import cn.zk.hi.R
import cn.zk.hi.model.MusicModeBean

/**
 * 作者: zhukun on $(DATE)$(TIME).
 */
object DataUtil {
    /**
     * 获取music主界面 的 模块 数据
     */
    fun getMusicModeBeans(): ArrayList<MusicModeBean> {
        var list = ArrayList<MusicModeBean>()
        for (i in 0 until 7) {
            when (i) {
                0 -> list.add(MusicModeBean("本地音乐", R.mipmap.music_icon_1, "15"
                        , R.mipmap.icon_cell_rightarrow, 1, "OPEN_SONG"))
                1 -> list.add(MusicModeBean("最近播放", R.mipmap.music_icon_2, "15"
                        , R.mipmap.icon_cell_rightarrow, 1, "OPEN_SONG"))
                2 -> list.add(MusicModeBean("我的电台", R.mipmap.music_icon_3, "15"
                        , R.mipmap.icon_cell_rightarrow, 1, "OPEN_SONG"))
                3 -> list.add(MusicModeBean("我的收藏", R.mipmap.music_icon_4, "15"
                        , R.mipmap.icon_cell_rightarrow, 1, "OPEN_SONG"))
                4 -> list.add(MusicModeBean("我的打赏", R.mipmap.music_icon_5, "15"
                        , R.mipmap.icon_cell_rightarrow, 1, "OPEN_SONG"))
                5 -> list.add(MusicModeBean("我的分享", R.mipmap.music_icon_6, "15"
                        , R.mipmap.icon_cell_rightarrow, 1, "OPEN_SONG"))

            }
        }

        return list
    }
}