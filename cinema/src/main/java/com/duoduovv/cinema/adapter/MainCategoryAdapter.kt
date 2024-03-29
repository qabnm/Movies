package com.duoduovv.cinema.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.cinema.R
import com.duoduovv.cinema.bean.Category
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/1/18 10:00
 * @des:首页分类
 */
class MainCategoryAdapter :
    BaseQuickAdapter<Category, BaseViewHolder>(R.layout.item_catagory) {
    override fun convert(holder: BaseViewHolder, item: Category) {
        GlideUtils.setImg(context, item.icon, holder.getView(R.id.imgIcon))
        holder.setText(R.id.tvCategory, item.name)
    }
}