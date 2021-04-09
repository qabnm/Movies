package com.duoduovv.personal.view

import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import com.duoduovv.personal.R
import com.tencent.liteav.demo.superplayer.SuperPlayerDef
import com.tencent.liteav.demo.superplayer.SuperPlayerGlobalConfig
import com.tencent.liteav.demo.superplayer.SuperPlayerModel
import com.tencent.liteav.demo.superplayer.SuperPlayerView
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXVodPlayer
import com.tencent.rtmp.downloader.ITXVodDownloadListener
import com.tencent.rtmp.downloader.TXVodDownloadManager
import com.tencent.rtmp.downloader.TXVodDownloadMediaInfo
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_tent_player.*
import kotlinx.android.synthetic.main.activity_tent_player.view.*

/**
 * @author: jun.liu
 * @date: 2021/4/8 11:23
 * @des:
 */
class TentPlayerActivity :BridgeActivity(){
    override fun getLayoutId()= R.layout.activity_tent_player
    override fun setLayout(isStatusColorDark: Boolean, statusBarColor: Int) {
        super.setLayout(false, ContextCompat.getColor(this, R.color.color000000))
    }

    override fun initData() {
        val playerConfig = SuperPlayerGlobalConfig.getInstance()
        playerConfig.apply {
//            enableFloatWindow = true
//            // 设置悬浮窗的初始位置和宽高
//            val rect = SuperPlayerGlobalConfig.TXRect().apply {
//                x = 0
//                y = 0
//                width = 810
//                height = 540
//            }
//            floatViewRect = rect
            // 播放器默认缓存个数
            maxCacheItem = 3
            // 设置播放器渲染模式
            enableHWAcceleration = true
            renderMode = TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN
        }
        superPlayer.setPlayerViewCallback(object :SuperPlayerView.OnSuperPlayerViewCallback{
            override fun onStartFullScreenPlay() {
            }

            override fun onStopFullScreenPlay() {
            }

            override fun onClickFloatCloseBtn() {
            }

            override fun onClickSmallReturnBtn() {
                this@TentPlayerActivity.finish()
            }

            override fun onStartFloatWindowPlay() {
            }
        })
        val url = "https://www.369hyt.com:65/20210318/1cZ2LyqC/index.m3u8"
        superPlayer.play(url)
    }

    override fun initView() {
        val downloader = TXVodDownloadManager.getInstance()
        downloader.setDownloadPath(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath)
        val url = "https://www.369hyt.com:65/20210318/1cZ2LyqC/index.m3u8"
        downloader.setListener(object :ITXVodDownloadListener{
            override fun onDownloadStart(p0: TXVodDownloadMediaInfo?) {
                Log.d("download","onDownloadStart")
            }

            override fun onDownloadProgress(p0: TXVodDownloadMediaInfo?) {
                Log.d("download","onDownloadProgress${p0?.progress}")
            }

            override fun onDownloadStop(p0: TXVodDownloadMediaInfo?) {
                Log.d("download","onDownloadStop")
            }

            override fun onDownloadFinish(p0: TXVodDownloadMediaInfo?) {
                Log.d("download","onDownloadFinish")
            }

            override fun onDownloadError(p0: TXVodDownloadMediaInfo?, p1: Int, p2: String?) {
                Log.d("download","onDownloadError${p2}${p1}")
            }

            override fun hlsKeyVerify(
                p0: TXVodDownloadMediaInfo?,
                p1: String?,
                p2: ByteArray?
            ): Int {
                Log.d("download","hlsKeyVerify${p1}")
                return 0
            }
        })
        btnStart.setOnClickListener {
//            val model = SuperPlayerModel()
//            model.apply {
//                title = "腾讯测试视频播放"
//                url = "https://www.369hyt.com:65/20210312/dakMN0PS/index.m3u8"
//                superPlayer.playWithModel(model)
//            }
            downloader.startDownloadUrl(url)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        superPlayer.release()
        if (superPlayer.playerMode == SuperPlayerDef.PlayerMode.FLOAT){
            superPlayer.resetPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (superPlayer.playerState == SuperPlayerDef.PlayerState.PLAYING){
            superPlayer.onResume()
            if (superPlayer.playerMode == SuperPlayerDef.PlayerMode.FLOAT){
                superPlayer.switchPlayMode(SuperPlayerDef.PlayerMode.WINDOW)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (superPlayer.playerMode == SuperPlayerDef.PlayerMode.FLOAT){
            superPlayer.onPause()
        }
    }
}