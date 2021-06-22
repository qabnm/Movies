package com.duoduovv.advert.gdtad

import android.app.Activity
import android.util.Log
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener
import com.qq.e.comm.util.AdError

/**
 * @author: jun.liu
 * @date: 2021/6/21 17:53
 * @des:广点通插屏广告
 */
class GDTInsertAd {
    private var gdtAd: UnifiedInterstitialAD?=null
    private val TAG = "adLoad"

    fun initInsertAd(activity: Activity,posId:String){
        if (null != gdtAd){
            gdtAd!!.close()
            gdtAd!!.destroy()
            gdtAd = null
        }
        if (null == gdtAd){
            gdtAd = UnifiedInterstitialAD(activity,posId,object :UnifiedInterstitialADListener{
                override fun onADReceive() {
                    Log.d(TAG,"onADReceive")
                }

                override fun onVideoCached() {
                    Log.d(TAG,"onVideoCached")
                }

                override fun onNoAD(p0: AdError?) {
                    Log.d(TAG,"onNoAD${p0?.errorCode}${p0?.errorMsg}")
                }

                override fun onADOpened() {
                    Log.d(TAG,"onADOpened")
                }

                override fun onADExposure() {
                    Log.d(TAG,"onADExposure")
                }

                override fun onADClicked() {
                    Log.d(TAG,"onADClicked")
                }

                override fun onADLeftApplication() {
                    Log.d(TAG,"onADLeftApplication")
                }

                override fun onADClosed() {
                    Log.d(TAG,"onADClosed")
                }

                override fun onRenderSuccess() {
                    Log.d(TAG,"onRenderSuccess")
                    gdtAd?.show()
                }

                override fun onRenderFail() {
                    Log.d(TAG,"onRenderFail")
                }
            })
        }
        gdtAd?.loadAD()
    }

    fun onDestroy(){
        gdtAd?.destroy()
    }
}