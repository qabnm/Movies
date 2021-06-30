package com.duoduovv.movie.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.util.RouterPath
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.MovieSubjectDetailAdapter
import com.duoduovv.movie.bean.SubjectDetailListBean
import com.duoduovv.movie.databinding.ActivitySubjectDetailBinding
import com.duoduovv.movie.viewmodel.SubjectDetailViewModel
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.TITLE
import dc.android.bridge.BridgeContext.Companion.WAY_VERIFY
import dc.android.bridge.view.BaseViewModelActivity

/**
 * @author: jun.liu
 * @date: 2021/6/25 13:32
 * @des:专题详情页
 */
@Route(path = RouterPath.PATH_SUBJECT_DETAIL)
class SubjectDetailActivity : BaseViewModelActivity<SubjectDetailViewModel>() {
    override fun getLayoutId() = R.layout.activity_subject_detail
    override fun providerVMClass() = SubjectDetailViewModel::class.java
    private lateinit var mBind: ActivitySubjectDetailBinding
    private var detailAdapter: MovieSubjectDetailAdapter? = null

    override fun initView() {
        mBind = ActivitySubjectDetailBinding.bind(layoutView)
        detailAdapter = MovieSubjectDetailAdapter()
        mBind.rvList.adapter = detailAdapter
        detailAdapter?.setOnItemClickListener { adapter, _, position ->
            val bean = (adapter as MovieSubjectDetailAdapter).data[position]
            onMovieClick(bean.strId, bean.way)
        }
        viewModel.getSubjectDetail().observe(this, { setList(viewModel.getSubjectDetail().value) })
    }

    private fun setList(list: List<SubjectDetailListBean>?) {
        detailAdapter?.setList(list)
    }

    override fun initData() {
        val subjectId = intent.getStringExtra(ID) ?: ""
        val title = intent.getStringExtra(TITLE)?:"专题"
        mBind.layoutTopBar.setTopTitle(title)
        viewModel.subjectDetail(subjectId)
    }

    /**
     * 跳转影视详情
     * @param movieId String
     */
    private fun onMovieClick(movieId: String, way: String) {
        val path = if (way == WAY_VERIFY) {
            RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG
        } else {
            RouterPath.PATH_MOVIE_DETAIL
        }
        ARouter.getInstance().build(path).withString(ID, movieId).navigation()
    }
}