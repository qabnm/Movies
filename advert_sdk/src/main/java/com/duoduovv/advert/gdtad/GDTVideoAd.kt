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
    private lateinit var container: ViewGroup


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
        height: Int,
        outContainer:ViewGroup
    ) {
        this.container = container
        mNativeExpressAD =
            NativeExpressAD2(activity, posId, object : NativeExpressAD2.AdLoadListener {
                override fun onNoAD(error: AdError?) {
                    Log.d(TAG, "onAdError${error?.errorCode}${error?.errorMsg}")
                    onVideoPrepare()
                    onAdComplete()
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
                                outContainer.visibility = View.VISIBLE
                                container.removeAllViews()
                                if (nativeExpressADData?.isVideoAd == true){
                                    val length = nativeExpressADData?.videoDuration
                                    LiveDataBus.get().with("videoLength").value = length
                                }else{
                                    //返回的是图片素材
                                    LiveDataBus.get().with("videoLength").value = 0
                                }
                                nativeExpressADData?.adView?.let {
                                    container.addView(it)
                                }
                            }

                            override fun onRenderFail() {
                                Log.d(TAG, "onRenderFail")
                                onVideoPrepare()
                                onAdComplete()
                            }

                            override fun onAdClosed() {
                                Log.d(TAG, "onAdClosed")
                                onVideoPrepare()
                                onAdComplete()
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
                                onVideoPrepare()
                                onAdComplete()
                            }

                            override fun onVideoError() {
                                onVideoPrepare()
                                onAdComplete()
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
        nativeExpressADData?.destroy()
    }

    private fun onVideoPrepare(){
        LiveDataBus.get().with("onAdComplete").value = "onAdComplete"
    }

    fun onAdComplete(){
        container.removeAllViews()
        nativeExpressADData?.destroy()
    }
}