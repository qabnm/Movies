package com.duoduovv.advert.ttad

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTNativeExpressAd
import dc.android.tools.LiveDataBus

/**
 * @author: jun.liu
 * @date: 2021/5/18 18:22
 * @des:穿山甲信息流 浮层广告
 */
class TTInfoAd {
    private val TAG = "AD_DEMO"
    private var mttAd: TTNativeExpressAd? = null

    /**
     * 初始化信息流广告
     * @param activity Activity
     * @param posId String
     * @param width Float
     * @param height Float
     * @param container ViewGroup
     */
    fun initTTInfoAd(
        activity: Activity,
        posId: String,
        width: Float,
        height: Float,
        container: ViewGroup
    ) {
        //创建TTAdNative对象
        val mTTAdNative = TTAdSdk.getAdManager().createAdNative(activity)
        //创建广告请求AdSlot
        val adSlot = AdSlot.Builder()
            .setCodeId(posId)
            .setSupportDeepLink(true)
            .setAdCount(1)
            .setExpressViewAcceptedSize(width, height)
            .build()
        //请求广告
        mTTAdNative.loadNativeExpressAd(adSlot, object : TTAdNative.NativeExpressAdListener {
            override fun onError(code: Int, msg: String?) {
                Log.d(TAG, "onError$code$msg")
            }

            override fun onNativeExpressAdLoad(adList: MutableList<TTNativeExpressAd>?) {
                if (adList?.isNotEmpty() == true) {
                    container.visibility = View.VISIBLE
                    container.removeAllViews()
                    mttAd = adList[0]
                    mttAd?.render()
                    mttAd?.setExpressInteractionListener(object :
                        TTNativeExpressAd.ExpressAdInteractionListener {
                        override fun onAdClicked(p0: View?, p1: Int) {}

                        override fun onAdShow(p0: View?, p1: Int) {}

                        override fun onRenderFail(p0: View?, p1: String?, p2: Int) {}

                        override fun onRenderSuccess(view: View?, p1: Float, p2: Float) {
                            LiveDataBus.get().with("render").value = "render"
                            view?.let { container.addView(it) }
                        }
                    })
                }
            }
        })
    }

    fun destroyInfoAd(){
        mttAd?.destroy()
    }
}