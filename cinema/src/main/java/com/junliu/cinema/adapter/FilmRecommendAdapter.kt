package com.junliu.cinema.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.junliu.cinema.R
import com.junliu.cinema.bean.FilmRecommendBean
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.StringUtils

/**
 * @author: jun.liu
 * @date: 2021/1/18 10:40
 * @des:推荐adapter
 */
class FilmRecommendAdapter :
    BaseQuickAdapter<FilmRecommendBean, BaseViewHolder>(R.layout.item_movie_library) {
    override fun convert(holder: BaseViewHolder, item: FilmRecommendBean) {
        GlideUtils.setImg(context, item.cover_url, holder.getView(R.id.imgCover))
        holder.setText(R.id.tvName, StringUtils.getString(item.vod_name))
        holder.setText(R.id.tvScore, StringUtils.getString(item.score))
    }
}