package com.duoduovv.cinema.view

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.cinema.CinemaContext.Companion.KEY_WORD
import com.duoduovv.cinema.R
import com.duoduovv.cinema.adapter.SearchResultListAdapter
import com.duoduovv.cinema.bean.MovieItem
import com.duoduovv.cinema.bean.SearchResultList
import com.duoduovv.cinema.databinding.FragmentSearchResultListBinding
import com.duoduovv.cinema.viewmodel.SearchResultViewModel
import com.duoduovv.common.util.RouterPath
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.TYPE_ID
import dc.android.bridge.view.BaseViewModelFragment
import java.util.*

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
    private lateinit var mBind: FragmentSearchResultListBinding
    private var typeId = ""
    private var keyWord = ""
    private var page = 1
    private var vid = ""

    override fun initView() {
        mBind = baseBinding as FragmentSearchResultListBinding
        viewModel.getSearchResult().observe(this, { setData(viewModel.getSearchResult().value) })
        viewModel.getNoMoreData().observe(this, { noMoreData(viewModel.getNoMoreData().value) })
        mBind.rvList.layoutManager = LinearLayoutManager(requireActivity())
        mBind.refreshLayout.apply {
            setRefreshHeader(ClassicsHeader(context))
            setRefreshFooter(ClassicsFooter(context))
            setOnRefreshListener(this@SearchResultListFragment)
            setOnLoadMoreListener(this@SearchResultListFragment)
        }
    }

    companion object {
        fun newInstance(id: String, keyWord: String): SearchResultListFragment {
            val fragment = SearchResultListFragment()
            val bundle = Bundle()
            bundle.putString(ID, id)
            bundle.putString(KEY_WORD, keyWord)
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun setData(dataList: List<SearchResultList>?) {
        if (dataList?.isNotEmpty() == true) {
            mBind.includeEmpty.layoutEmpty.visibility = View.GONE
            mBind.refreshLayout.visibility = View.VISIBLE
            if (null == resultAdapter) {
                resultAdapter = SearchResultListAdapter(dataList, requireActivity(), this)
                mBind.rvList.adapter = resultAdapter
            } else {
                resultAdapter?.notifyDataChanged(dataList)
            }
        } else {
            mBind.includeEmpty.layoutEmpty.visibility = View.VISIBLE
            mBind.refreshLayout.visibility = View.GONE
        }
        finishLoading()
    }

    override fun finishLoading() {
        if (mBind.refreshLayout.isRefreshing) mBind.refreshLayout.finishRefresh()
        if (mBind.refreshLayout.isLoading) mBind.refreshLayout.finishLoadMore()
    }

    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSearchResultListBinding.inflate(inflater, container, false)

    override fun initData() {
        typeId = arguments?.getString(ID, "") ?: ""
        keyWord = arguments?.getString(KEY_WORD, "") ?: ""
        viewModel.searchResult(keyWord = keyWord, page = page, column = typeId)
    }

    /**
     * 跳转详情页面
     * @param movieId String
     */
    override fun onItemClick(movieId: String, way: String) {
        val path = if (way == BridgeContext.WAY_VERIFY) {
            RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG
        } else {
            RouterPath.PATH_MOVIE_DETAIL
        }
        ARouter.getInstance().build(path)
            .withString(ID, movieId).withString(TYPE_ID, vid).navigation()
    }

    /**
     * 点击了选集
     * @param vid String
     */
    override fun onSelectClick(vid: String, movieId: String, way: String) {
        this.vid = vid
        onItemClick(movieId, way)
    }

    /**
     * 更多选集
     * @param dataList List<MovieItem>
     */
    override fun onMoreSelectClick(
        dataList: List<MovieItem>,
        movieId: String,
        title: String,
        way: String,
        movieFlag: String
    ) {
        ARouter.getInstance().build(RouterPath.PATH_SEARCH_MORE_SELECT)
            .withString(BridgeContext.TITLE, title).withString(ID, movieId)
            .withParcelableArrayList(BridgeContext.LIST, dataList as ArrayList<out Parcelable>)
            .withString(BridgeContext.WAY, way)
            .withString(BridgeContext.FLAG, movieFlag)
            .navigation()
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

    override fun onDestroyView() {
        resultAdapter = null
        mBind.refreshLayout.removeAllViews()
        super.onDestroyView()
    }

    /**
     * 没有更多数据
     * @param flag String?
     */
    private fun noMoreData(flag: String?) {
        if (BridgeContext.NO_MORE_DATA == flag) {
            //没有更多数据了
            mBind.refreshLayout.apply {
                finishLoadMoreWithNoMoreData()
                setNoMoreData(true)
            }
        }
    }
}