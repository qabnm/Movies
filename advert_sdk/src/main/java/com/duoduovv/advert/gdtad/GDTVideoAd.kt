package com.duoduovv.advert.gdtad

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.qq.e.ads.nativ.express2.*
import com.qq.e.comm.util.AdError
import dc.android.tools.LiveDataBus

/**
 * @author: jun.liu
 * @date: 2021/6/9 14:28
 * @des:广点通贴片广告
 */
class GDTVideoAd {
    private var mNativeExpressAD: NativeExpressAD2? = null
    private var nativeExpressADData: NativeExpressADData2? = null
    private val TAG = "AD_DEMO"

    /**
     * 穿山甲贴片广告
     * @param activity Activity
     * @param posId String
     * @param container ViewGroup
     * @param width Int
     * @param height Int
     */
    fun initVideoAd(
        activity: Activity,
        posId: String,
        container: ViewGroup,
        width: Int,
        height: Int
    ) {
        mNativeExpressAD =
            NativeExpressAD2(activity, posId, object : NativeExpressAD2.AdLoadListener {
                override fun onNoAD(error: AdError?) {
                    Log.d(TAG, "onAdError${error?.errorCode}${error?.errorMsg}")
                }

                override fun onLoadSuccess(adDataList: MutableList<NativeExpressADData2>?) {
                    Log.d(TAG, "onLoadSuccess")
                    if (adDataList?.isNotEmpty() == true) {
                        nativeExpressADData = adDataList[0]
                        nativeExpressADData?.setAdEventListener(object :AdEventListener{
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
                                    container.addView(it)
                                    Log.d("adDemo","${it.measuredWidth},${it.measuredHeight}")
                                }
                            }

                            override fun onRenderFail() {
                                Log.d(TAG, "onRenderFail")
                            }

                            override fun onAdClosed() {
                                Log.d(TAG, "onAdClosed")
                                container.removeAllViews()
                                destroyAd()
                            }
                        })
                        nativeExpressADData?.setMediaListener(object :MediaEventListener{
                            override fun onVideoCache() {
                            }

                            override fun onVideoStart() {
                            }

                            override fun onVideoResume() {
                            }

                            override fun onVideoPause() {
                            }

                            override fun onVideoComplete() {
                                container.removeAllViews()
                                destroyAd()
                            }

                            override fun onVideoError() {
                                container.removeAllViews()
                                destroyAd()
                            }
                        })
                        nativeExpressADData?.render()
                    }
                }
            })
        //这里的单位是dp
        mNativeExpressAD?.let {
            it.setAdSize(width, height)
//            it.setVideoOption2(
//                VideoOption2.Builder().setMinVideoDuration(5).setMaxVideoDuration(60).build()
//            )
            it.loadAd(1)
        }
        destroyAd()
    }

    fun destroyAd() {
        nativeExpressADData?.destroy()
    }
}