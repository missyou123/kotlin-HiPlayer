package cn.zk.hi.presenter

import cn.zk.hi.base.BasePresenter.Companion.TYPE_INIT_OR_REFRESH
import cn.zk.hi.base.BasePresenter.Companion.TYPE_LOAD_MORE
import cn.zk.hi.contract.FavoritesContract
import cn.zk.hi.model.FavoriteBean
import cn.zk.hi.net.FavoritesRequest
import cn.zk.hi.net.ResponseCallBack
import cn.zk.hi.utils.UrlUtil

class FavoritesPresenter(var view: FavoritesContract.View?)
    : FavoritesContract.Presenter, ResponseCallBack<List<FavoriteBean>> {

    /**
     * 首次加载推荐数据
     */
    override fun loadData() {
        FavoritesRequest(TYPE_INIT_OR_REFRESH, UrlUtil.getFavoritesUrl(0, 10), this).excute()
    }

    /**
     * 加载更多
     */
    override fun loadMoreData(start: Int) {
        FavoritesRequest(TYPE_LOAD_MORE , UrlUtil.getFavoritesUrl(start , 10) , this).excute()
    }

    //请求失败
    override fun onError(type: Int, msg: String) {
        view?.loadError(type, msg)
    }

    //请求成功
    override fun onSuccess(type: Int, response: List<FavoriteBean>) {
        when(type){
            TYPE_INIT_OR_REFRESH -> view?.loadFirstSuccess(response)
            TYPE_LOAD_MORE -> view?.loadMoreSuccess(response)
        }
    }

    /**
     * 解绑view
     */
    override fun destoryView() {
        if(view !=null) view = null
    }
}