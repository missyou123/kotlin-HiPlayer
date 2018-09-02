package cn.zk.hi.net

import android.util.Log
import cn.zk.hi.utils.ThreadUtil
import okhttp3.*
import java.io.IOException

/**
 * 网络请求管理者
 */
class HttpManager private constructor() {

    val client by lazy { OkHttpClient() }

    companion object {
        val httpManager by lazy { HttpManager() }
    }

    /**
     * 发送请求
     */
    fun <RESPONSE> sendRequest(req: BaseRequest<RESPONSE>) {
        //创建request
        val request = Request.Builder().url(req.url).get().build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                //切换线程 ，通知界面
                ThreadUtil.runOnMainThread(object : Runnable {
                    override fun run() {
                        req.responseCallBack.onError(req.type, "请求失败")
                    }

                })
            }

            override fun onResponse(call: Call?, response: Response?) {
                //处理数据结果
                var responseText = response?.body()?.string()
                //解析数据
                var result = req.parseResult(responseText)
                //切换线程 ，通知界面
                ThreadUtil.runOnMainThread(object : Runnable {
                    override fun run() {
                        req.responseCallBack.onSuccess(req.type, result)
                    }

                })
            }

        })
    }
}