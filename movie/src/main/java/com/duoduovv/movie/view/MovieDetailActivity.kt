package com.duoduovv.movie.view

import android.content.res.Configuration
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.listener.VideoPlayCallback
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.SampleCoverVideo
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
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.TITLE
import dc.android.bridge.BridgeContext.Companion.URL
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.util.LoggerSnack
import dc.android.bridge.util.OsUtils
import dc.android.bridge.util.StringUtils
import dc.android.bridge.view.BaseViewModelActivity
import kotlinx.android.synthetic.main.activity_movie_detail.*

/**
 * @author: jun.liu
 * @date: 2021/1/13 16:27
 * @des:影片详情
 */
@Route(path = RouterPath.PATH_MOVIE_DETAIL)
class MovieDetailActivity : BaseViewModelActivity<MovieDetailViewModel>(),
    MovieDetailAdapter.OnViewClickListener, SampleCoverVideo.OnStartClickListener,
    MovieDetailSelectDialogFragment.OnSelectDialogItemClickListener {
    override fun getLayoutId() = R.layout.activity_movie_detail
    override fun providerVMClass() = MovieDetailViewModel::class.java
    private var movieId = ""
    private var vid = ""
    private var detailAdapter: MovieDetailAdapter? = null
    private var detailBean: MovieDetailBean? = null
    private var orientationUtils: OrientationUtils? = null
    private var way = 2
    private var playUrl = ""
    private var title = ""
    override fun setLayout(isStatusColorDark: Boolean, statusBarColor: Int) {
        super.setLayout(false, resources.getColor(R.color.color000000))
    }

    override fun initView() {
        rvList.layoutManager = GridLayoutManager(this, 3)
        viewModel.getMovieDetail().observe(this, { setData(viewModel.getMovieDetail().value) })
        viewModel.getMoviePlayInfo()
            .observe(this, { setPlayInfo(viewModel.getMoviePlayInfo().value) })
        viewModel.getMovieClickInfo()
            .observe(this, { setClickInfo(viewModel.getMovieClickInfo().value) })
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
        videoPlayer.setVideoAllCallBack(object : VideoPlayCallback() {
            override fun onPlayError(url: String?, vararg objects: Any?) {
                super.onPlayError(url, *objects)
                AndroidUtils.toast("播放出错！",this@MovieDetailActivity)
            }
        })
        videoPlayer.fullscreenButton.setOnClickListener {
            orientationUtils?.resolveByClick()
            videoPlayer.startWindowFullscreen(this, true, true)
        }
    }

    /**
     * 点击播放的时候  需要直接播放视频信息
     * @param bean MoviePlayInfoBean?
     */
    private fun setClickInfo(bean: MoviePlayInfoBean?) {
        bean?.let {
            val playList = it.playUrls
            if (playList?.isNotEmpty() == true) {
//                val url = "http://down2.okdown10.com/20210105/2642_e5ede2d1/25岁当代单身女性尝试相亲APP的成果日记.EP03.mp4"
                videoPlayer.setStartClick(1)
                videoPlayer.setUp(playList[0].url, true, "")
                videoPlayer.startPlayLogic()
            }
        }
    }

    /**
     * 视频播放信息  第一次进来的时候，只加载视频信息 但是不播放
     * 只有正常版本的才会走到这里来
     * @param bean MoviePlayInfoBean
     */
    private fun setPlayInfo(bean: MoviePlayInfoBean?) {
        bean?.let {
            val playList = it.playUrls
            Log.d("videoPlayer", "****这里执行了：way=$way")
            if (playList?.isNotEmpty() == true) {
//                val url = "http://down2.okdown10.com/20210105/2642_e5ede2d1/25岁当代单身女性尝试相亲APP的成果日记.EP03.mp4"
                videoPlayer.setStartClick(1)
                videoPlayer.setUp(playList[0].url, true, "")
            }
        }
    }

    override fun initData() {
        vid = intent.getStringExtra(BridgeContext.TYPE_ID) ?: ""
        movieId = intent.getStringExtra(BridgeContext.ID) ?: ""
        viewModel.movieDetail(id = movieId)
    }

    private fun setData(detailBean: MovieDetailBean?) {
        this.detailBean = detailBean
        if (detailBean == null) return
        movieId = detailBean.movie.str_id
        way = detailBean.way
        title = detailBean.movie.vod_name
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
        if (list.isNotEmpty()) {
            if (!hasClickRecommend){
                if (StringUtils.isEmpty(vid)) {
                    detailBean.movieItems[0].isSelect = true
                } else {
                    for (i in list.indices) {
                        if (vid == list[i].vid) detailBean.movieItems[i].isSelect = true
                    }
                }
            }else{
                detailBean.movieItems[0].isSelect = true
            }
            detailAdapter?.notifyItemChanged(0)
            if (way == BridgeContext.WAY_RELEASE) {
                //如果是正常版本 就请求播放信息 如果没有剧集信息 就默认播放第一集
                    if (!hasClickRecommend) {
                        if (StringUtils.isEmpty(vid)) vid = list[0].vid
                    }else{
                        vid = list[0].vid
                    }
                viewModel.moviePlayInfo(vid, movieId)
            } else if (way == BridgeContext.WAY_H5) {
                //如果是H5版本
                videoPlayer.setStartClick(0)
                val urlList = detailBean.playUrls
                if (urlList?.isNotEmpty() == true) playUrl = urlList[0].url
            }
        }
    }

    private fun setVideoPlayer() {
        videoPlayer.apply {
            thumbImageViewLayout.visibility = View.VISIBLE
            //设置全屏按键功能
            fullscreenButton.setOnClickListener {
                this.startWindowFullscreen(context, true, true)
            }
            //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
            isAutoFullWithSize = false
            //音频焦点冲突时是否释放
            isReleaseWhenLossAudio = false
            //全屏动画
            isShowFullAnimation = true
            //非wifi环境下，显示流量提醒
            isNeedShowWifiTip = true
            isShowDragProgressTextOnSeekBar = true //拖动进度条时，是否在 seekbar 开始部位显示拖动进度
            backButton.setOnClickListener { onBackPressed() }
            setIsTouchWiget(true)
            setStartClickListener(this@MovieDetailActivity)
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
    override fun onCollectClick(isCollection: Int, movieId: String) {
        when (isCollection) {
            1 -> viewModel.deleteCollection(movieId)
            else -> viewModel.addCollection(movieId)
        }
    }

    /**
     * 点击详情 显示详情 介绍弹窗
     * @param bean MovieDetail
     */
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
        for (i in dataList.indices) {
            if (vid == dataList[i].vid) dataList[i].isSelect = true
        }
        val dialogFragment = MovieDetailSelectDialogFragment(height = realHeight, dataList, this)
        dialogFragment.showNow(supportFragmentManager, "select")
    }

    /**
     * 选集播放
     * @param vid String
     * @param movieId String
     */
    override fun onSelectClick(vid: String, movieId: String) {
        this.vid = vid
        if (way == BridgeContext.WAY_RELEASE) {
            //只有正常班的才会去请求接口
            viewModel.moviePlayInfo(vid, movieId, 1)
        }
    }

    private var hasClickRecommend = false
    /**
     * 点击了推荐的视频
     * @param movieId String
     */
    override fun onMovieClick(movieId: String) {
        //清理掉正在播放的视频
        GSYVideoManager.releaseAllVideos()
        hasClickRecommend = true
        viewModel.movieDetail(movieId)
    }

    override fun onBackPressed() {
        //先返回正常状态
        orientationUtils?.backToProtVideo()
        if (GSYVideoManager.backFromWindowFull(this)) return
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
        //获取视频播放信息
        GSYVideoManager.releaseAllVideos()
        orientationUtils?.releaseListener()
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                //横屏
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                setStatusBarVisible(View.GONE)
            }
            else -> {
                //竖屏
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                setStatusBarVisible(View.VISIBLE)
            }
        }
    }

    /**
     * 跳转H5页面
     */
    override fun onStartClick() {
        ARouter.getInstance().build(RouterPath.PATH_WEB_VIEW).withString(URL, playUrl)
            .withString(TITLE, title).navigation()
    }

    /**
     * 选集弹窗的点击事件
     * @param vid String
     */
    override fun onDialogClick(vid: String) {
        onSelectClick(vid, movieId)
    }
}