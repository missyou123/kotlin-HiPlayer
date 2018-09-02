package cn.zk.hi.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import cn.zk.hi.ui.fragment.MoviePagerFragment

class MoviePagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
    private val list = arrayOf("推荐", "现场", "翻唱", "你曾是少年", "听BGM", "少儿", "经典")
    override fun getItem(p0: Int): Fragment {
        return MoviePagerFragment()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return list[position]
    }
}