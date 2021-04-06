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
import dc.android.bridge.view.BaseViewModelActivity
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_movie_detail_for_debug.*

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
            GlideUtils.setMovieImg(this, it.movie.cover_url, imgCover)
            tvName.text = it.movie.vod_name
            tvType.text = "${it.movie.vod_area_text}/${it.movie.type_id_text}"
            tvYearUp.text = "上映时间：${it.movie.vod_year}（${it.movie.vod_area_text}上映）"
            tvDuration.text = "片长：123分钟"
            tvLanguage.text = "语言：${it.movie.vod_lang}"
            tvDetail.text = it.movie.vod_blurb
            actorAdapter?.setList(it.movieDetail.actor_array)
            photoAdapter?.setList(it.movieDetail.stage_photo_array)
            if (it.movieDetail.actor_array?.isNotEmpty() == true){
                tvMainActor.visibility = View.VISIBLE
            }else{
                tvMainActor.visibility = View.GONE
            }
            if (it.movieDetail.stage_photo_array?.isNotEmpty() == true){
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