package com.duoduovv.movie.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.SubjectDetailListBean
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.StringUtils

/**
 * @author: jun.liu
 * @date: 2021/6/25 16:21
 * @des:专题详情页
 */
class MovieSubjectDetailAdapter :
    BaseQuickAdapter<SubjectDetailListBean, BaseViewHolder>(R.layout.item_subject_detail) {
    override fun convert(holder: BaseViewHolder, item: SubjectDetailListBean) {
        holder.setGone(R.id.vTop, holder.layoutPosition != 0)
        GlideUtils.setMovieImg(context, item.coverUrl, holder.getView(R.id.imgCover))
        holder.setText(R.id.tvName, item.vodName)
        holder.setText(R.id.tvScore, item.remark)
        holder.setText(
            R.id.tvTime,
            "${item.vodYear} | ${item.typeText} | ${item.vodArea} | ${item.vodLang}"
        )
        holder.setGone(R.id.tvMainActor, StringUtils.isEmpty(item.vodActor))
        holder.setText(R.id.tvMainActor, "主演：${item.vodActor}")
        holder.setText(R.id.tvDirector, "导演：${item.vodDirector}")
    }
}