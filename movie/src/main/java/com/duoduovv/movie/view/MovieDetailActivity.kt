package com.duoduovv.movie.view

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.duoduovv.common.listener.VideoPlayCallback
import com.duoduovv.common.util.RouterPath
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.MovieDetailAdapter
import com.duoduovv.movie.bean.MovieDetail
import com.duoduovv.movie.bean.MovieDetailBean
import com.duoduovv.movie.bean.MovieItem
import com.duoduovv.movie.bean.MoviePlayInfoBean
import com.duoduovv.movie.component.MovieDetailDialogFragment
import com.duoduovv.movie.component.MovieDetailSelectDialogFragment
import com.duoduovv.movie.viewmodel.MovieDetailViewModel
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
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
    private var vid = ""
    private var detailAdapter: MovieDetailAdapter? = null
    private var detailBean: MovieDetailBean? = null
    private var orientationUtils :OrientationUtils? = null
    override fun setLayout(isStatusColorDark: Boolean, statusBarColor: Int) {
        super.setLayout(false, resources.getColor(R.color.color000000))
    }

    override fun initView() {
        rvList.layoutManager = GridLayoutManager(this, 3)
        viewModel.getMovieDetail().observe(this, { setData(viewModel.getMovieDetail().value) })
        viewModel.getMoviePlayInfo()
            .observe(this, { setPlayInfo(viewModel.getMoviePlayInfo().value) })
        viewModel.getAddState().observe(this, {
            detailBean?.let {
                it.isFavorite = 1
                detailAdapter?.notifyDataChange(it)
            }
        })
        viewModel.getDeleteState().observe(this, {
            detailBean?.let {
                it.isFavorite = 0
                detailAdapter?.notifyDataChange(it)
            }
        })
        orientationUtils = OrientationUtils(this, videoPlayer)
        setVideoPlayer()
        videoPlayer.setVideoAllCallBack(object :VideoPlayCallback(){})
        videoPlayer.fullscreenButton.setOnClickListener { orientationUtils?.resolveByClick() }
    }

    /**
     * 视频播放信息
     * @param bean MoviePlayInfoBean
     */
    private fun setPlayInfo(bean: MoviePlayInfoBean?) {
        bean?.let {
            val playList = it.playUrls
            if (playList?.isNotEmpty() == true) {
                val url = "http://down2.okdown10.com/20210105/2642_e5ede2d1/25岁当代单身女性尝试相亲APP的成果日记.EP03.mp4"
                videoPlayer.setUp(url, true, "")
            }
        }
    }

    override fun initData() {
        movieId = intent.getStringExtra(BridgeContext.ID) ?: ""
        viewModel.movieDetail(id = movieId)
    }

    private fun setData(detailBean: MovieDetailBean?) {
        this.detailBean = detailBean
        if (detailBean == null) return
        //视频信息
        videoPlayer.loadCoverImage(detailBean.movie.cover_url, R.drawable.back_white)
        if (null == detailAdapter) {
            detailAdapter = MovieDetailAdapter(this, detailBean = detailBean)
            detailAdapter?.setOnViewClick(this)
            rvList.adapter = detailAdapter
        } else {
            detailAdapter?.notifyDataChange(detailBean)
        }
        val list = detailBean.movieItems
        //默认播放第一集
        if (list.isNotEmpty()) vid = list[0].vid

        viewModel.moviePlayInfo(vid, movieId)
    }

    private fun setVideoPlayer() {
        videoPlayer.apply {
            thumbImageViewLayout.visibility = View.VISIBLE
            //设置全屏按键功能
            fullscreenButton.setOnClickListener {
                this.startWindowFullscreen(context, false, false)
            }
            //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
            isAutoFullWithSize = true
            //音频焦点冲突时是否释放
            isReleaseWhenLossAudio = false
            //全屏动画
            isShowFullAnimation = true
            //非wifi环境下，显示流量提醒
            isNeedShowWifiTip = true
            isShowDragProgressTextOnSeekBar = true //拖动进度条时，是否在 seekbar 开始部位显示拖动进度
            backButton.setOnClickListener { onBackPressed() }
        }
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
    override fun onCollectClick(isCollection: Int,movieId: String) {
        when (isCollection) {
            1 -> viewModel.deleteCollection(movieId)
            else -> viewModel.addCollection(movieId)
        }
    }

    override fun onDetailClick(bean: MovieDetail) {
        val screenHeight = OsUtils.getRealDisplayHeight(this)
        val topBarHeight = OsUtils.getStatusBarHeight(this)
        val videoHeight = videoPlayer.measuredHeight
        val realHeight = screenHeight - topBarHeight - videoHeight
        Log.d(
            "height",
            "screenHeight:${screenHeight}**topBarHeight:${topBarHeight}**videoHeight${videoHeight}"
        )
        val dialogFragment = MovieDetailDialogFragment(height = realHeight, bean = bean)
        dialogFragment.showNow(supportFragmentManager, "detail")
    }

    /**
     * 选集
     * @param dataList List<String>
     */
    override fun onSelectClick(dataList: List<MovieItem>) {
        val screenHeight = OsUtils.getRealDisplayHeight(this)
        val topBarHeight = OsUtils.getStatusBarHeight(this)
        val videoHeight = videoPlayer.measuredHeight
        val realHeight = screenHeight - topBarHeight - videoHeight
        Log.d(
            "height",
            "screenHeight:${screenHeight}**topBarHeight:${topBarHeight}**videoHeight${videoHeight}"
        )
        val dialogFragment = MovieDetailSelectDialogFragment(height = realHeight, dataList)
        dialogFragment.showNow(supportFragmentManager, "select")
    }

    override fun onMovieClick(movieId: String) {
        viewModel.movieDetail(movieId)
    }

    override fun onBackPressed() {
        //先返回正常状态
        if (orientationUtils?.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.fullscreenButton.performClick()
            return
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null)
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
        orientationUtils?.releaseListener()
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when(newConfig.orientation){
            Configuration.ORIENTATION_LANDSCAPE ->{
                //横屏
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                setStatusBarVisible(View.GONE)
            }
            else ->{
                //竖屏
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                setStatusBarVisible(View.VISIBLE)
            }
        }
    }

}