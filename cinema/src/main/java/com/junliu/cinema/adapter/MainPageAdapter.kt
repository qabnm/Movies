package com.junliu.cinema.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.junliu.cinema.CinemaContext
import com.junliu.cinema.R
import com.junliu.cinema.bean.MainPageBean
import com.junliu.cinema.bean.MainRecommend
import com.youth.banner.Banner
import com.youth.banner.indicator.CircleIndicator

/**
 * @author: jun.liu
 * @date: 2021/1/15 16:38
 * @des:首页adapter
 */
class MainPageAdapter(
    private val context: Context,
    private val bean: MainPageBean,
    private val dataList: List<MainRecommend>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        CinemaContext.TYPE_BANNER -> BannerViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_main_banner, parent, false), context
        )
        CinemaContext.TYPE_TODAY_RECOMMEND -> TodayRecommendViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_today_reccommend, parent, false)
        )
        CinemaContext.TYPE_ALL_LOOK -> AllLookViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_main_all_look, parent, false)
        )
        CinemaContext.TYPE_RECOMMEND_LIST -> RecommendViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_main_recommend, parent, false)
        )
        else -> RecommendViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_main_recommend, parent, false)
        )
    }

    override fun getItemCount() = if (null != dataList) dataList.size + 3 else 3

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemViewType(position: Int) = when (position) {
        0 -> CinemaContext.TYPE_BANNER
        1 -> CinemaContext.TYPE_TODAY_RECOMMEND
        2 -> CinemaContext.TYPE_ALL_LOOK
        else -> CinemaContext.TYPE_RECOMMEND_LIST
    }

    private fun bindBanner(holder: BannerViewHolder) {
        holder.banner.addBannerLifecycleObserver(context as AppCompatActivity)
            .setAdapter(BannerImgAdapter(bean.banner, context)).indicator =
            CircleIndicator(context)
    }

    private class RecommendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    private class AllLookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    private class TodayRecommendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    private class BannerViewHolder(itemView: View, context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val banner: Banner<com.junliu.cinema.bean.Banner, BannerImgAdapter> =
            itemView.findViewById(R.id.layoutBanner)
        val rvList: RecyclerView = itemView.findViewById(R.id.rvList)

        init {
            rvList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) manager.spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = when (getItemViewType(position)) {
                    CinemaContext.TYPE_BANNER -> 3
                    CinemaContext.TYPE_TODAY_RECOMMEND -> 3
                    CinemaContext.TYPE_ALL_LOOK -> 3
                    CinemaContext.TYPE_RECOMMEND_LIST -> 1
                    else -> 3
                }
            }
    }
}