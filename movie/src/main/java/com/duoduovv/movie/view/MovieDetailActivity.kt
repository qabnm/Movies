package com.duoduovv.movie.view

import android.util.Log
import android.view.View
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
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack
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
    override fun setLayout(isStatusColorDark: Boolean, statusBarColor: Int) {
        super.setLayout(false, resources.getColor(R.color.color000000))
    }

    override fun initView() {
        rvList.layoutManager = GridLayoutManager(this,3)
        viewModel.getMovieDetail().observe(this, { setData(viewModel.getMovieDetail().value) })
        viewModel.getMoviePlayInfo().observe(this, { setPlayInfo(viewModel.getMoviePlayInfo().value) })
        viewModel.getAddState().observe(this, { })
        viewModel.getDeleteState().observe(this, { })
        setVideoPlayer()
        videoPlayer.setVideoAllCallBack(object :VideoPlayCallback(){
            override fun onClickStartIcon(url: String?, vararg objects: Any?) {
                //请求播放信息
                viewModel.moviePlayInfo(vid, movieId)
            }
        })
    }

    /**
     * 视频播放信息
     * @param bean MoviePlayInfoBean
     */
    private fun setPlayInfo(bean: MoviePlayInfoBean?){
        bean?.let {
            val playList = it.playUrls
            if (playList.isNotEmpty()){
                videoPlayer.setUp(playList[0].url,true,"")
            }
        }
    }

    override fun initData() {
        movieId = intent.getStringExtra(BridgeContext.ID) ?: ""
        viewModel.movieDetail(id = movieId)
    }

    private fun setData(detailBean: MovieDetailBean?) {
        if (detailBean == null) return
        //视频信息
        videoPlayer.loadCoverImage(detailBean.movie.cover_url,R.drawable.back_white)
        detailAdapter = MovieDetailAdapter(this, detailBean = detailBean)
        detailAdapter?.setOnViewClick(this)
        rvList.adapter = detailAdapter
        val list = detailBean.movieItems
        if (list.isNotEmpty()) vid = list[0].vid
    }

    private fun setVideoPlayer(){
        videoPlayer.apply {
            thumbImageViewLayout.visibility = View.VISIBLE
            //设置全屏按键功能
            fullscreenButton.setOnClickListener { this.startWindowFullscreen(context, false, true) }
            //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
            isAutoFullWithSize = true
            //音频焦点冲突时是否释放
            isReleaseWhenLossAudio = false
            //全屏动画
            isShowFullAnimation = true
            //非wifi环境下，显示流量提醒
            isNeedShowWifiTip = true
            isShowDragProgressTextOnSeekBar = true //拖动进度条时，是否在 seekbar 开始部位显示拖动进度
            backButton.setOnClickListener { finish() }
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
    override fun onCollectClick(isCollection: Int) {
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
        Log.d("height","screenHeight:${screenHeight}**topBarHeight:${topBarHeight}**videoHeight${videoHeight}")
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
        Log.d("height","screenHeight:${screenHeight}**topBarHeight:${topBarHeight}**videoHeight${videoHeight}")
        val dialogFragment = MovieDetailSelectDialogFragment(height = realHeight, dataList)
        dialogFragment.showNow(supportFragmentManager, "select")
    }

}