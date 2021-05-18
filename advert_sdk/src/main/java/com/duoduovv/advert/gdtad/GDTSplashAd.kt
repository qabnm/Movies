package com.duoduovv.advert.gdtad

import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import com.qq.e.ads.splash.SplashAD
import com.qq.e.ads.splash.SplashADListener
import com.qq.e.comm.util.AdError
import dc.android.tools.LiveDataBus

/**
 * @author: jun.liu
 * @date: 2021/5/18 14:08
 * @des:
 */
class GDTSplashAd {
    private var canJump = true

    /**
     * 初始化广点通开屏广告
     * @param activity Activity
     * @param container ViewGroup
     * @param posId String
     */
    fun initGDTSplash(activity: Activity, container: ViewGroup, posId:String){
        val splashAd = SplashAD(activity, posId, gdtSplashADListener, 0)
        splashAd.fetchAndShowIn(container)
    }

    fun next() {
        Log.d("AD_DEMO","$canJump")
        if (canJump) {
            start()
        } else {
            canJump = true
        }
    }

    fun setCanJump(canJump:Boolean){
        this.canJump = canJump
    }

    /**
     * 是否可以跳转
     * @return Boolean
     */
    fun getCanJump() = canJump

    /**
     * 跳转首页
     */
    private fun start(){
        LiveDataBus.get().with("start").value = "start"
    }

    /**
     * 广点通广告获取加载listener
     */
    private val gdtSplashADListener = object : SplashADListener {
        /**
         * 广告关闭时调用，可能是用户关闭或者展示时间到
         */
        override fun onADDismissed() {
            Log.d("AD_DEMO", "onADDismissed")
            next()
        }

        /**
         * 广告加载失败
         * @param error AdError
         */
        override fun onNoAD(error: AdError?) {
            start()
        }

        /**
         * 广告展示成功
         */
        override fun onADPresent() {
            Log.d("AD_DEMO", "onADPresent")
        }

        /**
         * 广告被点击时调用，不代表满足计费条件（如点击时网络异常）
         */
        override fun onADClicked() {
            Log.d("AD_DEMO", "onADClicked")
        }

        /**
         * 倒计时的回调
         * @param p0 Long
         */
        override fun onADTick(p0: Long) {}

        /**
         * 广告曝光时调用
         */
        override fun onADExposure() {
            Log.d("AD_DEMO", "onADExposure")
        }

        /**
         * 广告加载成功的回调，在fetchAdOnly的情况下，表示广告拉取成功可以显示了。广告需要在SystemClock.elapsedRealtime <expireTimestamp前展示，
         * 否则在showAd时会返回广告超时错误。
         * @param expireTimestamp Long
         */
        override fun onADLoaded(expireTimestamp: Long) {
            Log.d("AD_DEMO", "onADLoaded")
        }
    }
}