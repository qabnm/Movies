package com.duoduovv.cinema.view

import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.cinema.R
import com.duoduovv.cinema.adapter.MainPageAdapter
import com.duoduovv.cinema.bean.FilmRecommendBean
import com.duoduovv.cinema.bean.MainBean
import com.duoduovv.cinema.viewmodel.CinemaListViewModel
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.RouterPath.Companion.PATH_MOVIE_DETAIL
import com.duoduovv.common.util.SharedPreferencesHelper
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.LIST
import dc.android.bridge.BridgeContext.Companion.NO_MORE_DATA
import dc.android.bridge.view.BaseViewModelFragment
import dc.android.tools.LiveDataBus
import kotlinx.android.synthetic.main.fragment_cinema_list.*
import java.util.ArrayList

/**
 * @author: jun.liu
 * @date: 2021/1/19 17:36
 * @des:首页列表
 */
class CinemaListFragment : BaseViewModelFragment<CinemaListViewModel>(), OnRefreshListener,
    OnLoadMoreListener, MainPageAdapter.OnItemClickListener {
    override fun getLayoutId() = R.layout.fragment_cinema_list
    override fun providerVMClass() = CinemaListViewModel::class.java
    private var page = 1
    private var adapter: MainPageAdapter? = null
    private var column = ""
    private var mainBean: MainBean? = null

    override fun initView() {
        adapter = null
        rvList.layoutManager = GridLayoutManager(requireActivity(), 3)
        refreshLayout.apply {
            setRefreshHeader(ClassicsHeader(requireActivity()))
            setRefreshFooter(ClassicsFooter(requireActivity()))
            setOnRefreshListener(this@CinemaListFragment)
            setOnLoadMoreListener(this@CinemaListFragment)
        }
        viewModel.getMain().observe(this, { setData(viewModel.getMain().value) })
        viewModel.getMainRecommend().observe(this, {
//            dismissLoading()
            val value = viewModel.getMainRecommend().value
            mainBean?.mainRecommendBean?.recommends = value
            mainBean?.let { adapter?.notifyDataChanged(it) }
            if (refreshLayout.isLoading) refreshLayout.finishLoadMore()
        })
        viewModel.getNoMoreData().observe(this, { noMoreData(viewModel.getNoMoreData().value) })
    }

    private fun setData(value: MainBean?) {
//        dismissLoading()
        mainBean = value
        if (null != value) {
            rvList.visibility = View.VISIBLE
            if (null == adapter) {
                adapter = MainPageAdapter(requireActivity(), bean = value)
                rvList.adapter = adapter
                adapter?.setOnItemClickListener(this)
            } else {
                adapter?.notifyDataChanged(value)
            }
            if (refreshLayout.isRefreshing) refreshLayout.finishRefresh()
        } else {
            rvList.visibility = View.GONE
        }
    }

    /**
     * 没有更多数据了
     * @param flag String?
     */
    private fun noMoreData(flag: String?) {
        if (flag == NO_MORE_DATA) {
            //没有更多的数据了
            refreshLayout.finishLoadMoreWithNoMoreData()
            refreshLayout.setNoMoreData(true)
        }
    }

    override fun initData() {
//        showLoading()
        column = arguments?.getString(ID) ?: ""
        viewModel.main(1, column)
    }

    /**
     * 下拉刷新
     * @param refreshLayout RefreshLayout
     */
    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshLayout.resetNoMoreData()
        page = 1
        viewModel.main(page, column = column)
    }

    /**
     * 请求分页数据
     * @param refreshLayout RefreshLayout
     */
    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page++
        viewModel.mainRecommend(page, column = column)
    }

    /**
     * 点击分类 跳转片库
     * @param typeId String
     */
    override fun onCategoryClick(typeId: String) {
        LiveDataBus.get().with(BridgeContext.TYPE_ID).value = typeId
    }

    /**
     * 跳转影视详情
     * @param movieId String
     */
    override fun onMovieClick(movieId: String, way:String) {
        val ways = if (way == "-1"){
            //这是从banner过来的 接口没有添加这个字段
            SharedPreferencesHelper.helper.getValue(BridgeContext.WAY,"") as String
        }else way
        val path = if (ways == BridgeContext.WAY_VERIFY) {
            RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG
        } else {
            PATH_MOVIE_DETAIL
        }
        ARouter.getInstance().build(path).withString(ID, movieId).navigation()
    }

    /**
     * 今日推荐查看更多
     */
    override fun onTodayMoreClick(dataList:List<FilmRecommendBean>) {
//        (rvList.layoutManager as GridLayoutManager).scrollToPositionWithOffset(3, 0)
        ARouter.getInstance().build(RouterPath.PATH_RECOMMEND).withParcelableArrayList(LIST, dataList as ArrayList<out Parcelable>).navigation()
    }
}