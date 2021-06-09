package com.duoduovv.movie.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.DetailRecommend
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/1/13 18:24
 * @des:影片详情
 */
class MovieDetailAdapter:BaseQuickAdapter<DetailRecommend, BaseViewHolder>(R.layout.item_movie_recommend){
    override fun convert(holder: BaseViewHolder, item: DetailRecommend) {
        GlideUtils.setMovieImg(context, item.coverUrl,holder.getView(R.id.imgCover))
        holder.setText(R.id.tvName,item.vodName)
        holder.setText(R.id.tvScore,item.lastRemark)
    }
}