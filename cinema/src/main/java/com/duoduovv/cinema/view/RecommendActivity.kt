package com.duoduovv.cinema.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.cinema.R
import com.duoduovv.cinema.adapter.FilmRecommendAdapter
import com.duoduovv.cinema.bean.MovieMoreList
import com.duoduovv.cinema.databinding.ActivityRecommendBinding
import com.duoduovv.cinema.viewmodel.CinemaListViewModel
import com.duoduovv.common.util.RouterPath
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dc.android.bridge.BridgeContext
import dc.android.bridge.view.BaseViewModelActivity

/**
 * @author: jun.liu
 * @date: 2021/4/6 10:08
 * @des:今日推荐 热门推荐查看更多
 */
@Route(path = RouterPath.PATH_RECOMMEND)
class RecommendActivity : BaseViewModelActivity<CinemaListViewModel>(), OnRefreshListener,
    OnLoadMoreListener {
    override fun getLayoutId() = R.layout.activity_recommend
    override fun providerVMClass() = CinemaListViewModel::class.java
    private var adapter: FilmRecommendAdapter? = null
    private lateinit var mBind: ActivityRecommendBinding
    private var page = 1
    private var id = ""

    override fun initView() {
        mBind = ActivityRecommendBinding.bind(layoutView)
        adapter = FilmRecommendAdapter()
        mBind.rvList.adapter = adapter
        adapter?.setOnItemClickListener { adapter, _, position ->
            val movieId = (adapter as FilmRecommendAdapter).data[position].strId
            val way = adapter.data[position].way
            onMovieClick(movieId, way)
        }
        viewModel.getMovieMore().observe(this, { setData(viewModel.getMovieMore().value) })
        viewModel.getNoMoreData().observe(this, { noMoreData(viewModel.getNoMoreData().value) })
        mBind.refreshLayout.apply {
            setRefreshHeader(ClassicsHeader(context))
            setRefreshFooter(ClassicsFooter(context))
            setOnRefreshListener(this@RecommendActivity)
            setOnLoadMoreListener(this@RecommendActivity)
        }
    }

    private fun setData(list: List<MovieMoreList>?) {
        if (list?.isNotEmpty() == true) {
            adapter?.setList(list)
        }
        finishLoading()
    }

    override fun finishLoading() {
        if (mBind.refreshLayout.isRefreshing) mBind.refreshLayout.finishRefresh()
        if (mBind.refreshLayout.isLoading) mBind.refreshLayout.finishLoadMore()
    }

    override fun initData() {
        val titleName = intent.getStringExtra(BridgeContext.TITLE) ?: "更多推荐"
        id = intent.getStringExtra(BridgeContext.ID) ?: ""
        mBind.layoutTopBar.setTopTitle(titleName)
        loadData()
    }

    private fun loadData() {
        viewModel.movieMore(id, page)
    }

    /**
     * 跳转影视详情
     * @param movieId String
     */
    private fun onMovieClick(movieId: String, way: String) {
        val path = if (way == BridgeContext.WAY_VERIFY) {
            RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG
        } else {
            RouterPath.PATH_MOVIE_DETAIL
        }
        ARouter.getInstance().build(path).withString(BridgeContext.ID, movieId).navigation()
    }

    /**
     * 没有更多数据了
     * @param flag String?
     */
    private fun noMoreData(flag: String?) {
        if (flag == BridgeContext.NO_MORE_DATA) {
            //没有更多的数据了
            mBind.refreshLayout.finishLoadMoreWithNoMoreData()
            mBind.refreshLayout.setNoMoreData(true)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshLayout.resetNoMoreData()
        page = 1
        loadData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page++
        loadData()
    }
}