package cn.zk.hi.ui.fragment

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import cn.zk.hi.R
import cn.zk.hi.adapter.FavoriteAdapter
import cn.zk.hi.base.BaseFragment
import cn.zk.hi.contract.FavoritesContract
import cn.zk.hi.model.FavoriteBean
import cn.zk.hi.presenter.FavoritesPresenter
import cn.zk.hi.ui.activity.PlayerActivity
import cn.zk.hi.utils.UrlUtil
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * 作者: zhukun on $(DATE)$(TIME).
 * 推荐页面
 */
class FavoritesFragment : BaseFragment(),FavoritesContract.View {

    //适配器
    private val favoriteAdapter by lazy { FavoriteAdapter() }
    //P类
    private val presenter by lazy { FavoritesPresenter(this) }


    override fun getLayoutId(): Int = R.layout.fragment_favorites

    override fun initView() {
        toolBar.initFavoritesBar()
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            favoriteAdapter.setListener { goPlay(it) }
            adapter = favoriteAdapter
            var divider = DividerItemDecoration(context ,DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(context,R.drawable.divider)?.let { divider.setDrawable(it) }
            addItemDecoration(divider)
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //判断当前滚动是否停止
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    val layoutManager = recyclerView.layoutManager
                    if(layoutManager is LinearLayoutManager){
                        //获取看见的最后一个item的position
                        var lastPosition = layoutManager.findLastVisibleItemPosition()
                        if(lastPosition == favoriteAdapter.itemCount - 1){
                            //到最后一条
                            loadMoreData(favoriteAdapter.itemCount -1)
                        }
                    }
                }
            }
        })
        //下拉刷新
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary)
        swipeRefreshLayout.setOnRefreshListener {
            loadFirstData()
        }
    }

    override fun initData() {
        loadFirstData()
    }

    /**
     * 首次加载数据
     */
    fun loadFirstData(){
        var url = UrlUtil.getFavoritesUrl(0 ,10)
        Log.e("url" ,url)
        presenter.loadData()
    }

    /**
     * 加载更多
     */
    fun loadMoreData(start :Int){
        presenter.loadMoreData(start)
    }


    override fun loadError(type: Int, msg: String) {
        swipeRefreshLayout.isRefreshing = false
        Log.e("FavoritesFragment" , "$msg")
    }

    override fun loadFirstSuccess(list: List<FavoriteBean>) {
        swipeRefreshLayout.isRefreshing = false
        favoriteAdapter.setData(list)
    }

    override fun loadMoreSuccess(list: List<FavoriteBean>) {
        swipeRefreshLayout.isRefreshing = false
        favoriteAdapter.setMoreData(list)
    }

    /**
     * 播放影片
     */
    private fun goPlay(it: FavoriteBean) {
        startActivity<PlayerActivity>("FAVORITEBEAN" to  it)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destoryView()
    }
}