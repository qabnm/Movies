package com.duoduovv.cinema.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.cinema.R

/**
 * @author: jun.liu
 * @date: 2021/1/5 : 14:13
 * 热搜页面
 */
class HotSearchAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_hot_search) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tvKeyWord, item)
        val position = holder.layoutPosition + 1
        holder.setText(R.id.tvRank, position.toString())
        when (position) {
            1 -> holder.setTextColor(R.id.tvRank, Color.parseColor("#EF0000"))
            2 -> holder.setTextColor(R.id.tvRank, Color.parseColor("#EF0033"))
            3 -> holder.setTextColor(R.id.tvRank, Color.parseColor("#EF9D00"))
            else -> holder.setTextColor(R.id.tvRank, Color.parseColor("#898989"))
        }
    }
}