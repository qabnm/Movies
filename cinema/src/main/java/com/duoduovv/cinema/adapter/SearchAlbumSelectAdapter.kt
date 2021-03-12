package com.duoduovv.cinema.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.cinema.R
import com.duoduovv.cinema.bean.MovieItem

/**
 * @author: jun.liu
 * @date: 2021/3/12 13:59
 * @des:搜索结果 专辑选集
 */
class SearchAlbumSelectAdapter(data: MutableList<MovieItem>) :
    BaseQuickAdapter<MovieItem, BaseViewHolder>(R.layout.item_search_album_select, data) {
    override fun convert(holder: BaseViewHolder, item: MovieItem) {
        holder.setText(R.id.tvContent, item.title)
    }
}