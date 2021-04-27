package com.duoduovv.personal.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.personal.R
import com.duoduovv.personal.bean.City

/**
 * @author: jun.liu
 * @date: 2021/4/26 13:40
 * @des:
 */
class CityAdapter :BaseQuickAdapter<City, BaseViewHolder>(R.layout.item_city_select) {
    override fun convert(holder: BaseViewHolder, item: City) {
        holder.setText(R.id.tvCity, item.name)
    }
}