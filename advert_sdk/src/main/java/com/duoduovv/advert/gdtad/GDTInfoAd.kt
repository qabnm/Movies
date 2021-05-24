package com.duoduovv.advert.gdtad

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.qq.e.ads.nativ.express2.AdEventListener
import com.qq.e.ads.nativ.express2.NativeExpressAD2
import com.qq.e.ads.nativ.express2.NativeExpressADData2
import com.qq.e.comm.util.AdError
import dc.android.tools.LiveDataBus

/**
 * @author: jun.liu
 * @date: 2021/5/18 14:59
 * @des:广点通纯图片形式的信息流
 */
class GDTInfoAd {
    private var mNativeExpressAD: NativeExpressAD2? = null
    private var nativeExpressADData: NativeExpressADData2? = null
    private val TAG = "AD_DEMO"

    fun initInfoAd(
        activity: Activity,
        posId: String,
        container: ViewGroup,
        width: Int,
        height: Int
    ) {
        mNativeExpressAD = NativeExpressAD2(
            activity,
            posId,
            object : NativeExpressAD2.AdLoadListener {
                override fun onNoAD(error: AdError?) {
                    Log.d(TAG, "onAdError${error?.errorCode}${error?.errorMsg}")
                }

                override fun onLoadSuccess(adDataList: MutableList<NativeExpressADData2>?) {
                    Log.d(TAG, "onLoadSuccess")
                    if (adDataList?.isNotEmpty() == true) {
                        nativeExpressADData = adDataList[0]
                        nativeExpressADData?.setAdEventListener(object : AdEventListener {
                            override fun onClick() {
                                Log.d(TAG, "onClick")
                            }

                            override fun onExposed() {
                                Log.d(TAG, "onExposed")
                            }

                            override fun onRenderSuccess() {
                                Log.d(TAG, "onRenderSuccess")
                                container.visibility = View.VISIBLE
                                container.removeAllViews()
                                nativeExpressADData?.adView?.let {
                                    LiveDataBus.get().with("render").value = "render"
                                    container.addView(it)
                                }
                            }

                            override fun onRenderFail() {
                                Log.d(TAG, "onRenderFail")
                            }

                            override fun onAdClosed() {
                                Log.d(TAG, "onAdClosed")
                                container.removeAllViews()
                                nativeExpressADData?.destroy()
                            }
                        })
                        nativeExpressADData?.render()
                    }
                }
            })
        //这里的单位是dp
        mNativeExpressAD?.setAdSize(width, height)
        mNativeExpressAD?.loadAd(1)
        nativeExpressADData?.destroy()
    }

    fun destroyInfoAd() {
        nativeExpressADData?.destroy()
    }
}