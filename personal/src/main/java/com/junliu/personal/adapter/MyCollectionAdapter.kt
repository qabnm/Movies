package com.junliu.personal.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.junliu.personal.R
import com.junliu.personal.bean.MyCollectionBean
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/1/11 14:24
 * @des:我的收藏
 */
class MyCollectionAdapter : BaseQuickAdapter<MyCollectionBean, BaseViewHolder>(R.layout.item_my_collection) {
    private var isEdit = false
    override fun convert(holder: BaseViewHolder, item: MyCollectionBean) {
        GlideUtils.setImg(context, item.coverUrl, holder.getView(R.id.imgCover))
        holder.setText(R.id.tvName, item.name)
        holder.setText(R.id.tvWhere, item.where)
        holder.setGone(R.id.imgSelect , !isEdit)
    }

    fun isEdit(isEdit:Boolean){
        this.isEdit = isEdit
    }
}