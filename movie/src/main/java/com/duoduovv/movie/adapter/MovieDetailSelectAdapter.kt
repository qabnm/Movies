package com.duoduovv.movie.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.movie.R

/**
 * @author: jun.liu
 * @date: 2021/3/11 15:23
 * @des:
 */
class MovieDetailSelectAdapter (list: MutableList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_movie_detail_select, list) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tvEpisodes, item)
    }
}