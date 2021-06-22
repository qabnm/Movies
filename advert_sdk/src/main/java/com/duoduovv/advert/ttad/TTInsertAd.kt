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
 * @date: 2021/6/21 15:52
 * @des:穿山甲插屏广告
 */
class TTInsertAd {
    private val TAG = "AD_DEMO"
    private var mttAd: TTNativeExpressAd? = null
    fun initInsertAd(
        activity: Activity,
        posId: String,
        width: Float,
        height: Float
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
        mTTAdNative.loadInteractionExpressAd(adSlot, object :TTAdNative.NativeExpressAdListener{
            override fun onError(code: Int, msg: String?) {
                Log.d(TAG, "onError$code$msg")
            }

            override fun onNativeExpressAdLoad(ad: MutableList<TTNativeExpressAd>?) {
                if (ad?.isNotEmpty() == true){
                    mttAd = ad[0]
                    mttAd?.render()
                    mttAd?.setExpressInteractionListener(object :TTNativeExpressAd.AdInteractionListener{
                        override fun onAdClicked(p0: View?, p1: Int) {}

                        override fun onAdShow(p0: View?, p1: Int) {}

                        override fun onRenderFail(p0: View?, p1: String?, p2: Int) {
                            onDismiss()
                        }

                        override fun onRenderSuccess(view: View?, p1: Float, p2: Float) {
                            view?.let {
                                LiveDataBus.get().with("insert").value = "200"
                                mttAd?.showInteractionExpressAd(activity)
                            }
                        }

                        override fun onAdDismiss() {}
                    })
                }
            }
        })
    }

    private fun onDismiss(){
        LiveDataBus.get().with("dismiss").value = "dismiss"
    }

    fun onDestroy(){
        mttAd?.destroy()
    }
}