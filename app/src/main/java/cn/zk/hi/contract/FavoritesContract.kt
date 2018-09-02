package cn.zk.hi.contract

import cn.zk.hi.base.BasePresenter
import cn.zk.hi.model.FavoriteBean

/**
 * 推荐的协议类
 */
interface FavoritesContract {
    interface View {
        fun loadError(type: Int, msg: String)

        fun loadFirstSuccess(list: List<FavoriteBean>)

        fun loadMoreSuccess(list: List<FavoriteBean>)
    }

    interface Presenter : BasePresenter {
        fun loadData()
        fun loadMoreData(start: Int)
    }
}