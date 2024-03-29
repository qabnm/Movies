package com.duoduovv.cinema.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duoduovv.advert.gdtad.GDTInfoAdForSelfRender
import com.duoduovv.advert.ttad.TTInfoAd
import com.duoduovv.cinema.R
import com.duoduovv.cinema.bean.BannerBean
import com.duoduovv.cinema.databinding.BannerAdContainerBinding
import com.duoduovv.cinema.databinding.ItemBannerViewBinding
import com.duoduovv.common.BaseApplication
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
class BannerImgAdapter(private val data: List<BannerBean>, private val context: Context) :
    BannerAdapter<BannerBean, RecyclerView.ViewHolder>(data) {
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
        data: BannerBean?,
        position: Int,
        size: Int
    ) {
        when (holder.itemViewType) {
            typeAd -> {
                BaseApplication.configBean?.ad?.mainPageBanner?.let {
                    when (it.type) {
                        TYPE_TT_AD -> { initTTAd(it.value, holder as AdViewHolder) }
                        TYPE_GDT_AD -> { initGDTAd(holder as AdViewHolder, it.value) }
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
    private fun initTTAd(posId: String, holder: AdViewHolder) {
        if (null == ttInfoAd) {
            holder.adBinder.layoutTTAd.visibility = View.VISIBLE
            holder.adBinder.layoutGdt.visibility = View.GONE
            ttInfoAd = TTInfoAd()
            ttInfoAd?.initTTInfoAd(context as Activity, posId, bannerWidth, 0f, holder.adBinder.layoutTTAd)
        }
    }

    /**
     * 初始化广点通广告
     */
    private fun initGDTAd(holder: AdViewHolder, posId: String) {
        if (null == gdtInfoAd) {
            holder.adBinder.layoutTTAd.visibility = View.GONE
            holder.adBinder.layoutGdt.visibility = View.VISIBLE
            gdtInfoAd = GDTInfoAdForSelfRender()
            gdtInfoAd?.initInfoAd(
                context as Activity,
                posId,
                holder.adBinder.adImgCover,
                holder.adBinder.mediaView,
                holder.adBinder.layoutGdt
            )
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