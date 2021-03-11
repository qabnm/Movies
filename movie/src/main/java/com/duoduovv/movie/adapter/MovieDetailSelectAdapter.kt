package com.duoduovv.movie.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.MovieItem

/**
 * @author: jun.liu
 * @date: 2021/3/11 15:23
 * @des:
 */
class MovieDetailSelectAdapter (list: MutableList<MovieItem>) :
    BaseQuickAdapter<MovieItem, BaseViewHolder>(R.layout.adapter_movie_detail_select, list) {
    override fun convert(holder: BaseViewHolder, item: MovieItem) {
        holder.setText(R.id.tvEpisodes, item.title)
    }
}