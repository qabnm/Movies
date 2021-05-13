package com.duoduovv.cinema.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duoduovv.cinema.R
import com.duoduovv.cinema.bean.Banner
import com.duoduovv.cinema.databinding.ItemBannerViewBinding
import com.youth.banner.adapter.BannerAdapter
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/1/15 17:59
 * @des:首页顶部banner
 */
class BannerImgAdapter(data: List<Banner>, private val context: Context) :
    BannerAdapter<Banner, BannerImgAdapter.BannerViewHolder>(data) {
    private lateinit var mBind:ItemBannerViewBinding

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int):BannerViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_banner_view, parent, false)
        mBind = ItemBannerViewBinding.bind(itemView)
        return BannerViewHolder(itemView)
    }

    override fun onBindView(holder: BannerViewHolder, data: Banner?, position: Int, size: Int) {
        GlideUtils.setImg(context = context,url = data?.img?:"",imageView = mBind.imgBanner)
        mBind.tvTitle.text = data?.title
    }
}