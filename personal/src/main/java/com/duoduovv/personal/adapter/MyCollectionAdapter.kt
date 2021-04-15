package com.duoduovv.personal.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.personal.R
import com.duoduovv.personal.bean.FavoriteBean
import com.duoduovv.room.domain.CollectionBean
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.StringUtils

/**
 * @author: jun.liu
 * @date: 2021/1/11 14:24
 * @des:我的收藏
 */
class MyCollectionAdapter :
    BaseQuickAdapter<CollectionBean, BaseViewHolder>(R.layout.item_my_collection) {
    private var isEdit = false
    override fun convert(holder: BaseViewHolder, item: CollectionBean) {
        GlideUtils.setMovieImg(context, item.coverUrl, holder.getView(R.id.imgCover))
        holder.setText(R.id.tvName, item.movieName)
        val tvWhere:TextView = holder.getView(R.id.tvWhere)
        if (!StringUtils.isEmpty(item.direcotor)){
            tvWhere.text = "导演：${item.direcotor}"
        }else{
            tvWhere.text = "主演：${item.actor}"
        }
        holder.setGone(R.id.imgSelect, !isEdit)
        holder.setImageResource(
            R.id.imgSelect,
            if (item.isCollect) R.drawable.personal_collection_selected else R.drawable.personal_collection_unselected
        )
    }

    fun isEdit(isEdit: Boolean) {
        this.isEdit = isEdit
    }
}