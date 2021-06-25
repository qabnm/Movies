package com.duoduovv.movie.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.SubjectListBean
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/6/25 11:07
 * @des:专题列表
 */
class MovieSubjectListAdapter :
    BaseQuickAdapter<SubjectListBean, BaseViewHolder>(R.layout.item_movie_subject_list) {

    override fun convert(holder: BaseViewHolder, item: SubjectListBean) {
        GlideUtils.setMovieImg(
            context,
            item.coverUrl,
            holder.getView(R.id.imgCover),
            R.drawable.banner_df
        )
        holder.setText(R.id.tvTitle, item.title)
    }
}