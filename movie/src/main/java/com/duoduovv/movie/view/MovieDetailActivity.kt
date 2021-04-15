package com.duoduovv.movie.view

import android.content.res.Configuration
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.listener.VideoPlayCallback
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.SampleCoverVideo
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.MovieDetailAdapter
import com.duoduovv.movie.bean.*
import com.duoduovv.movie.component.MovieDetailArtSelectDialog
import com.duoduovv.movie.component.MovieDetailDialogFragment
import com.duoduovv.movie.component.MovieDetailSelectDialogFragment
import com.duoduovv.movie.viewmodel.MovieDetailViewModel
import com.duoduovv.room.domain.CollectionBean
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.TITLE
import dc.android.bridge.BridgeContext.Companion.TYPE_ID
import dc.android.bridge.BridgeContext.Companion.URL
import dc.android.bridge.BridgeContext.Companion.WAY_RELEASE
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.util.OsUtils
import dc.android.bridge.util.StringUtils
import dc.android.bridge.view.BaseViewModelActivity
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager

/**
 * @author: jun.liu
 * @date: 2021/1/13 16:27
 * @des:影片详情
 */
@Route(path = RouterPath.PATH_MOVIE_DETAIL)
class MovieDetailActivity : BaseViewModelActivity<MovieDetailViewModel>(),
    MovieDetailAdapter.OnViewClickListener, SampleCoverVideo.OnStartClickListener,
    MovieDetailSelectDialogFragment.OnSelectDialogItemClickListener,
    MovieDetailArtSelectDialog.OnSelectDialogItemClickListener {
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
    private var currentPlayPosition = 0  //默认是从第一集开始播放
    private var vidTitle = ""
    private var currentLength: Long = 0
    private var screenHeight = 0
    private var topBarHeight = 0
    private var videoHeight = 0
    private var navHeight = 0
    private var realHeight = 0
    private var vidByQuery = ""
    override fun setLayout(isStatusColorDark: Boolean, statusBarColor: Int) {
        super.setLayout(false, ContextCompat.getColor(this, R.color.color000000))
    }

    override fun initView() {
        rvList.layoutManager = GridLayoutManager(this, 3)
        viewModel.getMovieDetail().observe(this, { setData(viewModel.getMovieDetail().value) })
        viewModel.getMoviePlayInfo()
            .observe(this, { setPlayInfo(viewModel.getMoviePlayInfo().value) })
        viewModel.getMovieClickInfo()
            .observe(this, { setClickInfo(viewModel.getMovieClickInfo().value) })
        orientationUtils = OrientationUtils(this, videoPlayer)
        orientationUtils?.isEnable = false
        setVideoPlayer()
        videoPlayer.setVideoAllCallBack(videoCallback)
        videoPlayer.fullscreenButton.setOnClickListener {
            orientationUtils?.resolveByClick()
            videoPlayer.startWindowFullscreen(this, true, true)
        }
        screenHeight = OsUtils.getRealDisplayHeight(this)
        topBarHeight = OsUtils.getStatusBarHeight(this)
        navHeight = OsUtils.getNavigationBarHeight(this)
    }

    /**
     * 播放器相关状态和时间监听毁掉
     */
    private val videoCallback = object : VideoPlayCallback() {
        override fun onPlayError(url: String?, vararg objects: Any?) {
            super.onPlayError(url, *objects)
            AndroidUtils.toast("播放出错！", this@MovieDetailActivity)
        }

        override fun onPrepared(url: String?, vararg objects: Any?) {
            super.onPrepared(url, *objects)
            orientationUtils?.isEnable = videoPlayer.isRotateWithSystem
            if (currentLength > 0 && vidByQuery == vid) {
                videoPlayer.seekTo(currentLength)
            }
            currentLength = 0
        }

        override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
            super.onQuitFullscreen(url, *objects)
            orientationUtils?.backToProtVideo()
        }

        override fun onAutoComplete(url: String?, vararg objects: Any?) {
            super.onAutoComplete(url, *objects)
            //播放完成了
            if (way == WAY_RELEASE) {
                //正常播放的模式 如果有下一集 直接播放下一集
                detailBean?.let {
                    val movieItems = it.movieItems
                    for (i in movieItems.indices) {
                        if (vid == movieItems[i].vid) {
                            currentPlayPosition = i
                        }
                    }
                    if (currentPlayPosition < movieItems.size - 1) {
                        //还有下一集 播放下一集
                        currentPlayPosition++
                        vid = movieItems[currentPlayPosition].vid
                        vidTitle = movieItems[currentPlayPosition].title
                        viewModel.moviePlayInfo(vid, movieId, 1)
                        //更新选集显示
                        for (i in movieItems.indices) {
                            movieItems[i].isSelect = false
                        }
                        movieItems[currentPlayPosition].isSelect = true
                        detailAdapter?.notifyItemChanged(0)
                    }
                }
            }
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
                (videoPlayer.currentPlayer as SampleCoverVideo).apply {
                    setStartClick(1)
                    setUp(playList[0].url, true, "")
                    startPlayLogic()
                }
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
                videoPlayer.setStartClick(1)
                videoPlayer.setUp(playList[0].url, true, "")
                //如果是可播放的直接播放
//                if (way == WAY_RELEASE) videoPlayer.startPlayLogic()
            }
        }
    }

    override fun initData() {
        showLoading()
        vid = intent.getStringExtra(TYPE_ID) ?: ""
        movieId = intent.getStringExtra(ID) ?: ""
        viewModel.movieDetail(id = movieId)
    }

    private fun setData(detailBean: MovieDetailBean?) {
        //查询视频详情
        dismissLoading()
        this.detailBean = detailBean
        if (detailBean == null) return
        movieId = detailBean.movie.str_id
        way = detailBean.way
        title = detailBean.movie.vod_name
        queryMovieById(movieId)
    }

    /**
     * 通过id查询影片
     * @param movieId String
     * @return String
     */
    private fun queryMovieById(movieId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            if (way == WAY_RELEASE) {
                val bean = viewModel.queryMovieById(movieId)
                bean?.let {
                    if (it.movieId == movieId) {
                        vid = it.vid
                        vidByQuery = it.vid
                        currentLength = it.currentLength.toLong()
                    }
                }
            }
            //视频信息
            videoPlayer.loadCoverImage(detailBean!!.movie.cover_url, R.drawable.back_white)
            if (null == detailAdapter) {
                detailAdapter =
                    MovieDetailAdapter(this@MovieDetailActivity, detailBean = detailBean!!)
                detailAdapter?.setOnViewClick(this@MovieDetailActivity)
                rvList.adapter = detailAdapter
            } else {
                detailAdapter?.notifyDataChange(detailBean!!)
            }
            val list = detailBean!!.movieItems
            //默认播放第一集
            if (list.isNotEmpty()) {
                if (!hasClickRecommend) {
                    if (StringUtils.isEmpty(vid)) {
                        detailBean!!.movieItems[0].isSelect = true
                        vidTitle = detailBean!!.movieItems[0].title
                    } else {
                        for (i in list.indices) {
                            if (vid == list[i].vid) {
                                detailBean!!.movieItems[i].isSelect = true
                                vidTitle = detailBean!!.movieItems[i].title
                            }
                        }
                    }
                } else {
                    detailBean!!.movieItems[0].isSelect = true
                    vidTitle = detailBean!!.movieItems[0].title
                }
                detailAdapter?.notifyItemChanged(0)
                if (way == WAY_RELEASE) {
                    //如果是正常版本 就请求播放信息 如果没有剧集信息 就默认播放第一集
                    if (!hasClickRecommend) {
                        if (StringUtils.isEmpty(vid)) {
                            vid = list[0].vid
                        }
                    } else {
                        vid = list[0].vid
                    }
                    viewModel.moviePlayInfo(vid, movieId)
                } else if (way == BridgeContext.WAY_H5) {
                    //如果是H5版本
                    videoPlayer.setStartClick(0)
                    val urlList = detailBean!!.playUrls
                    if (urlList?.isNotEmpty() == true) playUrl = urlList[0].url
                }
            }
            //更新收藏状态
            val collectionBean = viewModel.queryCollectionById(detailBean!!.movie.id)
            detailAdapter?.notifyCollectionChange(collectionBean)
        }
    }

    /**
     * 设置播放器的基本功能
     */
    private fun setVideoPlayer() {
        //EXOPlayer内核，支持格式更多
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
        //exo缓存模式，支持m3u8，只支持exo
        CacheFactory.setCacheManager(ExoPlayerCacheManager::class.java)
        videoPlayer.apply {
            thumbImageViewLayout.visibility = View.VISIBLE
            //设置全屏按键功能
            fullscreenButton.setOnClickListener {
                startWindowFullscreen(context, true, true)
            }
            //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
            isAutoFullWithSize = false
            //音频焦点冲突时是否释放
            isReleaseWhenLossAudio = true
            //全屏动画
            isShowFullAnimation = false
            //非wifi环境下，显示流量提醒
            isNeedShowWifiTip = true
            isShowDragProgressTextOnSeekBar = true //拖动进度条时，是否在 seekbar 开始部位显示拖动进度
            backButton.setOnClickListener { onBackPressed() }
            setIsTouchWiget(true)
            setStartClickListener(this@MovieDetailActivity)
            isNeedLockFull = true
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
    override fun onCollectClick(collectionBean: CollectionBean?) {
        collectionBean?.let {
            GlobalScope.launch(Dispatchers.Main) {
                if (it.isCollect) {
                    viewModel.deleteCollection(it)
                } else {
                    viewModel.addCollection(it)
                }
                val bean = viewModel.queryCollectionById(detailBean!!.movie.id)
                detailAdapter?.notifyCollectionChange(bean)
            }
        } ?: also {
            GlobalScope.launch(Dispatchers.Main) {
                val detailBean = detailBean!!.movie
                val bean = CollectionBean(
                    coverUrl = detailBean.cover_url,
                    strId = detailBean.str_id,
                    movieId = detailBean.id,
                    lastRemark = detailBean.last_remark,
                    actor = detailBean.vod_actor,
                    direcotor = detailBean.vod_director,
                    movieName = detailBean.vod_name,
                    lang = detailBean.vod_lang,
                    isCollect = true,
                    collectionTime = System.currentTimeMillis()
                )
                viewModel.addCollection(bean)
                val beans = viewModel.queryCollectionById(detailBean.id)
                detailAdapter?.notifyCollectionChange(beans)
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        videoHeight = videoPlayer.measuredHeight
        realHeight = screenHeight - topBarHeight - videoHeight - navHeight
    }

    /**
     * 点击详情 显示详情 介绍弹窗
     * @param bean MovieDetail
     */
    override fun onDetailClick(bean: MovieDetail) {
        if (OsUtils.isFastClick()) return
        Log.d(
            "height",
            "screenHeight:${screenHeight}**topBarHeight:${topBarHeight}**videoHeight${videoHeight}**navHeight${navHeight}"
        )
        val dialogFragment =
            MovieDetailDialogFragment(height = realHeight, bean = bean, reportListener)
        dialogFragment.showNow(supportFragmentManager, "detail")
    }

    /**
     * 举报按钮点击监听
     */
    private val reportListener = object : MovieDetailDialogFragment.OnReportClickListener {
        override fun onReportClick(movieId: String) {
            ARouter.getInstance().build(RouterPath.PATH_REPORT).withString(ID, movieId).navigation()
        }
    }

    /**
     * 综艺类型的选集更多展开
     * @param dataList List<MovieItem>
     */
    override fun onArtSelectClick(dataList: List<MovieItem>) {
        for (i in dataList.indices) {
            if (vid == dataList[i].vid) dataList[i].isSelect = true
        }
        val dialogFragment = MovieDetailArtSelectDialog(height = realHeight, dataList, this)
        dialogFragment.showNow(supportFragmentManager, "select")
    }

    /**
     * 电视剧类型的选集更多展开
     * @param dataList List<String>
     */
    override fun onSelectClick(dataList: List<MovieItem>) {
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
    override fun onSelectClick(vid: String, movieId: String, vidTitle: String) {
        this.vid = vid
        this.vidTitle = vidTitle
        if (way == WAY_RELEASE) {
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
        GlobalScope.launch(Dispatchers.Main) {
            updateHistoryDB()
//            GSYVideoManager.releaseAllVideos()
            hasClickRecommend = true
            viewModel.movieDetail(movieId)
        }
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
        videoPlayer.currentPlayer.onVideoResume(false)
        super.onResume()
    }

    override fun onStop() {
        updateHistoryDB()
        super.onStop()
    }

    /**
     * 更新数据库数据
     */
    private fun updateHistoryDB() {
        detailBean?.let {
            viewModel.updateHistoryDB(
                progress = videoPlayer.currentPlayer.currentPositionWhenPlaying,
                detailBean = it,
                movieId = movieId,
                vid = vid,
                vidTitle = vidTitle,
                duration = videoPlayer.currentPlayer.duration
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //获取视频播放信息
        GSYVideoManager.releaseAllVideos()
        orientationUtils?.releaseListener()
    }

    override fun onPause() {
        videoPlayer.currentPlayer.onVideoPause()
        super.onPause()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        (videoPlayer.currentPlayer as SampleCoverVideo).setStartClick(if (way == WAY_RELEASE) 1 else 0)
        Log.d("movieDetail", "当前播放路径是：$way")
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
    override fun onDialogClick(vid: String, vidTitle: String) {
        //更新显示
        detailBean?.let {
            var pos = 0
            for (i in it.movieItems.indices) {
                it.movieItems[i].isSelect = false
                if (vid == it.movieItems[i].vid) pos = i
            }
            it.movieItems[pos].isSelect = true
            detailAdapter?.notifyItemChanged(0)
            onSelectClick(vid, movieId, vidTitle)
        }
    }
}