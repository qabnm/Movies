package com.duoduovv.personal.view

import androidx.core.content.ContextCompat
import com.duoduovv.personal.R
import com.tencent.liteav.demo.superplayer.SuperPlayerDef
import com.tencent.liteav.demo.superplayer.SuperPlayerGlobalConfig
import com.tencent.liteav.demo.superplayer.SuperPlayerModel
import com.tencent.rtmp.TXLiveConstants
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_tent_player.*

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
    }

    override fun initView() {
        btnStart.setOnClickListener {
            val model = SuperPlayerModel()
            model.apply {
                title = "腾讯测试视频播放"
                url = "https://www.369hyt.com:65/20210312/dakMN0PS/index.m3u8"
                superPlayer.playWithModel(model)
            }
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