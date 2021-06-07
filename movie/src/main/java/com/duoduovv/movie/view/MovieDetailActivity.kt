package com.duoduovv.movie.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.component.ShareDialogFragment
import com.duoduovv.common.listener.VideoPlayCallback
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.SampleCoverVideo
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.ChangePlayLineAdapter
import com.duoduovv.movie.adapter.MovieDetailAdapter
import com.duoduovv.movie.bean.*
import com.duoduovv.movie.component.MovieDetailArtSelectDialog
import com.duoduovv.movie.component.MovieDetailCallback
import com.duoduovv.movie.component.MovieDetailDialogFragment
import com.duoduovv.movie.component.MovieDetailSelectDialogFragment
import com.duoduovv.movie.databinding.ActivityMovieDetailBinding
import com.duoduovv.movie.viewmodel.MovieDetailViewModel
import com.duoduovv.room.domain.CollectionBean
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.SHARE_CONTENT
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.SHARE_LINK
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.SHARE_TITLE
import com.duoduovv.weichat.WeiChatTool
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.TransferListener
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.tencent.connect.common.UIListenerManager
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.TITLE
import dc.android.bridge.BridgeContext.Companion.TYPE_ID
import dc.android.bridge.BridgeContext.Companion.URL
import dc.android.bridge.BridgeContext.Companion.WAY_H5
import dc.android.bridge.BridgeContext.Companion.WAY_RELEASE
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.util.OsUtils
import dc.android.bridge.util.StringUtils
import dc.android.bridge.view.BaseViewModelActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager
import tv.danmaku.ijk.media.exo2.ExoMediaSourceInterceptListener
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager
import tv.danmaku.ijk.media.exo2.ExoSourceManager
import java.io.File

/**
 * @author: jun.liu
 * @date: 2021/1/13 16:27
 * @des:影片详情
 */
@Route(path = RouterPath.PATH_MOVIE_DETAIL)
class MovieDetailActivity : BaseViewModelActivity<MovieDetailViewModel>(),
    SampleCoverVideo.OnStartClickListener,
    MovieDetailSelectDialogFragment.OnSelectDialogItemClickListener,
    MovieDetailArtSelectDialog.OnSelectDialogItemClickListener, MovieDetailCallback {
    override fun getLayoutId() = R.layout.activity_movie_detail
    override fun providerVMClass() = MovieDetailViewModel::class.java
    private lateinit var mBind: ActivityMovieDetailBinding
    private var movieId = ""
    private var vid = ""
    private var detailAdapter: MovieDetailAdapter? = null
    private var detailBean: MovieDetailBean? = null
    private var orientationUtils: OrientationUtils? = null
    private var way = "2"
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
    private var fragment: MovieDetailFragment? = null
    private var line = ""//播放线路
    override fun setLayout(isStatusColorDark: Boolean, statusBarColor: Int) {
        super.setLayout(false, ContextCompat.getColor(this, R.color.color000000))
    }

    override fun initView() {
        mBind = ActivityMovieDetailBinding.bind(layoutView)
        //播放详情
        viewModel.getMovieDetail().observe(this, { setData(viewModel.getMovieDetail().value) })
        //获取播放地址
        viewModel.getMoviePlayInfo()
            .observe(this, { setPlayInfo(viewModel.getMoviePlayInfo().value) })
        //点击了推荐视频
        viewModel.getMovieClickInfo()
            .observe(this, { setClickInfo(viewModel.getMovieClickInfo().value) })
        //解析播放地址
        viewModel.getPlayUrl().observe(this, { analysisPlayUrl(viewModel.getPlayUrl().value) })
        //解析三方的地址
        viewModel.getJxUrl().observe(this, { jxPlayUrl(viewModel.getJxUrl().value) })
        orientationUtils = OrientationUtils(this, mBind.videoPlayer)
        orientationUtils?.isEnable = false
        setVideoPlayer()
        mBind.videoPlayer.setVideoAllCallBack(videoCallback)
        mBind.videoPlayer.fullscreenButton.setOnClickListener {
            orientationUtils?.resolveByClick()
            mBind.videoPlayer.startWindowFullscreen(this, true, true)
        }
        screenHeight = OsUtils.getRealDisplayHeight(this)
        topBarHeight = OsUtils.getStatusBarHeight(this)
        navHeight = OsUtils.getNavigationBarHeight(this)
        //添加顶部fragment
        fragment = MovieDetailFragment()
        fragment?.let {
            it.setCallback(this)
            supportFragmentManager.beginTransaction().add(R.id.layoutTop, it).commit()
        }
        detailAdapter = MovieDetailAdapter()
        mBind.rvList.adapter = detailAdapter
        detailAdapter?.setOnItemClickListener { adapter, _, position ->
            val movieId = (adapter as MovieDetailAdapter).data[position].strId
            onMovieClick(movieId)
        }
        mBind.imgError.setOnClickListener { finish() }
    }

    /**
     * 播放出现错误的时候切换线路
     */
    private fun onPlayError() {
        detailBean?.let {
            mBind.layoutStateError.visibility = View.VISIBLE
            val lineAdapter = ChangePlayLineAdapter()
            mBind.rvLine.adapter = lineAdapter
            val lineList = it.lineList
            for (i in lineList.indices) {
                lineList[i].isDefault = false
            }
            for (i in lineList.indices) {
                if (lineList[i].line == line) lineList[i].isDefault = true
            }
            lineAdapter.setList(lineList)
            lineAdapter.setOnItemClickListener { _, _, position ->
                this.line = lineList[position].line
                viewModel.moviePlayInfo(vid, movieId, line, 1)
                mBind.layoutStateError.visibility = View.GONE
                for (i in it.lineList.indices) {
                    it.lineList[i].isDefault = false
                }
                it.lineList[position].isDefault = true
            }
        }
    }

    override fun onJxError() {
        onPlayError()
    }

    /**
     * 播放器相关状态和时间监听毁掉
     */
    private val videoCallback = object : VideoPlayCallback() {
        override fun onPlayError(url: String?, vararg objects: Any?) {
            super.onPlayError(url, *objects)
            onPlayError()
            AndroidUtils.toast("播放出错！", this@MovieDetailActivity)
        }

        override fun onPrepared(url: String?, vararg objects: Any?) {
            super.onPrepared(url, *objects)
            mBind.layoutStateError.visibility = View.GONE
            orientationUtils?.isEnable = mBind.videoPlayer.isRotateWithSystem
            if (currentLength > 0 && vidByQuery == vid) {
                mBind.videoPlayer.seekTo(currentLength)
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
                        viewModel.moviePlayInfo(vid, movieId, line, 1)
                        //更新选集显示
                        for (i in movieItems.indices) {
                            movieItems[i].isSelect = false
                        }
                        movieItems[currentPlayPosition].isSelect = true
                        fragment?.updateSelect(it.movieItems, currentPlayPosition)
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
        setPlayInfo(bean, 1)
    }

    private var playFlag = 0

    /**
     * 视频播放信息  第一次进来的时候，只加载视频信息 但是不播放
     * 只有正常版本的才会走到这里来
     * @param bean MoviePlayInfoBean
     */
    private fun setPlayInfo(bean: MoviePlayInfoBean?, flag: Int = 0) {
        bean?.let {
            //根据type判断是否需要调用解析的接口
            this.playFlag = flag
            when (it.type) {
                "h5" -> {
                    //跳转H5
                    playUrl = it.h5Url
                }
                "stream" -> {
                    //直接播放的 不用再次解析
                    val playList = it.playUrls
                    Log.d("videoPlayer", "****这里执行了：way=$way")
                    if (playList?.isNotEmpty() == true) {
                        mBind.videoPlayer.setStartClick(1)
                        mBind.videoPlayer.setUp(playList[0].url, true, "")
                        mBind.videoPlayer.startPlayLogic()
                    } else { }
                }
                "jx" -> {
                    //需要再次解析播放地址
                    val headers = it.request.headers
                    val map = HashMap<String, String>()
                    for (i in headers.indices) {
                        map[headers[i].name] = headers[i].value
                    }
                    if ("GET" == it.request.method || "get" == it.request.method) {
                        viewModel.jxUrlForGEet(it.request.url, map)
                    } else {
                        //post请求
                        val paramsMap = HashMap<String, String>()
                        val maps = it.request.formParams
                        maps?.let {
                            for (i in maps.indices) {
                                paramsMap[maps[i].name] = maps[i].value
                            }
                        }
                        viewModel.jxUrlForPost(it.request.url, map, paramsMap)
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun jxPlayUrl(content: String?) {
        content?.let {
            viewModel.analysisPlayUrl(vid, movieId, line, it)
        }
    }

    /**
     * 解析播放地址
     * 获取真正的播放地址
     * @param bean List
     */
    private fun analysisPlayUrl(bean: JxPlayUrlBean?) {
        bean?.let {
            val playUrls = it.playUrls
            if (playUrls?.isNotEmpty() == true) {
                mBind.videoPlayer.setStartClick(1)
                mBind.videoPlayer.setUp(playUrls[0].url, true, "")
                mBind.videoPlayer.startPlayLogic()
                Log.d("videoPlayer", "****这里执行了：way=$way")
            }
        }
    }

    override fun initData() {
        vid = intent.getStringExtra(TYPE_ID) ?: ""
        movieId = intent.getStringExtra(ID) ?: ""
        viewModel.movieDetail(id = movieId)
    }

    private fun setData(detailBean: MovieDetailBean?) {
        //查询视频详情
        this.detailBean = detailBean
        if (detailBean == null) return
        movieId = detailBean.movie.strId
        way = detailBean.way
        title = detailBean.movie.vodName
        line = detailBean.playLine
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
            mBind.videoPlayer.loadCoverImage(
                this@MovieDetailActivity,
                detailBean!!.movie.coverUrl,
                ContextCompat.getColor(this@MovieDetailActivity, R.color.color000000)
            )
            fragment?.bindDetail(detailBean!!)
            detailAdapter?.setList(detailBean!!.recommends)
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
                fragment?.bindDetail(detailBean!!)
                //去请求播放地址信息 播放地址也 可能是H5的跳转链接
                if (!hasClickRecommend) {
                    if (StringUtils.isEmpty(vid)) {
                        vid = list[0].vid
                    }
                } else {
                    vid = list[0].vid
                }
                viewModel.moviePlayInfo(vid, movieId, line)
                if (way == WAY_H5) mBind.videoPlayer.setStartClick(0)
            }
            //更新收藏状态
            val collectionBean = viewModel.queryCollectionById(detailBean!!.movie.id)
            fragment?.notifyCollectionChange(collectionBean)
        }
    }

    /**
     * 设置播放器的基本功能
     */
    private fun setVideoPlayer() {
        //EXOPlayer内核，支持格式更多
//        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
        //exo缓存模式，支持m3u8，只支持exo
//        CacheFactory.setCacheManager(ExoPlayerCacheManager::class.java)
        mBind.videoPlayer.apply {
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
        val shareDialog = ShareDialogFragment(shareClickListener)
        shareDialog.showNow(supportFragmentManager, "share")
    }

    private val shareClickListener = object : ShareDialogFragment.OnShareClickListener {
        override fun onQQShareClick(flag: Int) {
            WeiChatTool.regToQQ(BaseApplication.baseCtx)
            WeiChatTool.shareToQQ(
                this@MovieDetailActivity,
                SHARE_TITLE,
                SHARE_CONTENT,
                SHARE_LINK,
                resources.getString(R.string.app_name),
                flag
            )
        }

        override fun onCopyClick() {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(null, SHARE_LINK)
            clipboard.setPrimaryClip(clipData)
            AndroidUtils.toast("复制成功，快去打开看看吧！", this@MovieDetailActivity)
        }

        override fun onWeiChatClick(flag: Int) {
            WeiChatTool.regToWx(BaseApplication.baseCtx)
            WeiChatTool.weiChatShareAsWeb(
                SHARE_LINK,
                SHARE_TITLE,
                SHARE_CONTENT,
                BitmapFactory.decodeResource(resources, R.drawable.share_icon),
                flag
            )
        }
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
                    AndroidUtils.toast("取消收藏成功", this@MovieDetailActivity)
                } else {
                    viewModel.addCollection(it)
                    AndroidUtils.toast("收藏成功", this@MovieDetailActivity)
                }
                val bean = viewModel.queryCollectionById(detailBean!!.movie.id)
                fragment?.notifyCollectionChange(bean)
            }
        } ?: also {
            GlobalScope.launch(Dispatchers.Main) {
                val detailBean = detailBean!!.movie
                val bean = CollectionBean(
                    coverUrl = detailBean.coverUrl,
                    strId = detailBean.strId,
                    movieId = detailBean.id,
                    lastRemark = detailBean.lastRemark,
                    actor = detailBean.vodActor,
                    direcotor = detailBean.vodDirector,
                    movieName = detailBean.vodName,
                    lang = detailBean.vodLang,
                    isCollect = true,
                    collectionTime = System.currentTimeMillis()
                )
                viewModel.addCollection(bean)
                AndroidUtils.toast("收藏成功", this@MovieDetailActivity)
                val beans = viewModel.queryCollectionById(detailBean.id)
                fragment?.notifyCollectionChange(beans)
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        videoHeight = mBind.videoPlayer.measuredHeight
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
        mBind.layoutStateError.visibility = View.GONE
//        if (way == WAY_RELEASE) {
        //只有正常班的才会去请求接口
        viewModel.moviePlayInfo(vid, movieId, line, 1)
        //清理掉当前正在播放的视频
        mBind.videoPlayer.currentPlayer.release()
//        }
    }

    private var hasClickRecommend = false

    /**
     * 点击了推荐的视频
     * @param movieId String
     */
    private fun onMovieClick(movieId: String) {
        //清理掉正在播放的视频
        GlobalScope.launch(Dispatchers.Main) {
            mBind.videoPlayer.currentPlayer.release()
            mBind.layoutStateError.visibility = View.GONE
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
        mBind.videoPlayer.setVideoAllCallBack(null)
        super.onBackPressed()
    }

    override fun onResume() {
        mBind.videoPlayer.currentPlayer.onVideoResume(false)
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
                progress = mBind.videoPlayer.currentPlayer.currentPositionWhenPlaying,
                detailBean = it,
                movieId = movieId,
                vid = vid,
                vidTitle = vidTitle,
                duration = mBind.videoPlayer.currentPlayer.duration
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
        mBind.videoPlayer.currentPlayer.onVideoPause()
        super.onPause()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        (mBind.videoPlayer.currentPlayer as SampleCoverVideo).setStartClick(if (way == WAY_RELEASE) 1 else 0)
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
        if (!StringUtils.isEmpty(playUrl) && (playUrl.startsWith("http") || playUrl.startsWith("https"))) {
            ARouter.getInstance().build(RouterPath.PATH_WEB_VIEW).withString(URL, playUrl)
                .withString(TITLE, title).navigation()
            //跳转系统浏览器
//            val uri = Uri.parse(playUrl)
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            startActivity(intent)
        }
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
            fragment?.updateSelect(it.movieItems, pos)
//            detailAdapter?.notifyItemChanged(0)
            onSelectClick(vid, movieId, vidTitle)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        UIListenerManager.getInstance()
            .onActivityResult(requestCode, resultCode, data, WeiChatTool.shareListener)
        super.onActivityResult(requestCode, resultCode, data)
    }
}