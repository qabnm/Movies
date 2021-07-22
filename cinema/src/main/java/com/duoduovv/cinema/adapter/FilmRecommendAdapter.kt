package com.duoduovv.cinema.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.cinema.R
import com.duoduovv.cinema.bean.MovieMoreList
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.StringUtils

/**
 * @author: jun.liu
 * @date: 2021/1/18 10:40
 * @des:今日推荐adapter
 */
class FilmRecommendAdapter:
    BaseQuickAdapter<MovieMoreList, BaseViewHolder>(R.layout.item_movie_more) {
    override fun convert(holder: BaseViewHolder, item: MovieMoreList) {
        GlideUtils.setMovieImg(context, item.coverUrl, holder.getView(R.id.imgCover))
        holder.setText(R.id.tvName, StringUtils.getString(item.vodName))
        holder.setText(R.id.tvScore, StringUtils.getString(item.remark))
    }
}