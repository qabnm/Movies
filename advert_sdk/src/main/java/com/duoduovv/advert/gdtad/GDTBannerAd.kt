package com.duoduovv.advert.gdtad

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.qq.e.ads.banner2.UnifiedBannerADListener
import com.qq.e.ads.banner2.UnifiedBannerView
import com.qq.e.comm.util.AdError
import dc.android.tools.LiveDataBus

/**
 * @author: jun.liu
 * @date: 2021/5/19 14:48
 * @des:广点通banner广告
 */
class GDTBannerAd {
    private val TAG = "AD_DEMO"
    private var bannerView:UnifiedBannerView?= null

    fun initBanner(activity: Activity, posId: String, container: ViewGroup) {
        bannerView?.let {
            container.removeView(it)
            it.destroy()
        }
        bannerView = UnifiedBannerView(activity, posId,object :UnifiedBannerADListener{
            override fun onNoAD(p0: AdError?) {
                LiveDataBus.get().with("adClose").value = "adClose"
            }

            override fun onADReceive() {
            }

            override fun onADExposure() {
            }

            override fun onADClosed() {
                LiveDataBus.get().with("adClose").value = "adClose"
            }

            override fun onADClicked() {
            }

            override fun onADLeftApplication() {
            }

            override fun onADOpenOverlay() {
            }

            override fun onADCloseOverlay() {
            }
        })
        container.visibility = View.VISIBLE
        container.addView(bannerView)
        bannerView?.loadAD()
        bannerView?.setRefresh(5)
    }

    fun onDestroy(){
        bannerView?.destroy()
    }
}