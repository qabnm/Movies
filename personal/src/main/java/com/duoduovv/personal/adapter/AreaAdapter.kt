package com.duoduovv.personal.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.personal.R

/**
 * @author: jun.liu
 * @date: 2021/4/26 13:41
 * @des:
 */
class AreaAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_city_select) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tvCity, item)
    }
}