package com.duoduovv.movie.adapter

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.MovieItem

/**
 * @author: jun.liu
 * @date: 2021/3/31 17:34
 * @des:
 */
class MovieEpisodesArtAdapter (list: MutableList<MovieItem>) :
    BaseQuickAdapter<MovieItem, BaseViewHolder>(R.layout.item_art_more_select, list) {
    override fun convert(holder: BaseViewHolder, item: MovieItem) {
        val tvTitle: TextView = holder.getView(R.id.tvTitle)
        tvTitle.text = item.title
        tvTitle.setTextColor(
            if (item.isSelect) ContextCompat.getColor(
                context, R.color.color009CFF
            ) else ContextCompat.getColor(context, R.color.color000000)
        )
    }
}