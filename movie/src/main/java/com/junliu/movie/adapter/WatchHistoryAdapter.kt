package com.junliu.movie.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.junliu.movie.R
import com.junliu.movie.bean.WatchHistoryBean
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/1/11 18:00
 * @des:观看历史
 */
class WatchHistoryAdapter :BaseQuickAdapter<WatchHistoryBean,BaseViewHolder>(R.layout.item_watch_history) {
    private var isEdit = false
    override fun convert(holder: BaseViewHolder, item: WatchHistoryBean) {
        GlideUtils.setImg(context, item.coverUrl, holder.getView(R.id.imgCover))
        holder.setText(R.id.tvName, item.name)
        holder.setText(R.id.tvWhere, item.where)
        holder.setGone(R.id.imgSelect , !isEdit)
        holder.setImageResource(R.id.imgSelect , if (item.isSelect) R.drawable.movie_collection_selected else R.drawable.movie_collection_unselected)
    }

    fun isEdit(isEdit:Boolean){
        this.isEdit = isEdit
    }
}