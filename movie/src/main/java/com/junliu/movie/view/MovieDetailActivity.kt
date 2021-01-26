package com.junliu.movie.view

import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.common.util.RouterPath
import com.junliu.movie.R
import com.junliu.movie.bean.MovieDetailBean
import com.junliu.movie.viewmodel.MovieDetailViewModel
import dc.android.bridge.BridgeContext
import dc.android.bridge.view.BaseViewModelActivity
import dc.android.bridge.view.BridgeActivity

/**
 * @author: jun.liu
 * @date: 2021/1/13 16:27
 * @des:影片详情
 */
@Route(path = RouterPath.PATH_MOVIE_DETAIL)
class MovieDetailActivity : BaseViewModelActivity<MovieDetailViewModel>() {
    override fun getLayoutId() = R.layout.activity_movie_detail
    override fun providerVMClass() = MovieDetailViewModel::class.java
    private var movieId = ""
    private var num = ""

    override fun initView() {
        viewModel.getMovieDetail()
            .observe(this, Observer { setData(viewModel.getMovieDetail().value) })
    }

    override fun initData() {
        movieId = intent.getStringExtra(BridgeContext.ID) ?: ""
        num = intent.getStringExtra(BridgeContext.NUM) ?: ""
        viewModel.movieDetail(id = movieId, num = num)
    }

    private fun setData(detailBean: MovieDetailBean?) {

    }
}