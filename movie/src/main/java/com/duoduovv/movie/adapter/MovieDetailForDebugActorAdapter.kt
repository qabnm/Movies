package com.duoduovv.movie.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.ActorArray
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/3/10 11:01
 * @des:审核版的演员表
 */
class MovieDetailForDebugActorAdapter :BaseQuickAdapter<ActorArray,BaseViewHolder>(R.layout.item_movie_detail_for_debug) {
    override fun convert(holder: BaseViewHolder, item: ActorArray) {
        GlideUtils.setImg(context, item.Img, holder.getView(R.id.imgCover))
        holder.setText(R.id.tvName, item.Name)
    }
}