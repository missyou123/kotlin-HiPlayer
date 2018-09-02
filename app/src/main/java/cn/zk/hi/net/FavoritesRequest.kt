package cn.zk.hi.net

import cn.zk.hi.model.FavoriteBean

/**
 * 推荐位的请求类
 */
class FavoritesRequest(type: Int, url: String, callBack: ResponseCallBack<List<FavoriteBean>>)
    : BaseRequest<List<FavoriteBean>>(type, url, callBack)