package cn.zk.hi.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * 作者: zhukun on $(DATE)$(TIME).
 * 本项目fragment的基类
 */
abstract class BaseFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context , getLayoutId() , null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initData()
    }

    /**
     * 初始化
     */
    open fun init() {
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
}