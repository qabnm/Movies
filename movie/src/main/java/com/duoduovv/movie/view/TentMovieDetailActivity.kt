package com.duoduovv.movie.view

import android.os.Environment
import android.util.Log
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

        val downloader = TXVodDownloadManager.getInstance()
        downloader.setDownloadPath(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath)
        downloader.startDownloadUrl("https://jh.nainiuyx.xyz/yun/jiemi.php?key=86fd0c346e44388adc195f5eb05215e4923c0cff3a0d167053c5ea037db25ea5f0b911259f77c67e6645f60e5a060b4fed12a5730679674ca4175908d76b0e0ab0649651dae15e2b92547e51d826526768df1132530ec274fc93b11c7f4bedfd6c497e3f8777065db02479a7&sign=38814224ba40ff5b20de3dc616025434&t=1623402565&name=jianghu.m3u8")
        downloader.setListener(object :ITXVodDownloadListener{
            override fun onDownloadStart(p0: TXVodDownloadMediaInfo?) {
                Log.d("tentPlayer","onDownloadStart")
            }

            override fun onDownloadProgress(p0: TXVodDownloadMediaInfo?) {
                Log.d("tentPlayer","onDownloadProgress${p0?.progress}")
            }

            override fun onDownloadStop(p0: TXVodDownloadMediaInfo?) {
                Log.d("tentPlayer","onDownloadStop${p0?.downloadSize}")
            }

            override fun onDownloadFinish(p0: TXVodDownloadMediaInfo?) {
                Log.d("tentPlayer","onDownloadFinish")
            }

            override fun onDownloadError(p0: TXVodDownloadMediaInfo?, p1: Int, p2: String?) {
                Log.d("tentPlayer","onDownloadError${p2}$p1")
            }

            override fun hlsKeyVerify(
                p0: TXVodDownloadMediaInfo?,
                p1: String?,
                p2: ByteArray?
            ): Int {
                Log.d("tentPlayer","hlsKeyVerify$")
                return 0
            }
        })
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