package com.duoduovv.movie.view

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.duoduovv.common.util.RouterPath
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.MovieDetailForDebugActorAdapter
import com.duoduovv.movie.adapter.MovieDetailForDebugStagePhoto
import com.duoduovv.movie.bean.MovieDetailForDebugBean
import com.duoduovv.movie.databinding.ActivityMovieDetailForDebugBinding
import com.duoduovv.movie.viewmodel.MovieDetailForDebugViewModel
import dc.android.bridge.BridgeContext
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.OsUtils
import dc.android.bridge.util.StringUtils
import dc.android.bridge.view.BaseViewModelActivity

/**
 * @author: jun.liu
 * @date: 2021/3/1 10:23
 * @des:审核版用的影视详情页
 */
@Route(path = RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG)
class MovieDetailActivityForDebug : BaseViewModelActivity<MovieDetailForDebugViewModel>() {
    override fun getLayoutId() = R.layout.activity_movie_detail_for_debug
    private lateinit var mBind:ActivityMovieDetailForDebugBinding
    override fun providerVMClass() = MovieDetailForDebugViewModel::class.java
    override fun showStatusBarView() = false
    private var movieId = ""
    private var actorAdapter: MovieDetailForDebugActorAdapter? = null
    private var photoAdapter: MovieDetailForDebugStagePhoto? = null
    override fun setLayout(isStatusColorDark: Boolean, statusBarColor: Int) {
        super.setLayout(false, statusBarColor)
    }

    override fun initView() {
        mBind = ActivityMovieDetailForDebugBinding.bind(layoutView)
        val layoutParams = mBind.layoutTopBar.layoutParams as LinearLayout.LayoutParams
        layoutParams.topMargin = OsUtils.getStatusBarHeight(this)
        viewModel.getMovieDetail().observe(this, { setData(viewModel.getMovieDetail().value) })
        mBind.rvListActor.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBind.rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        actorAdapter = MovieDetailForDebugActorAdapter()
        photoAdapter = MovieDetailForDebugStagePhoto()
        mBind.rvListActor.adapter = actorAdapter
        mBind.rvList.adapter = photoAdapter
    }

    private fun setData(bean: MovieDetailForDebugBean?) {
        bean?.let {
            GlideUtils.setMovieImg(this, it.movie.coverUrl, mBind.imgCover)
            mBind.tvName.text = it.movie.vodName
            mBind.tvType.text = "${it.movie.vodArea}/${it.movie.typeText}"
            mBind.tvYearUp.text = "上映时间：${it.movie.vodYear}（${it.movie.vodArea}上映）"
            if (StringUtils.isEmpty(it.movie.vodDirector)){
                mBind.tvDirector.visibility = View.GONE
            }else{
                mBind.tvDirector.visibility = View.VISIBLE
                mBind.tvDirector.text = "导演：${it.movie.vodDirector}"
            }
            mBind.tvLanguage.text = "语言：${it.movie.vodLang}"
            if (StringUtils.isEmpty(it.movie.vodActor)){
                mBind.tvActor.visibility = View.GONE
            }else{
                mBind.tvActor.visibility = View.VISIBLE
                mBind.tvActor.text = "主演：${it.movie.vodActor}"
            }
            mBind.tvDetail.text = it.movie.vodDetail
            actorAdapter?.setList(it.movieDetail.actorArray)
            photoAdapter?.setList(it.movieDetail.photoArray)
            if (it.movieDetail.actorArray?.isNotEmpty() == true){
                mBind.tvMainActor.visibility = View.VISIBLE
            }else{
                mBind.tvMainActor.visibility = View.GONE
            }
            if (it.movieDetail.photoArray?.isNotEmpty() == true){
                mBind.tvJuZhao.visibility = View.VISIBLE
            }else{
                mBind.tvJuZhao.visibility = View.GONE
            }
        }
    }

    override fun initData() {
        movieId = intent.getStringExtra(BridgeContext.ID) ?: ""
        viewModel.movieDetailForDebug(movieId)
    }
}