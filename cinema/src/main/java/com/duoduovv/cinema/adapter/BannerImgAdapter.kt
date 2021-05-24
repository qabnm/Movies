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
    private lateinit var mBind: ItemBannerViewBinding
    private var ttInfoAd: TTInfoAd? = null
    private var gdtInfoAd: GDTInfoAd? = null

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_banner_view, parent, false)
        mBind = ItemBannerViewBinding.bind(itemView)
        return BannerViewHolder(itemView)
    }

    override fun onBindView(holder: BannerViewHolder, data: Banner?, position: Int, size: Int) {
        if (position == 0) {
            //加载广告
//            initTTAd()
            initGDTAd()
        } else {
            GlideUtils.setImg(context = context, url = data?.img ?: "", imageView = mBind.imgBanner)
//            mBind.imgBanner.load(data?.img)
            mBind.tvTitle.text = data?.title
        }
    }

    /**
     * 请求穿山甲广告
     */
    private fun initTTAd() {
        ttInfoAd = TTInfoAd()
        ttInfoAd?.initTTInfoAd(context as Activity, "946107576", 0f, 0f, mBind.layoutContainer)
    }

    /**
     * 初始化广点通广告
     */
    private fun initGDTAd() {
        gdtInfoAd = GDTInfoAd()
        gdtInfoAd?.initInfoAd(context as Activity, "5051684812707537",mBind.layoutContainer,375,0)
    }

    fun onDestroy(){
        ttInfoAd?.destroyInfoAd()
        gdtInfoAd?.destroyInfoAd()
    }
}