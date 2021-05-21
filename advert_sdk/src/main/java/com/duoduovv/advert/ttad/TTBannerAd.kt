package com.duoduovv.advert.ttad

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTNativeExpressAd

/**
 * @author: jun.liu
 * @date: 2021/5/21 15:19
 * @des:穿山甲banner广告
 */
class TTBannerAd {
    private val TAG = "AD_DEMO"
    private var ttBannerAd: TTNativeExpressAd? = null

    fun initBanner(
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
        mTTAdNative.loadBannerExpressAd(adSlot, object : TTAdNative.NativeExpressAdListener {
            override fun onError(code: Int, msg: String?) {
                Log.d(TAG, "获取banner广告错误$code$msg")
            }

            override fun onNativeExpressAdLoad(adList: MutableList<TTNativeExpressAd>?) {
                if (adList?.isNotEmpty() == true) {
                    ttBannerAd = adList[0]
                    ttBannerAd?.render()
                    ttBannerAd?.setExpressInteractionListener(object :
                        TTNativeExpressAd.ExpressAdInteractionListener {
                        override fun onAdClicked(p0: View?, p1: Int) {}

                        override fun onAdShow(p0: View?, p1: Int) {}

                        override fun onRenderFail(p0: View?, p1: String?, p2: Int) {}

                        override fun onRenderSuccess(view: View?, p1: Float, p2: Float) {
                            view?.let { container.addView(it) }
                        }
                    })
                }
            }
        })
    }

    fun onDestroy() {
        ttBannerAd?.destroy()
    }
}