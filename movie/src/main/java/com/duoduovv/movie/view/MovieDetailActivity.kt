package com.duoduovv.movie.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.duoduovv.common.util.RouterPath
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.MovieDetailAdapter
import com.duoduovv.movie.bean.MovieDetail
import com.duoduovv.movie.bean.MovieDetailBean
import com.duoduovv.movie.component.MovieDetailDialogFragment
import com.duoduovv.movie.viewmodel.MovieDetailViewModel
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
class MovieDetailActivity : BaseViewModelActivity<MovieDetailViewModel>(),
    MovieDetailAdapter.OnViewClickListener {
    override fun getLayoutId() = R.layout.activity_movie_detail
    override fun providerVMClass() = MovieDetailViewModel::class.java
    private var movieId = ""
    private var num = ""
    private var detailAdapter: MovieDetailAdapter? = null

    override fun initView() {
        viewModel.getMovieDetail().observe(this, { setData(viewModel.getMovieDetail().value) })
        viewModel.getAddState().observe(this, { })
        viewModel.getDeleteState().observe(this, { })
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

    /**
     * 分享
     */
    override fun onShareClick() {
    }

    /**
     * 下载
     */
    override fun onDownLoadClick() {
    }

    /**
     * 收藏
     */
    override fun onCollectClick(isCollection: Int) {
        when (isCollection) {
            1 -> viewModel.deleteCollection(movieId)
            else -> viewModel.addCollection(movieId)
        }
    }

    override fun onDetailClick(bean: MovieDetail) {
        val screenHeight = OsUtils.getRealDisplayHeight(this)
        val topBarHeight = OsUtils.getStatusBarHeight(this)
        val videoHeight = videoPlayer.layoutParams.height
        val realHeight = screenHeight - topBarHeight - videoHeight
        val dialogFragment = MovieDetailDialogFragment(height = realHeight, bean = bean)
        dialogFragment.showNow(supportFragmentManager, "detail")
    }

}