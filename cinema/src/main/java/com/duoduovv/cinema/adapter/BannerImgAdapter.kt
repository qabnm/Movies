package com.duoduovv.cinema.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTNativeExpressAd
import com.duoduovv.cinema.R
import com.duoduovv.cinema.bean.Banner
import com.duoduovv.cinema.databinding.ItemBannerViewBinding
import com.qq.e.ads.banner2.UnifiedBannerADListener
import com.qq.e.ads.banner2.UnifiedBannerView
import com.qq.e.comm.util.AdError
import com.youth.banner.adapter.BannerAdapter
import dc.android.bridge.util.GlideUtils
import kotlin.math.roundToInt

/**
 * @author: jun.liu
 * @date: 2021/1/15 17:59
 * @des:首页顶部banner
 */
class BannerImgAdapter(data: List<Banner>, private val context: Context) :
    BannerAdapter<Banner, BannerImgAdapter.BannerViewHolder>(data) {
    private lateinit var mBind:ItemBannerViewBinding
    private var bannerView:UnifiedBannerView?=null

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int):BannerViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_banner_view, parent, false)
        mBind = ItemBannerViewBinding.bind(itemView)
        return BannerViewHolder(itemView)
    }

    override fun onBindView(holder: BannerViewHolder, data: Banner?, position: Int, size: Int) {
        if (position == 0){
            //加载广告
//            getBanner()?.loadAD()
            ttBanner()
        }else {
//            GlideUtils.setImg(context = context, url = data?.img ?: "", imageView = mBind.imgBanner)
            mBind.imgBanner.load(data?.img)
            mBind.tvTitle.text = data?.title
        }
    }

    private fun ttBanner(){
        val mTTAdNative = TTAdSdk.getAdManager().createAdNative(context as Activity)
        val adSlot = AdSlot.Builder()
            .setCodeId("946107597")
            .setSupportDeepLink(true)
            .setAdCount(1)
            .setExpressViewAcceptedSize(355f,152f)
            .build()
        mTTAdNative.loadBannerExpressAd(adSlot, object :TTAdNative.NativeExpressAdListener{
            override fun onError(p0: Int, p1: String?) {
                Log.d("AD_DEMO","onError$p0$p1")
            }

            override fun onNativeExpressAdLoad(p0: MutableList<TTNativeExpressAd>?) {
                Log.d("AD_DEMO","onNativeExpressAdLoad")
                p0?.let {
                    val bannerView = it[0].expressAdView
                    mBind.layoutContainer.removeAllViews()
                    mBind.layoutContainer.addView(bannerView)
                }
            }
        })
    }

    private fun getBanner():UnifiedBannerView?{
        if (null != bannerView) return bannerView
        bannerView?.let { mBind.layoutContainer.removeView(it) }
        bannerView = UnifiedBannerView(context as Activity, "5011588732659291",object :UnifiedBannerADListener{
            override fun onNoAD(error: AdError?) {
                Log.d("AD_DEMO","onNoAD${error?.errorMsg}${error?.errorCode}")
            }

            override fun onADReceive() {
                Log.d("AD_DEMO","onADReceive")
            }

            override fun onADExposure() {
                Log.d("AD_DEMO","onADExposure")
            }

            override fun onADClosed() {
                Log.d("AD_DEMO","onADClosed")
            }

            override fun onADClicked() {
                Log.d("AD_DEMO","onADClicked")
            }

            override fun onADLeftApplication() {
                Log.d("AD_DEMO","onADLeftApplication")
            }

            override fun onADOpenOverlay() {
                Log.d("AD_DEMO","onADOpenOverlay")
            }

            override fun onADCloseOverlay() {
                Log.d("AD_DEMO","onADCloseOverlay")
            }
        })
        mBind.layoutContainer.addView(bannerView, getUnifiedBannerLayoutParams())
        return bannerView
    }

    private fun getUnifiedBannerLayoutParams():ConstraintLayout.LayoutParams{
        return ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
    }
}