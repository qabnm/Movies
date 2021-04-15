package com.duoduovv.movie.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.movie.MovieContext
import com.duoduovv.movie.R
import com.duoduovv.room.domain.VideoWatchHistoryBean
import dc.android.bridge.BridgeContext.Companion.TYPE_ALBUM
import dc.android.bridge.BridgeContext.Companion.TYPE_TV
import dc.android.bridge.BridgeContext.Companion.TYPE_TV0
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.StringUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: jun.liu
 * @date: 2021/1/11 18:00
 * @des:观看历史
 */
class WatchHistoryAdapter :
    BaseQuickAdapter<VideoWatchHistoryBean, BaseViewHolder>(R.layout.item_watch_history) {
    private var isEdit = false
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
    override fun convert(holder: BaseViewHolder, item: VideoWatchHistoryBean) {
        GlideUtils.setMovieImg(context, item.coverUrl, holder.getView(R.id.imgCover))
        holder.setText(R.id.tvName, item.title)
        val tvWhere: TextView = holder.getView(R.id.tvWhere)
        when (item.type) {
            TYPE_TV, TYPE_TV0 -> {
                tvWhere.text = "观看到${item.vidTitle}集"
            }
            TYPE_ALBUM -> {
                tvWhere.text = "观看到${item.vidTitle}"
            }
            else -> {
                tvWhere.text = "观看到${StringUtils.getDifferTime(item.currentLength)}"
            }
        }
        holder.setText(R.id.tvLastTime, "最后观看：${sdf.format(Date(item.currentTime))}")
        holder.setGone(R.id.imgSelect, !isEdit)
        holder.setImageResource(
            R.id.imgSelect,
            if (item.isSelect) R.drawable.movie_collection_selected else R.drawable.movie_collection_unselected
        )
    }

    fun isEdit(isEdit: Boolean) {
        this.isEdit = isEdit
    }
}