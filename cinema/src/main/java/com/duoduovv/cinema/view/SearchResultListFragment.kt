package com.duoduovv.cinema.view

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.cinema.CinemaContext
import com.duoduovv.cinema.R
import com.duoduovv.cinema.adapter.SearchResultListAdapter
import com.duoduovv.cinema.bean.SearchResultList
import com.duoduovv.cinema.viewmodel.SearchResultViewModel
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.SharedPreferencesHelper
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.TYPE_ID
import dc.android.bridge.view.BaseViewModelFragment
import kotlinx.android.synthetic.main.fragment_search_result_list.*
import kotlinx.android.synthetic.main.layout_search_empty.*

/**
 * @author: jun.liu
 * @date: 2021/1/8 16:59
 * @des:搜索结果的列表页
 */
class SearchResultListFragment : BaseViewModelFragment<SearchResultViewModel>(),
    SearchResultListAdapter.OnItemClickListener, OnRefreshListener, OnLoadMoreListener {
    private var resultAdapter: SearchResultListAdapter? = null
    override fun providerVMClass() = SearchResultViewModel::class.java

    override fun getLayoutId() = R.layout.fragment_search_result_list
    private var typeId = ""
    private var keyWord = ""
    private var page = 1
    private var vid = ""

    override fun initView() {
        resultAdapter = null
        viewModel.getSearchResult().observe(this, { setData(viewModel.getSearchResult().value) })
        viewModel.getNoMoreData().observe(this, { noMoreData(viewModel.getNoMoreData().value) })
        rvList.layoutManager = LinearLayoutManager(requireActivity())
        refreshLayout.apply {
            setRefreshHeader(ClassicsHeader(requireActivity()))
            setRefreshFooter(ClassicsFooter(requireActivity()))
            setOnRefreshListener(this@SearchResultListFragment)
            setOnLoadMoreListener(this@SearchResultListFragment)
        }
    }

    private fun setData(dataList: List<SearchResultList>?) {
        if (dataList?.isNotEmpty() == true) {
            layoutEmpty.visibility = View.GONE
            refreshLayout.visibility = View.VISIBLE
            if (null == resultAdapter) {
                resultAdapter = SearchResultListAdapter(dataList, requireActivity(), this)
                rvList.adapter = resultAdapter
            } else {
                resultAdapter?.notifyDataChanged(dataList)
            }
        } else {
            layoutEmpty.visibility = View.VISIBLE
            refreshLayout.visibility = View.GONE
        }
        finishLoading()
    }

    override fun finishLoading() {
        if (refreshLayout.isRefreshing) refreshLayout.finishRefresh()
        if (refreshLayout.isLoading) refreshLayout.finishLoadMore()
    }

    override fun initData() {
        typeId = arguments?.getString(BridgeContext.ID, "") ?: ""
        keyWord = arguments?.getString(CinemaContext.KEY_WORD, "") ?: ""
        viewModel.searchResult(keyWord = keyWord, page = page, column = typeId)
    }

    /**
     * 跳转详情页面
     * @param movieId String
     */
    override fun onItemClick(movieId: String) {
        val flag = SharedPreferencesHelper.helper.getValue(BridgeContext.isRes, 1)
        val path = if (flag == 1) {
            RouterPath.PATH_MOVIE_DETAIL
        } else {
            RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG
        }
        ARouter.getInstance().build(path)
            .withString(ID, movieId).withString(TYPE_ID, vid).navigation()
    }

    /**
     * 点击了选集
     * @param vid String
     */
    override fun onSelectClick(vid: String) {
        this.vid = vid
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        refreshLayout.resetNoMoreData()
        viewModel.searchResult(keyWord = keyWord, page = page, column = typeId)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page++
        viewModel.searchResult(keyWord = keyWord, page = page, column = typeId)
    }

    /**
     * 没有更多数据
     * @param flag String?
     */
    private fun noMoreData(flag: String?) {
        if (BridgeContext.NO_MORE_DATA == flag) {
            //没有更多数据了
            refreshLayout.apply {
                finishLoadMoreWithNoMoreData()
                setNoMoreData(true)
            }
        }
    }
}