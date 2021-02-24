package com.junliu.movie.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.common.util.RouterPath
import com.junliu.movie.R
import com.junliu.movie.adapter.MovieDetailAdapter
import com.junliu.movie.bean.MovieDetail
import com.junliu.movie.bean.MovieDetailBean
import com.junliu.movie.component.MovieDetailDialogFragment
import com.junliu.movie.viewmodel.MovieDetailViewModel
import dc.android.bridge.BridgeContext
import dc.android.bridge.util.OsUtils
import dc.android.bridge.view.BaseViewModelActivity
import kotlinx.android.synthetic.main.activity_movie_detail.*

/**
 * @author: jun.liu
 * @date: 2021/1/13 16:27
 * @des:影片详情
 */
@Route(path = RouterPath.PATH_MOVIE_DETAIL)
class MovieDetailActivity : BaseViewModelActivity<MovieDetailViewModel>(),MovieDetailAdapter.OnViewClickListener {
    override fun getLayoutId() = R.layout.activity_movie_detail
    override fun providerVMClass() = MovieDetailViewModel::class.java
    private var movieId = ""
    private var num = ""
    private var detailAdapter:MovieDetailAdapter?= null

    override fun initView() {
        viewModel.getMovieDetail().observe(this, { setData(viewModel.getMovieDetail().value) })
    }

    override fun initData() {
        movieId = intent.getStringExtra(BridgeContext.ID) ?: ""
        num = intent.getStringExtra(BridgeContext.NUM) ?: ""
        viewModel.movieDetail(id = movieId, num = num)
    }

    private fun setData(detailBean: MovieDetailBean?) {
        if (detailBean == null) return
        detailAdapter = MovieDetailAdapter(this, detailBean = detailBean)
        detailAdapter?.setOnViewClick(this)
        rvList.adapter = detailAdapter
    }

    override fun onShareClick() {
    }

    override fun onDownLoadClick() {
    }

    override fun onCollectClick() {
    }

    override fun onDetailClick(bean: MovieDetail) {
        val screenHeight = OsUtils.getRealDisplayHeight(this)
        val topBarHeight = OsUtils.getStatusBarHeight(this)
        val videoHeight = videoPlayer.layoutParams.height
        val realHeight = screenHeight - topBarHeight - videoHeight
        val dialogFragment = MovieDetailDialogFragment(height = realHeight, bean = bean)
        dialogFragment.showNow(supportFragmentManager,"detail")
    }
}