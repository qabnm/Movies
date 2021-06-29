package com.duoduovv.advert.ttad

import android.app.Activity
import android.util.Log
import com.bytedance.sdk.openadsdk.*
import dc.android.tools.LiveDataBus

/**
 * @author: jun.liu
 * @date: 2021/6/29 15:35
 * @des:穿山甲激励视频
 */
class TTEncourageAd {
    private val TAG = "TTEncourageAd"
    private var ttRewardVideoAd :TTRewardVideoAd?=null

    /**
     * 穿山甲激励广告
     * @param context Activity
     * @param posId String
     * @param vid String
     * @param orientation Int
     */
    fun initAd(context: Activity, posId: String, vid: String, orientation: Int) {
        val mTTAdNative = TTAdSdk.getAdManager().createAdNative(context)
        val adSlot = AdSlot.Builder()
            .setCodeId(posId)
            .setExpressViewAcceptedSize(1f, 1f)
            .setUserID(vid)
            .setOrientation(if (orientation == 1) TTAdConstant.VERTICAL else TTAdConstant.HORIZONTAL)
            .build()
        mTTAdNative.loadRewardVideoAd(adSlot, object :TTAdNative.RewardVideoAdListener{
            override fun onError(code: Int, msg: String?) {
                Log.d(TAG, "$code$msg")
            }

            override fun onRewardVideoAdLoad(ad: TTRewardVideoAd?) {
                //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验
                Log.d(TAG,"onRewardVideoAdLoad")
                ttRewardVideoAd = ad
                ad?.setRewardAdInteractionListener(object :TTRewardVideoAd.RewardAdInteractionListener{
                    override fun onAdShow() {
                        Log.d(TAG,"onAdShow")
                    }

                    override fun onAdVideoBarClick() {
                        Log.d(TAG,"onAdVideoBarClick")
                    }

                    override fun onAdClose() {
                        Log.d(TAG,"onAdClose")
                    }

                    override fun onVideoComplete() {
                        Log.d(TAG,"onVideoComplete")
                    }

                    override fun onVideoError() {
                        Log.d(TAG,"onVideoError")
                    }

                    override fun onRewardVerify(rewardVerify: Boolean, rewardAmount: Int, rewardName: String?, code: Int, msg: String?) {
                        Log.d(TAG,"onRewardVerify")
                        if (rewardVerify){
                            LiveDataBus.get().with("rewardVerify").value = "rewardVerify"
                        }
                    }

                    override fun onSkippedVideo() {
                        Log.d(TAG,"onSkippedVideo")
                    }
                })
            }

            override fun onRewardVideoCached() {
                //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
                Log.d(TAG,"onRewardVideoCached")
                ttRewardVideoAd?.showRewardVideoAd(context, TTAdConstant.RitScenes.CUSTOMIZE_SCENES,"vid")
                ttRewardVideoAd = null
            }
        })
    }
}