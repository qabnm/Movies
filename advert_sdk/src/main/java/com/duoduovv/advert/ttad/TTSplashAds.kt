package com.duoduovv.advert.ttad

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTSplashAd
import dc.android.tools.LiveDataBus
import dc.android.tools.ScreenUtils

/**
 * @author: jun.liu
 * @date: 2021/5/18 14:23
 * @des:穿山甲开屏广告
 */
class TTSplashAds {
    private var listener:TTSplashAdListener?= null
    /**
     *初始化穿山甲开屏广告
     * @param activity Activity
     * @param posId String
     * @param timeOut Int
     * @param container ViewGroup
     */
    fun initTTSplashAd(activity: Activity, posId: String, timeOut: Int, container: ViewGroup) {
        val width = ScreenUtils.getScreenWidth(activity)
        val totalHeight = ScreenUtils.getScreenHeight(activity)
        val navHeight = ScreenUtils.getNavigationBarHeight(activity)
        val height = totalHeight - navHeight - ScreenUtils.dip2px(activity, 80f)
        // 创建TTAdNative对象，createAdNative(Context context) context需要传入Activity对象
        val mTTAdNative = TTAdSdk.getAdManager().createAdNative(activity)
        //创建广告请求AdSlot
        val adSlot = AdSlot.Builder()
            .setCodeId(posId)
            .setImageAcceptedSize(width, height)
            .build()
        //加载开屏广告
        listener = TTSplashAdListener(activity,container)
        mTTAdNative.loadSplashAd(adSlot, listener!!, timeOut)
    }

    private class TTSplashAdListener(
        private val activity: Activity,
        private val container: ViewGroup
    ) : TTAdNative.SplashAdListener {
        override fun onError(code: Int, msg: String?) {
            Log.d("ttAd", "开屏广告请求失败：$code${msg}")
            start()
        }

        override fun onTimeout() {
            Log.d("ttAd", "开屏广告请求超时")
            start()
        }

        private fun start() {
            LiveDataBus.get().with("start").value = "start"
        }

        override fun onSplashAdLoad(ad: TTSplashAd?) {
            ad?.let {
                //获取SplashView
                val splashView = it.splashView
                if (!activity.isFinishing) {
                    Log.d("ttAd", "开屏广告请求成功")
                    container.removeAllViews()
                    container.addView(splashView)
                    it.setSplashInteractionListener(object : TTSplashAd.AdInteractionListener {
                        /**
                         * Splash广告的点击回调
                         *
                         * @param view Splash广告
                         * @param type Splash广告的交互类型
                         */
                        override fun onAdClicked(view: View?, type: Int) {}

                        /**
                         * Splash广告的展示回调
                         *
                         * @param view Splash广告
                         * @param type Splash广告的交互类型
                         */
                        override fun onAdShow(view: View?, type: Int) {}

                        /**
                         * 点击跳过时回调
                         */
                        override fun onAdSkip() {
                            start()
                        }

                        /**
                         * 广告播放时间结束
                         */
                        override fun onAdTimeOver() {
                            start()
                        }
                    })
                } else {
                    start()
                }
            }
        }
    }
    fun onDestroy(){
        listener = null
    }
}