package com.duoduovv.movie.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.StagePhotoArray
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/3/10 11:12
 * @des:审核版影视详情的剧照
 */
class MovieDetailForDebugStagePhoto :
    BaseQuickAdapter<StagePhotoArray, BaseViewHolder>(R.layout.item_movie_detail_for_debug_stage_photo) {
    override fun convert(holder: BaseViewHolder, item: StagePhotoArray) {
        GlideUtils.setImg(context, item.Img, holder.getView(R.id.imgCover))
    }
}