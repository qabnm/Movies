package com.junliu.movie.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.junliu.movie.R

/**
 * @author: jun.liu
 * @date: 2021/2/24 11:00
 * @des:影视详情选集
 */
class MovieEpisodesAdapter(list: MutableList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_movie_episodes, list) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tvEpisodes, item)
    }
}