package cn.zk.hi.net

/**
 * http请求结果回调ß
 */
interface ResponseCallBack<RESPONSE> {
    fun onError(type:Int , msg:String)
    fun onSuccess(type:Int , response:RESPONSE)
}