package com.duoduovv.cinema.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.cinema.R
import com.duoduovv.cinema.bean.FilmRecommendBean
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/1/18 11:41
 * @des:大家都在看
 */
class FilmAllLookAdapter(data: List<FilmRecommendBean>?) :
    BaseQuickAdapter<FilmRecommendBean, BaseViewHolder>(
        R.layout.item_all_look,
        data as MutableList<FilmRecommendBean>?
    ) {
    override fun convert(holder: BaseViewHolder, item: FilmRecommendBean) {
        GlideUtils.setImg(context, item.cover_url, holder.getView(R.id.imgCover))
        holder.setText(R.id.tvName, item.vod_name)
    }
}