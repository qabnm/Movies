package com.junliu.cinema.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.junliu.cinema.R
import com.junliu.cinema.bean.Banner
import com.youth.banner.adapter.BannerAdapter
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/1/15 17:59
 * @des:首页顶部banner
 */
class BannerImgAdapter(private val data: List<Banner>, private val context: Context) :
    BannerAdapter<Banner, BannerImgAdapter.BannerViewHolder>(data) {

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBanner:ShapeableImageView = itemView.findViewById(R.id.imgBanner)
        val tvTitle:TextView = itemView.findViewById(R.id.tvTitle)
    }

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int) = BannerViewHolder(
        LayoutInflater.from(context).inflate(R.layout.item_banner_view, parent, false)
    )

    override fun onBindView(holder: BannerViewHolder, data: Banner?, position: Int, size: Int) {
        GlideUtils.setImg(context = context,url = data?.img?:"",imageView = holder.imgBanner)
        holder.tvTitle.text = data?.title
    }
}