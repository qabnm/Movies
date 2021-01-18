package com.junliu.cinema.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.junliu.cinema.R
import com.junliu.cinema.bean.Category
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/1/18 10:00
 * @des:首页分类
 */
class MainCategoryAdapter(data:MutableList<Category>) : BaseQuickAdapter<Category, BaseViewHolder>(R.layout.item_catagory,data){
    override fun convert(holder: BaseViewHolder, item: Category) {
        GlideUtils.setImg(context, item.img, holder.getView(R.id.imgIcon))
        holder.setText(R.id.tvCategory, item.name)
    }
}