package com.duoduovv.movie.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.util.RouterPath
import com.duoduovv.movie.adapter.MovieSubjectListAdapter
import com.duoduovv.movie.bean.SubjectListBean
import com.duoduovv.movie.databinding.FragmentSubjectBinding
import com.duoduovv.movie.viewmodel.SubjectListViewModel
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.TITLE
import dc.android.bridge.view.BaseViewModelFragment

/**
 * @author: jun.liu
 * @date: 2021/6/25 10:25
 * @des:专题
 */
class SubjectFragment : BaseViewModelFragment<SubjectListViewModel>(), OnRefreshListener,
    OnLoadMoreListener {
    private lateinit var mbind: FragmentSubjectBinding
    private var page = 1
    private var adapter: MovieSubjectListAdapter? = null
    override fun providerVMClass() = SubjectListViewModel::class.java
    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSubjectBinding.inflate(inflater, container, false)

    override fun initView() {
        mbind = baseBinding as FragmentSubjectBinding
        adapter = MovieSubjectListAdapter()
        mbind.rvList.adapter = adapter
        adapter?.setOnItemClickListener { adapter, _, position ->
            val dataBean = (adapter as MovieSubjectListAdapter).data[position]
            ARouter.getInstance().build(RouterPath.PATH_SUBJECT_DETAIL)
                .withString(TITLE, dataBean.title).withString(ID, dataBean.subjectId)
                .withString("coverUrl", dataBean.coverUrl).withString("des", dataBean.des?:"")
                .navigation()
        }
        viewModel.getSubjectList().observe(this, { setList(viewModel.getSubjectList().value) })
        viewModel.getNoMoreData().observe(this, { noMoreData(viewModel.getNoMoreData().value) })
        mbind.refreshLayout.apply {
            setRefreshHeader(ClassicsHeader(context))
            setRefreshFooter(ClassicsFooter(context))
            setOnRefreshListener(this@SubjectFragment)
            setOnLoadMoreListener(this@SubjectFragment)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SubjectFragment()
    }

    private fun setList(list: List<SubjectListBean>?) {
        adapter?.setList(list)
        finishLoading()
    }

    override fun finishLoading() {
        if (mbind.refreshLayout.isRefreshing) mbind.refreshLayout.finishRefresh()
        if (mbind.refreshLayout.isLoading) mbind.refreshLayout.finishLoadMore()
    }

    override fun initData() {
        viewModel.subjectList(page)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshLayout.resetNoMoreData()
        page = 1
        initData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page++
        initData()
    }

    /**
     * 分页没有更多数据的通知
     * @param flag String?
     */
    private fun noMoreData(flag: String?) {
        if (BridgeContext.NO_MORE_DATA == flag) {
            //没有更多数据了
            mbind.refreshLayout.apply {
                finishLoadMoreWithNoMoreData()
                setNoMoreData(true)
            }
        }
    }
}