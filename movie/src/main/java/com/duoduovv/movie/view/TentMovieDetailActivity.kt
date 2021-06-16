package com.duoduovv.movie.view

import android.content.res.Configuration
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.duoduovv.common.util.SampleCoverVideo
import com.duoduovv.movie.R
import com.duoduovv.movie.databinding.ActivityTentMovieDetailBinding
import com.tencent.liteav.demo.superplayer.SuperPlayerCode
import com.tencent.liteav.demo.superplayer.SuperPlayerDef
import com.tencent.liteav.demo.superplayer.SuperPlayerGlobalConfig
import com.tencent.liteav.demo.superplayer.SuperPlayerModel
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.downloader.ITXVodDownloadListener
import com.tencent.rtmp.downloader.TXVodDownloadManager
import com.tencent.rtmp.downloader.TXVodDownloadMediaInfo
import dc.android.bridge.BridgeContext
import dc.android.bridge.view.BridgeActivity

/**
 * @author: jun.liu
 * @date: 2021/6/11 15:44
 * @des:
 */
class TentMovieDetailActivity :BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_tent_movie_detail

    private lateinit var mBind:ActivityTentMovieDetailBinding
    override fun initView() {
        mBind = ActivityTentMovieDetailBinding.bind(layoutView)
    }

    override fun initData() {
        initPlayer()
    }

    override fun setLayout(isStatusColorDark: Boolean, statusBarColor: Int) {
        super.setLayout(false, ContextCompat.getColor(this, R.color.color000000))
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

    override fun onPause() {
        super.onPause()
        if (mBind.superPlayerView.playerMode == SuperPlayerDef.PlayerMode.FLOAT){
            mBind.superPlayerView.onPause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBind.superPlayerView.release()
        if (mBind.superPlayerView.playerMode == SuperPlayerDef.PlayerMode.FLOAT){
            mBind.superPlayerView.resetPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mBind.superPlayerView.playerState == SuperPlayerDef.PlayerState.PLAYING){
            mBind.superPlayerView.onResume()
            if (mBind.superPlayerView.playerMode == SuperPlayerDef.PlayerMode.FLOAT){
                mBind.superPlayerView.switchPlayMode(SuperPlayerDef.PlayerMode.WINDOW)
            }
        }
    }

    private fun initPlayer(){
        val prefs = SuperPlayerGlobalConfig.getInstance()
        prefs.apply {
            //设置播放器默认缓存2个
            maxCacheItem = 2
            enableHWAcceleration = true
            renderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION
        }
        val model = SuperPlayerModel()
        model.title = "我爱特种兵第01集"
        model.url = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8"
        mBind.superPlayerView.playWithModel(model)
    }
}