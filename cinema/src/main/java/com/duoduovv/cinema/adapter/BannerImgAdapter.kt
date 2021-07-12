package com.duoduovv.cinema.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.duoduovv.advert.gdtad.GDTInfoAdForSelfRender
import com.duoduovv.advert.ttad.TTInfoAd
import com.duoduovv.cinema.R
import com.duoduovv.cinema.bean.Banner
import com.duoduovv.cinema.databinding.BannerAdContainerBinding
import com.duoduovv.cinema.databinding.ItemBannerViewBinding
import com.duoduovv.common.BaseApplication
import com.qq.e.ads.nativ.MediaView
import com.qq.e.ads.nativ.widget.NativeAdContainer
import com.youth.banner.adapter.BannerAdapter
import dc.android.bridge.BridgeContext.Companion.TYPE_GDT_AD
import dc.android.bridge.BridgeContext.Companion.TYPE_TT_AD
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.OsUtils

/**
 * @author: jun.liu
 * @date: 2021/1/15 17:59
 * @des:首页顶部banner
 */
class BannerImgAdapter(private val data: List<Banner>, private val context: Context) :
    BannerAdapter<Banner, RecyclerView.ViewHolder>(data) {
    private var ttInfoAd: TTInfoAd? = null
    private var gdtInfoAd: GDTInfoAdForSelfRender? = null
    private var bannerWidth = 0f
    private val typeAd = 1
    private val typeBanner = 2

    init {
        bannerWidth =
            (OsUtils.px2dip(context, OsUtils.getScreenWidth(context).toFloat()) - 20).toFloat()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        ttInfoAd?.destroyInfoAd()
        gdtInfoAd?.onDestroy()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int) = when (viewType) {
        typeAd -> {
            val adBinder =
                BannerAdContainerBinding.inflate(LayoutInflater.from(context), parent, false)
            AdViewHolder(adBinder)
        }
        else -> {
            val bannerBind =
                ItemBannerViewBinding.inflate(LayoutInflater.from(context), parent, false)
            BannerViewHolder(bannerBind)
        }
    }

    override fun onBindView(
        holder: RecyclerView.ViewHolder,
        data: Banner?,
        position: Int,
        size: Int
    ) {
        when (holder.itemViewType) {
            typeAd -> {
                BaseApplication.configBean?.ad?.mainPageBanner?.let {
                    when (it.type) {
                        TYPE_TT_AD -> {
                            (holder as AdViewHolder).adBinder.layoutTTAd.visibility =
                                View.VISIBLE
                            holder.adBinder.layoutGdt.visibility = View.GONE
                            initTTAd(holder.adBinder.layoutTTAd, it.value)
                        }
                        TYPE_GDT_AD -> {
                            (holder as AdViewHolder).adBinder.layoutTTAd.visibility = View.GONE
                            holder.adBinder.layoutGdt.visibility = View.VISIBLE
                            initGDTAd(
                                holder.adBinder.adImgCover,
                                holder.adBinder.mediaView,
                                holder.adBinder.layoutGdt,
                                it.value
                            )
                        }
                    }
                }
            }
            else -> {

                GlideUtils.setMovieImg(
                    context = context,
                    url = data?.img ?: "",
                    imageView = (holder as BannerViewHolder).bannerBind.imgBanner,
                    R.drawable.banner_df
                )
                holder.bannerBind.tvTitle.text = data?.title
            }
        }
    }

    /**
     * 请求穿山甲广告
     */
    private fun initTTAd(container: ViewGroup, posId: String) {
        if (null == ttInfoAd) {
            ttInfoAd = TTInfoAd()
            ttInfoAd?.initTTInfoAd(context as Activity, posId, bannerWidth, 0f, container)
        }
    }

    /**
     * 初始化广点通广告
     */
    private fun initGDTAd(
        adImg: ImageView,
        mediaView: MediaView,
        container: NativeAdContainer,
        posId: String
    ) {
        if (null == gdtInfoAd) {
            gdtInfoAd = GDTInfoAdForSelfRender()
            gdtInfoAd?.initInfoAd(context as Activity, posId, adImg, mediaView, container)
        }
    }

    private class AdViewHolder(val adBinder: BannerAdContainerBinding) :
        RecyclerView.ViewHolder(adBinder.root)

    private class BannerViewHolder(val bannerBind: ItemBannerViewBinding) :
        RecyclerView.ViewHolder(bannerBind.root)

    fun onDestroy() {
        ttInfoAd?.destroyInfoAd()
        gdtInfoAd?.onDestroy()
    }

    override fun getItemViewType(position: Int) =
        if ("ad" == data[getRealPosition(position)].type) typeAd else typeBanner
}