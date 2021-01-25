package com.junliu.movie.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.junliu.movie.R
import com.junliu.movie.bean.MovieRankBean
import com.junliu.movie.bean.RankList
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.StringUtils

/**
 * @author: jun.liu
 * @date: 2021/1/12 13:20
 * @des:榜单
 */
class MovieRankAdapter : BaseQuickAdapter<RankList, BaseViewHolder>(R.layout.item_movie_rank) {
    override fun convert(holder: BaseViewHolder, item: RankList) {
        GlideUtils.setImg(
            context = context,
            url = item.cover_url,
            imageView = holder.getView(R.id.imgCover)
        )
        holder.setText(R.id.tvRank, "${holder.layoutPosition + 1}")
        holder.setText(R.id.tvName, item.vod_name)
        holder.setText(
            R.id.tvTime,
            "${item.vod_year} | ${item.type_text} | ${item.vod_area_text} | ${item.vod_lang}"
        )
        holder.setGone(R.id.tvMainActor, StringUtils.isEmpty(item.vod_actor))
        holder.setText(R.id.tvMainActor, "主演：${item.vod_actor}")
        holder.setText(R.id.tvDirector, "导演：${item.vod_director}")
        holder.setText(R.id.tvScore, item.remark)
    }
}