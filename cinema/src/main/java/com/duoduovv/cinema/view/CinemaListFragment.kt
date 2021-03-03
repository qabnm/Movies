package com.duoduovv.cinema.view

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.duoduovv.cinema.R
import com.duoduovv.cinema.adapter.MainPageAdapter
import com.duoduovv.cinema.bean.MainBean
import com.duoduovv.cinema.viewmodel.CinemaListViewModel
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.NO_MORE_DATA
import dc.android.bridge.util.LoggerSnack
import dc.android.bridge.view.BaseViewModelFragment
import kotlinx.android.synthetic.main.fragment_cinema_list.*

/**
 * @author: jun.liu
 * @date: 2021/1/19 17:36
 * @des:首页
 */
class CinemaListFragment : BaseViewModelFragment<CinemaListViewModel>(), OnRefreshListener,
    OnLoadMoreListener {
    private var page = 1
    private var adapter: MainPageAdapter? = null
    private var column = ""
    override fun providerVMClass() = CinemaListViewModel::class.java

    override fun getLayoutId() = R.layout.fragment_cinema_list

    override fun initView() {
        rvList.layoutManager = GridLayoutManager(requireActivity(), 3)
        refreshLayout.apply {
            setRefreshHeader(ClassicsHeader(requireActivity()))
            setRefreshFooter(ClassicsFooter(requireActivity()))
            setOnRefreshListener(this@CinemaListFragment)
            setOnLoadMoreListener(this@CinemaListFragment)
        }
        viewModel.getMain().observe(this, { setData(viewModel.getMain().value) })
        viewModel.getMainRecommend().observe(this, {
            val value = viewModel.getMainRecommend().value
            mainBean?.mainRecommendBean?.recommends = value
            mainBean?.let { adapter?.notifyDataChanged(it) }
            if (refreshLayout.isLoading) refreshLayout.finishLoadMore()
        })
        viewModel.getNoMoreData().observe(this, { noMoreData(viewModel.getNoMoreData().value) })
    }

    private var mainBean: MainBean? = null
    private fun setData(value: MainBean?) {
        mainBean = value
        if (null != value && value.mainRecommendBean.recommends?.isNotEmpty() == true){
            rvList.visibility = View.VISIBLE
            if (null == adapter) {
                adapter = MainPageAdapter(requireActivity(), bean = value)
                rvList.adapter = adapter
            } else {
                adapter?.notifyDataChanged(value)
            }
            if (refreshLayout.isRefreshing) refreshLayout.finishRefresh()
        }else{
            rvList.visibility = View.GONE
        }


//        value?.let {
//            if (null == adapter) {
//                adapter = MainPageAdapter(requireActivity(), bean = value)
//                rvList.adapter = adapter
//            } else {
//                adapter?.notifyDataChanged(it)
//            }
//            if (refreshLayout.isRefreshing) refreshLayout.finishRefresh()
//        }
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
        column = arguments?.getString(ID) ?: ""
        viewModel.main(1, column)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshLayout.resetNoMoreData()
        page = 1
        viewModel.main(page, column = column)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page++
        viewModel.mainRecommend(page, column = column)
    }
}