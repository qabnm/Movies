package com.duoduovv.hotspot.adapter

import android.view.View
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.common.listener.VideoPlayCallback
import com.duoduovv.common.util.SampleCoverVideo
import com.duoduovv.hotspot.R
import com.duoduovv.hotspot.bean.ShortVideoBean
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

/**
 * @author: jun.liu
 * @date: 2021/1/7 : 11:01
 * 短视频
 */
class ShortVideoAdapter : BaseQuickAdapter<ShortVideoBean, BaseViewHolder>(R.layout.item_short_video) {
    override fun convert(holder: BaseViewHolder, item: ShortVideoBean) {
        holder.getView<SampleCoverVideo>(R.id.videoPlayer).apply {
            setUp(item.url, true, item.title)
            backButton.visibility = View.GONE  //设置返回键
            //设置封面
            thumbImageViewLayout.visibility = View.VISIBLE
            loadCoverImage(item.coverUlr,R.drawable.back)
            //设置全屏按键功能
            fullscreenButton.setOnClickListener { this.startWindowFullscreen(context, false, true) }
            //防止错位设置
            playTag = holder.layoutPosition.toString()
            playPosition = holder.layoutPosition
            //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
            isAutoFullWithSize = true
            //音频焦点冲突时是否释放
            isReleaseWhenLossAudio = false
            //全屏动画
            isShowFullAnimation = true
            //非wifi环境下，显示流量提醒
            isNeedShowWifiTip = true
            isShowDragProgressTextOnSeekBar = true //拖动进度条时，是否在 seekbar 开始部位显示拖动进度
            setVideoAllCallBack(object : VideoPlayCallback() {
                override fun onAutoComplete(url: String?, vararg objects: Any?) {
                    item.complete = true
                    notifyItemChanged(holder.layoutPosition)
                }
            })
            holder.getView<LinearLayout>(R.id.layoutRePlay).setOnClickListener { onRePlay(holder, this) }
            onPlayComplete(holder,item.complete)
        }
    }

    /**
     * 播放完成
     */
    private fun onPlayComplete(holder: BaseViewHolder,complete:Boolean) {
        //播放完成 隐藏掉播放器 显示出分享的界面
        holder.setVisible(R.id.videoPlayer, !complete)//隐藏播放器
        holder.setGone(R.id.layoutShare, !complete)//显示分享
    }

    /**
     * 点击重播
     */
    private fun onRePlay(holder: BaseViewHolder, player: StandardGSYVideoPlayer) {
        //隐藏分享界面
        holder.setGone(R.id.layoutShare, isGone = true)
        holder.setVisible(R.id.videoPlayer, isVisible = true)//显示播放器
        player.startPlayLogic()
    }
}