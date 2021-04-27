package com.duoduovv.movie.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.RankList
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.StringUtils
import retrofit2.http.POST

/**
 * @author: jun.liu
 * @date: 2021/1/12 13:20
 * @des:榜单
 */
class MovieRankAdapter : BaseQuickAdapter<RankList, BaseViewHolder>(R.layout.item_movie_rank) {
    override fun convert(holder: BaseViewHolder, item: RankList) {
        GlideUtils.setMovieImg(
            context = context,
            url = item.coverUrl,
            imageView = holder.getView(R.id.imgCover)
        )
        val  tvRank:TextView = holder.getView(R.id.tvRank)
        when(holder.layoutPosition){
            0 ->{
                tvRank.setBackgroundResource(R.drawable.movie_rank_first)
                tvRank.text = ""
            }
            1 ->{
                tvRank.setBackgroundResource(R.drawable.movie_rank_second)
                tvRank.text = ""
            }
            2->{
                tvRank.setBackgroundResource(R.drawable.movie_rank_third)
                tvRank.text = ""
            }
            else ->{
                tvRank.setBackgroundResource(R.drawable.movie_rank_other)
                tvRank.text = "${holder.layoutPosition+1}"
            }
        }
        holder.setText(R.id.tvName, item.vodName)
        holder.setText(
            R.id.tvTime,
            "${item.vodYear} | ${item.typeText} | ${item.vodArea} | ${item.vodLang}"
        )
        holder.setGone(R.id.tvMainActor, StringUtils.isEmpty(item.vodActor))
        holder.setText(R.id.tvMainActor, "主演：${item.vodActor}")
        holder.setText(R.id.tvDirector, "导演：${item.vodDirector}")
        holder.setText(R.id.tvScore, item.lastRemark)
    }
}