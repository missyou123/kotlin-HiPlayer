package cn.zk.hi.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.zk.hi.utils.DisplayUtils

/**
 * 作者: zhukun on $(DATE)$(TIME).
 * 本项目activity的基类
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DisplayUtils.setCustomDensity(this ,application)
        setContentView(getLayoutId())
        initView()
        initData()
    }

    /**
     * 返回页面布局id , 需子类实现
     */
    abstract fun getLayoutId(): Int

    /**
     * 初始化view ,子类可以重写
     */
    open fun initView() {
    }

    /**
     * 初始化数据 ,子类可以重写
     */
    open fun initData() {
    }
	
	override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        window.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    }
}