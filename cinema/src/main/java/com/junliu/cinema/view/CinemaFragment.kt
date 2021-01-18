package com.junliu.cinema.view

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.junliu.cinema.R
import com.junliu.cinema.adapter.MainPageAdapter
import com.junliu.cinema.viewmodel.CinemaViewModel
import com.junliu.common.util.RouterPath
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dc.android.bridge.view.BaseFragment
import dc.android.bridge.view.BaseViewModelFragment
import kotlinx.android.synthetic.main.fragment_cinema.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * @author: jun.liu
 * @date: 2020/12/29 : 14:32
 * 首页
 */
@Route(path = RouterPath.PATH_CINEMA)
class CinemaFragment : BaseViewModelFragment<CinemaViewModel>(),OnRefreshListener,OnLoadMoreListener {
    override fun getLayoutId() = R.layout.fragment_cinema
    override fun providerVMClass() = CinemaViewModel::class.java
    private var page = 1
    private var adapter :MainPageAdapter?=null

    override fun initView() {
        tvSearch.setOnClickListener {
            ARouter.getInstance().build(RouterPath.PATH_SEARCH_ACTIVITY).navigation()
        }
        rvList.layoutManager = GridLayoutManager(requireActivity(), 3)
        refreshLayout.apply {
            setRefreshHeader(ClassicsHeader(requireActivity()))
            setRefreshFooter(ClassicsFooter(requireActivity()))
            setOnRefreshListener(this@CinemaFragment)
            setOnLoadMoreListener(this@CinemaFragment)
        }
    }

    override fun initData() {
        viewModel.main(page = page)
        viewModel.getMain().observe(this , Observer {
            val value = viewModel.getMain().value
            if (null == adapter) adapter = MainPageAdapter(requireActivity(),value!!)
        })
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
    }
}