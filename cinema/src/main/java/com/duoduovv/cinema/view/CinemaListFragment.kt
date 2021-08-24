package com.duoduovv.cinema.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.cinema.adapter.CinemaListAdapter
import com.duoduovv.cinema.bean.ColumnBean
import com.duoduovv.cinema.databinding.FragmentCinemaListBinding
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
import dc.android.bridge.BridgeContext.Companion.NO_MORE_DATA
import dc.android.bridge.BridgeContext.Companion.TITLE
import dc.android.bridge.EventContext
import dc.android.bridge.view.BaseViewModelFragment
import dc.android.tools.LiveDataBus
import java.util.*

/**
 * @author: jun.liu
 * @date: 2021/1/19 17:36
 * @des:首页列表
 */
class CinemaListFragment : BaseViewModelFragment<CinemaListViewModel>(), OnRefreshListener,
    OnLoadMoreListener, CinemaListAdapter.OnItemClickListener {
    override fun providerVMClass() = CinemaListViewModel::class.java
    private var page = 1
    private var adapter: CinemaListAdapter? = null
    private var column = ""
    private var tabName: String = ""
    private lateinit var mBind: FragmentCinemaListBinding
    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCinemaListBinding.inflate(inflater, container, false)

    companion object {
        @JvmStatic
        fun newInstance(id: String, tabName: String): CinemaListFragment {
            val fragment = CinemaListFragment()
            val bundle = Bundle()
            bundle.putString(ID, id)
            bundle.putString(TITLE, tabName)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {
        mBind = baseBinding as FragmentCinemaListBinding
        mBind.refreshLayout.apply {
            setRefreshHeader(ClassicsHeader(context))
            setRefreshFooter(ClassicsFooter(context))
            setOnRefreshListener(this@CinemaListFragment)
            setOnLoadMoreListener(this@CinemaListFragment)
        }
        viewModel.getCinemaList().observe(viewLifecycleOwner, { setData(viewModel.getCinemaList().value) })
        viewModel.getNoMoreData().observe(viewLifecycleOwner, { noMoreData(viewModel.getNoMoreData().value) })
    }

    private fun setData(dataList: List<ColumnBean>?) {
        if (dataList?.isNotEmpty() == true) {
            if (null == adapter) {
                adapter =
                    CinemaListAdapter(dataList as ArrayList<ColumnBean>, requireContext(), this)
                mBind.rvList.adapter = adapter
                adapter?.setOnItemClickListener(this)
            } else {
                adapter?.notifyDataChanged(dataList as ArrayList<ColumnBean>, page)
            }
            (parentFragment as? CinemaFragment)?.showRecord()
        }
        finishLoading()
    }

    override fun finishLoading() {
        if (mBind.refreshLayout.isRefreshing) mBind.refreshLayout.finishRefresh()
        if (mBind.refreshLayout.isLoading) mBind.refreshLayout.finishLoadMore()
    }

    override fun onDestroyView() {
        adapter?.onDestroy()
        mBind.refreshLayout.removeAllViews()
        adapter = null
        super.onDestroyView()
    }

    /**
     * 没有更多数据了
     * @param flag String?
     */
    private fun noMoreData(flag: String?) {
        if (flag == NO_MORE_DATA) {
            //没有更多的数据了
            mBind.refreshLayout.finishLoadMoreWithNoMoreData()
            mBind.refreshLayout.setNoMoreData(true)
        }
    }

    override fun initData() {
        column = arguments?.getString(ID) ?: ""
        tabName = arguments?.getString(TITLE) ?: ""
        loadData()
    }

    private fun loadData() {
        viewModel.cinemaList(page, column)
    }

    /**
     * 下拉刷新
     * @param refreshLayout RefreshLayout
     */
    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshLayout.resetNoMoreData()
        page = 1
        loadData()
    }

    /**
     * 请求分页数据
     * @param refreshLayout RefreshLayout
     */
    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page++
        loadData()
    }

    /**
     * 点击分类 跳转片库
     * @param typeId String
     */
    override fun onCategoryClick(typeId: String, typeName: String) {
        LiveDataBus.get().with(BridgeContext.TYPE_ID).value = typeId
        val map = mapOf("categoryName" to typeName)
        EventContext.uMenEvent(EventContext.EVENT_CATEGORY, map)
    }

    /**
     * 跳转影视详情
     * @param movieId String
     */
    override fun onMovieClick(movieId: String, way: String) {
        val ways = if (way == "-1") {
            //这是从banner过来的 接口没有添加这个字段
            SharedPreferencesHelper.helper.getValue(BridgeContext.WAY, "") as String
        } else way
        val path = if (ways == BridgeContext.WAY_VERIFY) {
            RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG
        } else {
            PATH_MOVIE_DETAIL
        }
        ARouter.getInstance().build(path).withString(ID, movieId).navigation()
        val map = mapOf("tabName" to tabName)
        EventContext.uMenEvent(
            if (way == "-1") EventContext.EVENT_BANNER_MOVIE_DETAIL else EventContext.EVENT_MOVIE_DETAIL,
            map
        )
    }

    /**
     * 今日推荐查看更多
     */
    override fun onMoreClick(titleName: String, id: String) {
        ARouter.getInstance().build(RouterPath.PATH_RECOMMEND).withString(ID, id)
            .withString(TITLE, titleName).navigation()
    }
}