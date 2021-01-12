package com.junliu.movie.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.junliu.movie.R
import com.junliu.movie.bean.MovieRankBean
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.StringUtils

/**
 * @author: jun.liu
 * @date: 2021/1/12 13:20
 * @des:榜单
 */
class MovieRankAdapter : BaseQuickAdapter<MovieRankBean, BaseViewHolder>(R.layout.item_movie_rank) {
    override fun convert(holder: BaseViewHolder, item: MovieRankBean) {
        GlideUtils.setImg(
            context = context,
            url = item.coverUrl,
            imageView = holder.getView(R.id.imgCover)
        )
        holder.setText(R.id.tvRank, "${holder.layoutPosition + 1}")
        holder.setText(R.id.tvName, item.name)
        holder.setText(
            R.id.tvTime,
            "${item.year} | ${item.type} | ${item.county} | ${item.language}"
        )
        holder.setGone(R.id.tvMainActor, StringUtils.isEmpty(item.mainActor))
        holder.setText(R.id.tvMainActor, "主演：${item.mainActor}")
        holder.setText(R.id.tvDirector, "导演：${item.director}")
        holder.setText(R.id.tvScore, item.score)
    }
}