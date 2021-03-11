package com.duoduovv.movie.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.MovieItem

/**
 * @author: jun.liu
 * @date: 2021/3/8 17:27
 * @des:专辑播放列表
 */
class MovieAlbumAdapter(data: MutableList<MovieItem>) :
    BaseQuickAdapter<MovieItem, BaseViewHolder>(R.layout.layout_movie_zhuanji, data) {
    override fun convert(holder: BaseViewHolder, item: MovieItem) {
        holder.setText(R.id.tvContent, item.title)
    }
}