package com.junliu.personal.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.junliu.personal.R
import com.junliu.personal.bean.FavoriteBean
import com.junliu.personal.bean.MyCollectionBean
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/1/11 14:24
 * @des:我的收藏
 */
class MyCollectionAdapter :
    BaseQuickAdapter<FavoriteBean, BaseViewHolder>(R.layout.item_my_collection) {
    private var isEdit = false
    override fun convert(holder: BaseViewHolder, item: FavoriteBean) {
        GlideUtils.setImg(context, item.cover_url, holder.getView(R.id.imgCover))
        holder.setText(R.id.tvName, item.vod_name)
        holder.setText(R.id.tvWhere, item.last_remark)
        holder.setGone(R.id.imgSelect, !isEdit)
        holder.setImageResource(
            R.id.imgSelect,
            if (item.isSelect) R.drawable.personal_collection_selected else R.drawable.personal_collection_unselected
        )
    }

    fun isEdit(isEdit: Boolean) {
        this.isEdit = isEdit
    }
}