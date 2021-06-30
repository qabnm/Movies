package com.duoduovv.movie.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.util.RouterPath
import com.duoduovv.movie.adapter.MovieSubjectListAdapter
import com.duoduovv.movie.bean.SubjectListBean
import com.duoduovv.movie.databinding.FragmentSubjectBinding
import com.duoduovv.movie.viewmodel.SubjectListViewModel
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.TITLE
import dc.android.bridge.view.BaseViewModelFragment

/**
 * @author: jun.liu
 * @date: 2021/6/25 10:25
 * @des:专题
 */
class SubjectFragment : BaseViewModelFragment<SubjectListViewModel>() {
    private lateinit var mbind: FragmentSubjectBinding
    override fun providerVMClass() = SubjectListViewModel::class.java
    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSubjectBinding.inflate(inflater, container, false)

    private var adapter: MovieSubjectListAdapter? = null

    override fun initView() {
        mbind = baseBinding as FragmentSubjectBinding
        adapter = MovieSubjectListAdapter()
        mbind.rvList.adapter = adapter
        adapter?.setOnItemClickListener { adapter, _, position ->
            val dataBean = (adapter as MovieSubjectListAdapter).data[position]
            ARouter.getInstance().build(RouterPath.PATH_SUBJECT_DETAIL)
                .withString(TITLE, dataBean.title).withString(ID, dataBean.subjectId).navigation()
        }
        viewModel.getSubjectList().observe(this, { setList(viewModel.getSubjectList().value) })
    }

    private fun setList(list: List<SubjectListBean>?) {
        adapter?.setList(list)
    }

    override fun initData() {
        viewModel.subjectList()
    }
}