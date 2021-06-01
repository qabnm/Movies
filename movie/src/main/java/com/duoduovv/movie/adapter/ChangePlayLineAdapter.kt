package com.duoduovv.movie.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.LineList

/**
 * @author: jun.liu
 * @date: 2021/6/1 9:06
 * @des:线路切换
 */
class ChangePlayLineAdapter :
    BaseQuickAdapter<LineList, BaseViewHolder>(R.layout.item_line_change) {
    override fun convert(holder: BaseViewHolder, item: LineList) {
        val tvLine: TextView = holder.getView(R.id.tvLine)
        tvLine.setBackgroundResource(if (item.isDefault) R.drawable.shape_radius13_solid0083ff else R.drawable.shape_radius15_solidb3ffffff)
        tvLine.text = item.name
    }
}