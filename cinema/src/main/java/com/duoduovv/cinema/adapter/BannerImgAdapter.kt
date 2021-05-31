package com.duoduovv.cinema.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.duoduovv.advert.gdtad.GDTInfoAd
import com.duoduovv.advert.ttad.TTInfoAd
import com.duoduovv.cinema.R
import com.duoduovv.cinema.bean.Banner
import com.duoduovv.cinema.databinding.ItemBannerViewBinding
import com.qq.e.ads.banner2.UnifiedBannerView
import com.youth.banner.adapter.BannerAdapter
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/1/15 17:59
 * @des:首页顶部banner
 */
class BannerImgAdapter(data: List<Banner>, private val context: Context) :
    BannerAdapter<Banner, BannerImgAdapter.BannerViewHolder>(data) {
    private var ttInfoAd: TTInfoAd? = null
    private var gdtInfoAd: GDTInfoAd? = null

    class BannerViewHolder(val bind:ItemBannerViewBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val bind = ItemBannerViewBinding.inflate(LayoutInflater.from(context),parent ,false)
        return BannerViewHolder(bind)
    }

    override fun onBindView(holder: BannerViewHolder, data: Banner?, position: Int, size: Int) {
//        if (position == 0) {
//            //加载广告
////            initTTAd(holder)
//            initGDTAd(holder)
//        } else {
            GlideUtils.setImg(context = context, url = data?.img ?: "", imageView = holder.bind.imgBanner)
//            mBind.imgBanner.load(data?.img)
            holder.bind.tvTitle.text = data?.title
//        }
    }

    /**
     * 请求穿山甲广告
     */
    private fun initTTAd(holder: BannerViewHolder) {
        ttInfoAd = TTInfoAd()
        ttInfoAd?.initTTInfoAd(context as Activity, "946164816", 0f, 0f, holder.bind.layoutContainer)
    }

    /**
     * 初始化广点通广告
     */
    private fun initGDTAd(holder: BannerViewHolder) {
        gdtInfoAd = GDTInfoAd()
        gdtInfoAd?.initInfoAd(context as Activity, "1031395106138231",holder.bind.layoutContainer,375,0)
    }

    fun onDestroy(){
        ttInfoAd?.destroyInfoAd()
        gdtInfoAd?.destroyInfoAd()
    }
}