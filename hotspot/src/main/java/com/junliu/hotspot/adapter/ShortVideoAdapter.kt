package com.junliu.hotspot.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.junliu.common.listener.VideoPlayCallback
import com.junliu.hotspot.R
import com.junliu.hotspot.bean.ShortVideoBean
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/1/7 : 11:01
 * 短视频
 */
class ShortVideoAdapter :
    BaseQuickAdapter<ShortVideoBean, BaseViewHolder>(R.layout.item_short_video) {
    override fun convert(holder: BaseViewHolder, item: ShortVideoBean) {
        holder.getView<StandardGSYVideoPlayer>(R.id.videoPlayer).apply {
            setUp(item.url, true, "")
            titleTextView.visibility = View.GONE  //增加title
            backButton.visibility = View.GONE  //设置返回键

            //设置封面
            thumbImageViewLayout.visibility = View.VISIBLE
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                GlideUtils.setImg(context, item.coverUlr, this)
                thumbImageView = this
            }
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
            //小屏时不触摸滑动
            setIsTouchWiget(false)
            //非wifi环境下，显示流量提醒
            isNeedShowWifiTip = true
            setVideoAllCallBack(object : VideoPlayCallback() {
                override fun onAutoComplete(url: String?, vararg objects: Any?) {
                    super.onAutoComplete(url, *objects)
                    onPlayComplete(holder = holder)
                }
            })
            holder.getView<TextView>(R.id.tvRePlay).setOnClickListener { onRePlay(holder, this) }
        }
    }

    /**
     * 播放完成
     */
    private fun onPlayComplete(holder: BaseViewHolder) {
        //播放完成 隐藏掉播放器 显示出分享的界面
        holder.setVisible(R.id.videoPlayer, false)//隐藏播放器
        holder.setGone(R.id.layoutShare, false)//显示分享
        notifyItemChanged(holder.layoutPosition)
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