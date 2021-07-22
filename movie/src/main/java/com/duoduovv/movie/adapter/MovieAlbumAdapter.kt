package com.duoduovv.movie.adapter

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.MovieItem

/**
 * @author: jun.liu
 * @date: 2021/3/8 17:27
 * @des:专辑播放列表
 */
class MovieAlbumAdapter: BaseQuickAdapter<MovieItem, BaseViewHolder>(R.layout.layout_movie_zhuanji) {
    override fun convert(holder: BaseViewHolder, item: MovieItem) {
        val tvContent: TextView = holder.getView(R.id.tvContent)
        tvContent.text = item.title
        tvContent.setTextColor(
            if (item.isSelect) ContextCompat.getColor(
                context, R.color.color009CFF
            ) else ContextCompat.getColor(context, R.color.color000000)
        )
    }
}