package cn.zk.hi.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import cn.zk.hi.ui.fragment.*

/**
 * 作者: zhukun on $(DATE)$(TIME).
 */
class FragmentFactory private constructor() {
    private var currentPosition = -1

    companion object {
        val fragmentFactory by lazy { FragmentFactory() }
    }

    //通过惰性 加载所有的fragment
    private val favoritesFragment by lazy { FavoritesFragment() }
    private val movieFragment by lazy { MovieFragment() }
    private val musicFragment by lazy { MusicFragment() }
    private val personalFragment by lazy { PersonalFragment() }
    private val aboutFragment by lazy { AboutFragment() }

    //根据id获取对应的fragment
    private fun getFragment(tabId: Int): Fragment? {
        return when (tabId) {
            0 -> favoritesFragment
            1 -> movieFragment
            2 -> musicFragment
            3 -> aboutFragment
            4 -> personalFragment
            else -> null
        }
    }

    /**
     * show对应的fragment
     */
    fun showFragment(id: Int, position: Int, transaction: FragmentTransaction) {

        //先hide当前的fragment
        var currentFragment = getFragment(currentPosition)
        currentFragment?.let { transaction.hide(it) }
        //显示选中页面
        currentPosition = position
        var targetFragment = getFragment(currentPosition)
        targetFragment?.let {
            if (!it.isAdded) {
                transaction.add(id, targetFragment)
            } else {
                transaction.show(targetFragment)
            }
        }
        transaction.commit()
    }

}