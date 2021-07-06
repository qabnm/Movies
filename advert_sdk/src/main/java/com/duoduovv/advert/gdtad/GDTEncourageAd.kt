package com.duoduovv.advert.gdtad

import android.app.Activity
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import com.qq.e.ads.rewardvideo.RewardVideoAD
import com.qq.e.ads.rewardvideo.RewardVideoADListener
import com.qq.e.comm.util.AdError
import dc.android.tools.LiveDataBus

/**
 * @author: jun.liu
 * @date: 2021/6/29 16:21
 * @des:腾讯激励视频
 */
class GDTEncourageAd {
    private var rewardVideoAD: RewardVideoAD? = null
    private val tag = "GDTEncourageAd"

    fun initAd(activity: Activity, posId: String) {
        if (null == rewardVideoAD){
            rewardVideoAD = RewardVideoAD(activity, posId, object : RewardVideoADListener {
                override fun onADLoad() {
                    //广告加载成功，可在此回调后进行广告展示
                    Log.d(tag, "onADLoad")
                }

                override fun onVideoCached() {
                    //视频素材缓存成功，可在此回调后进行广告展示
                    Log.d(tag, "onVideoCached")
                    LiveDataBus.get().with("encourageAd").value = "start"
                    if (rewardVideoAD?.hasShown() == false) {//当前广告数据还没有展示过
                        val delta = 1000//建议给广告过期时间加个buffer，单位ms，这里demo采用1000ms的buffer
                        //展示广告前判断广告数据未过期
                        if (SystemClock.elapsedRealtime()<(rewardVideoAD!!.expireTimestamp- delta)){
                            rewardVideoAD?.showAD()
                        }else{
                            adClose()
//                        Toast.makeText(activity, "激励视频广告已过期，请再次请求广告后进行广告展示！", Toast.LENGTH_LONG).show()
                        }
                    }else{
                        adClose()
//                    Toast.makeText(activity, "激励视频广告已过期，请再次请求广告后进行广告展示！", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onADShow() {
                    Log.d(tag, "onADShow")
                }

                override fun onADExpose() {
                    Log.d(tag, "onADExpose")
                }

                override fun onReward(map: MutableMap<String, Any>?) {
                    //激励视频触发激励（观看视频大于一定时长或者视频播放完毕）
                    Log.d(tag, "onReward")
                }

                override fun onADClick() {
                    Log.d(tag, "onADClick")
                }

                override fun onVideoComplete() {
                    Log.d(tag, "onVideoComplete")
                }

                override fun onADClose() {
                    Log.d(tag, "onADClose")
                    adClose()
                }

                override fun onError(error: AdError?) {
                    Log.d(tag, "onError${error?.errorCode}${error?.errorMsg}")
                    adClose()
                }
            })
        }
        rewardVideoAD?.loadAD()
    }

    private fun adClose(){
        LiveDataBus.get().with("encourageAd").value = "onAdClose"
        onDestroy()
    }

    fun onDestroy(){
//        rewardVideoAD = null
    }
}