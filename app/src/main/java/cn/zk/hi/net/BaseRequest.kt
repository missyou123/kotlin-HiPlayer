package cn.zk.hi.net

import com.google.gson.Gson
import java.lang.reflect.ParameterizedType

/**
 * http请求基类
 */
open class BaseRequest<RESPONSE>(val type: Int, val url: String
                            , val responseCallBack: ResponseCallBack<RESPONSE>) {
    /**
     * 发送网络请求
     */
    fun excute(){
        HttpManager.httpManager.sendRequest(this)
    }

    /**
     * 解析网络请求结果
     */
    fun parseResult(result: String?): RESPONSE {
        val gson = Gson()
        //获取泛型类型
        val type = (this.javaClass
                .genericSuperclass as ParameterizedType).getActualTypeArguments()[0]
        val list = gson.fromJson<RESPONSE>(result, type)
        return list
    }
}