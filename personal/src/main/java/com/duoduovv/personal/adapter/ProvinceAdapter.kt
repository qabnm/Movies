package com.duoduovv.personal.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.personal.R
import com.duoduovv.personal.bean.CityBeanItem

/**
 * @author: jun.liu
 * @date: 2021/4/26 13:37
 * @des:
 */
class ProvinceAdapter : BaseQuickAdapter<CityBeanItem, BaseViewHolder>(R.layout.item_city_select) {
    override fun convert(holder: BaseViewHolder, item: CityBeanItem) {
        holder.setText(R.id.tvCity, item.name)
    }
}