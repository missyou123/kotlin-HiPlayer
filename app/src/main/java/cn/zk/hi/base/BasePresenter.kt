package cn.zk.hi.base

/**
 * 作者: zhukun on $(DATE)$(TIME).
 * 项目presenter的基类
 */
interface BasePresenter {
    companion object {
        val TYPE_INIT_OR_REFRESH = 1
        val TYPE_LOAD_MORE = 2
    }

    /**
     * 解绑view
     */
     fun destoryView()
}