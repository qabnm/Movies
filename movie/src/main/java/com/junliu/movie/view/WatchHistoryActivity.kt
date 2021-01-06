package com.junliu.movie.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.common.util.RouterPath
import com.junliu.movie.R
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_watch_history.*

/**
 * @author: jun.liu
 * @date: 2021/1/6 13:28
 * @des:观看历史
 */
@Route(path = RouterPath.PATH_WATCH_HISTORY)
class WatchHistoryActivity :BridgeActivity(){
    override fun getLayoutId() = R.layout.activity_watch_history

    override fun initData() {
        //以下测试视频播放
        val url = "http://mudan.iii-kuyunzy.com/20200121/10197_bbd984b4/index.m3u8"
        videoPlayer.setUp(url , true,"测试视频")
    }
}