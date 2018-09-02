package cn.zk.hi.ui.fragment

import cn.zk.hi.R
import cn.zk.hi.adapter.MoviePagerAdapter
import cn.zk.hi.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_movie.*

/**
 * 作者: zhukun on $(DATE)$(TIME).
 * 视频页面
 */
class MovieFragment : BaseFragment() {

    override fun getLayoutId(): Int = R.layout.fragment_movie

    override fun initView() {
        toolBar.initMovieBar()
        var adapter = MoviePagerAdapter(childFragmentManager)
        viewPager.adapter = adapter
        //TabLayout关联viewpager
        tabLayout.setupWithViewPager(viewPager)
    }
}