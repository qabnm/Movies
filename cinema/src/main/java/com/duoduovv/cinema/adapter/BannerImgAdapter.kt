package com.duoduovv.cinema.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duoduovv.advert.AdvertBridge
import com.duoduovv.advert.gdtad.GDTInfoAd
import com.duoduovv.advert.ttad.TTInfoAd
import com.duoduovv.cinema.bean.Banner
import com.duoduovv.cinema.databinding.ItemBannerViewBinding
import com.youth.banner.adapter.BannerAdapter
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.OsUtils
import kotlinx.coroutines.flow.combine

/**
 * @author: jun.liu
 * @date: 2021/1/15 17:59
 * @des:首页顶部banner
 */
class BannerImgAdapter(data: List<Banner>, private val context: Context) :
    BannerAdapter<Banner, BannerImgAdapter.BannerViewHolder>(data) {
    private var ttInfoAd: TTInfoAd? = null
    private var gdtInfoAd: GDTInfoAd? = null

    class BannerViewHolder(val bind: ItemBannerViewBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val bind = ItemBannerViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return BannerViewHolder(bind)
    }

    override fun onBindView(holder: BannerViewHolder, data: Banner?, position: Int, size: Int) {
        if (data?.type == "ad") {
            Log.d("adad","onBindView方法执行了")
            //加载广告
            if (AdvertBridge.TT_AD == AdvertBridge.AD_TYPE) {
                initTTAd(holder.bind.layoutContainer, AdvertBridge.MAIN_PAGE_BANNER)
            } else {
                initGDTAd(holder.bind.layoutContainer, AdvertBridge.MAIN_PAGE_BANNER)
            }
        } else {
            Log.d("adad","onBindView方法执行了99999999999999999")
            GlideUtils.setImg(
                context = context,
                url = data?.img ?: "",
                imageView = holder.bind.imgBanner
            )
//            mBind.imgBanner.load(data?.img)
            holder.bind.tvTitle.text = data?.title
            Log.d("hhhh", "${holder.bind.imgBanner.width},${holder.bind.imgBanner.height}")
        }
    }

    /**
     * 请求穿山甲广告
     */
    private fun initTTAd(container: ViewGroup, posId: String) {
        if (null == ttInfoAd) {
            ttInfoAd = TTInfoAd()
            val width = (OsUtils.px2dip(context,OsUtils.getScreenWidth(context).toFloat())- 20).toFloat()
            Log.d("adView","$width")
            ttInfoAd?.initTTInfoAd(context as Activity, posId, width, 0f, container)
        }
    }

    /**
     * 初始化广点通广告
     */
    private fun initGDTAd(container: ViewGroup, posId: String) {
        if (null == gdtInfoAd) {
            gdtInfoAd = GDTInfoAd()
            gdtInfoAd?.initInfoAd(context as Activity, posId, container, 380, 0)
        }
    }

    fun onDestroy() {
        ttInfoAd?.destroyInfoAd()
        gdtInfoAd?.destroyInfoAd()
    }
}