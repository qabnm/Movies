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
import com.duoduovv.movie.viewmodel.MovieDetailForDebugViewModel
import dc.android.bridge.BridgeContext
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.OsUtils
import dc.android.bridge.util.StringUtils
import dc.android.bridge.view.BaseViewModelActivity
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_movie_detail_for_debug.*
import kotlinx.android.synthetic.main.activity_movie_detail_for_debug.rvList
import kotlinx.android.synthetic.main.activity_movie_detail_for_debug.tvDetail
import kotlinx.android.synthetic.main.activity_movie_detail_for_debug.tvName
import kotlinx.android.synthetic.main.activity_movie_detail_for_debug.tvType
import kotlinx.android.synthetic.main.item_movie_detail_top.*

/**
 * @author: jun.liu
 * @date: 2021/3/1 10:23
 * @des:审核版用的影视详情页
 */
@Route(path = RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG)
class MovieDetailActivityForDebug : BaseViewModelActivity<MovieDetailForDebugViewModel>() {
    override fun getLayoutId() = R.layout.activity_movie_detail_for_debug
    override fun providerVMClass() = MovieDetailForDebugViewModel::class.java
    override fun showStatusBarView() = false
    private var movieId = ""
    private var actorAdapter: MovieDetailForDebugActorAdapter? = null
    private var photoAdapter: MovieDetailForDebugStagePhoto? = null
    override fun setLayout(isStatusColorDark: Boolean, statusBarColor: Int) {
        super.setLayout(false, statusBarColor)
    }

    override fun initView() {
        val layoutParams = layoutTopBar.layoutParams as LinearLayout.LayoutParams
        layoutParams.topMargin = OsUtils.getStatusBarHeight(this)
        viewModel.getMovieDetail().observe(this, { setData(viewModel.getMovieDetail().value) })
        rvListActor.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        actorAdapter = MovieDetailForDebugActorAdapter()
        photoAdapter = MovieDetailForDebugStagePhoto()
        rvListActor.adapter = actorAdapter
        rvList.adapter = photoAdapter
    }

    private fun setData(bean: MovieDetailForDebugBean?) {
        bean?.let {
            GlideUtils.setMovieImg(this, it.movie.coverUrl, imgCover)
            tvName.text = it.movie.vodName
            tvType.text = "${it.movie.vodArea}/${it.movie.typeText}"
            tvYearUp.text = "上映时间：${it.movie.vodYear}（${it.movie.vodArea}上映）"
            if (StringUtils.isEmpty(it.movie.vodDirector)){
                tvDirector.visibility = View.GONE
            }else{
                tvDirector.visibility = View.VISIBLE
                tvDirector.text = "导演：${it.movie.vodDirector}"
            }
            tvLanguage.text = "语言：${it.movie.vodLang}"
            if (StringUtils.isEmpty(it.movie.vodActor)){
                tvActor.visibility = View.GONE
            }else{
                tvActor.visibility = View.VISIBLE
                tvActor.text = "主演：${it.movie.vodActor}"
            }
            tvDetail.text = it.movie.vodDetail
            actorAdapter?.setList(it.movieDetail.actorArray)
            photoAdapter?.setList(it.movieDetail.photoArray)
            if (it.movieDetail.actorArray?.isNotEmpty() == true){
                tvMainActor.visibility = View.VISIBLE
            }else{
                tvMainActor.visibility = View.GONE
            }
            if (it.movieDetail.photoArray?.isNotEmpty() == true){
                tvJuZhao.visibility = View.VISIBLE
            }else{
                tvJuZhao.visibility = View.GONE
            }
        }
    }

    override fun initData() {
        movieId = intent.getStringExtra(BridgeContext.ID) ?: ""
        viewModel.movieDetailForDebug(movieId)
    }
}