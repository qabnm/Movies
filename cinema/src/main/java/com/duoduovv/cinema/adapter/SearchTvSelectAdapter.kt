package com.duoduovv.cinema.adapter

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.cinema.R
import com.duoduovv.cinema.bean.MovieItem

/**
 * @author: jun.liu
 * @date: 2021/3/12 13:50
 * @des:搜索结果电视剧选集显示
 */
class SearchTvSelectAdapter (list: MutableList<MovieItem>) :
    BaseQuickAdapter<MovieItem, BaseViewHolder>(R.layout.item_search_tv_select, list) {
    override fun convert(holder: BaseViewHolder, item: MovieItem) {
        val tvTitle: TextView = holder.getView(R.id.tvEpisodes)
        tvTitle.text = item.title
        tvTitle.setTextColor(
            if (item.isSelect) ContextCompat.getColor(
                context, R.color.color009CFF
            ) else ContextCompat.getColor(context, R.color.color000000)
        )
    }
}