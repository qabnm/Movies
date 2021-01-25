package com.junliu.cinema.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.junliu.cinema.R
import com.junliu.cinema.bean.SearchResultList
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.StringUtils

/**
 * @author: jun.liu
 * @date: 2021/1/11 10:03
 * @des：搜索结果页
 */
class SearchResultListAdapter :
    BaseQuickAdapter<SearchResultList, BaseViewHolder>(R.layout.item_search_result) {
    override fun convert(holder: BaseViewHolder, item: SearchResultList) {
        GlideUtils.setImg(context, item.cover_url, holder.getView(R.id.imgCover))
        holder.setText(R.id.tvTitle, item.vod_name)
        holder.setText(
            R.id.tvTime,
            StringUtils.append(
                item.vod_year,
                " ",
                item.type_text,
                " ",
                item.vod_area_text,
                " ",
                item.vod_lang
            )
        )
        holder.setText(R.id.tvDirector, StringUtils.append("导演：", item.vod_director))
        val length = item.movie_items.size
        hideAllBtn(holder, length > 6)
        when (length) {
            1 -> {
                setTotal1(holder)
            }
            2 -> {
                setTotal2(holder)
            }
            3 -> {
                setTotal3(holder)
            }
            4 -> {
                setTotal4(holder)
            }
            5 -> {
                setTotal5(holder)
            }
            6 -> {
                setTotal6(holder)
            }
            else -> {
                //大于6剧集
                holder.setText(R.id.tv1, "1")
                holder.setText(R.id.tv2, "2")
                holder.setText(R.id.tv3, "...")
                holder.setText(R.id.tv4, "${length - 2}")
                holder.setText(R.id.tv5, "${length - 1}")
                holder.setText(R.id.tv6, "$length")
            }
        }
    }

    private fun hideAllBtn(holder: BaseViewHolder, hide: Boolean) {
        holder.setVisible(R.id.tv1, hide)
        holder.setVisible(R.id.tv2, hide)
        holder.setVisible(R.id.tv3, hide)
        holder.setVisible(R.id.tv4, hide)
        holder.setVisible(R.id.tv5, hide)
        holder.setVisible(R.id.tv6, hide)
    }

    private fun setTotal6(holder: BaseViewHolder) {
        setTotal5(holder)
        holder.setVisible(R.id.tv6, true)
        holder.setText(R.id.tv6, "6")
    }

    private fun setTotal5(holder: BaseViewHolder) {
        setTotal4(holder)
        holder.setVisible(R.id.tv5, true)
        holder.setText(R.id.tv5, "5")
    }

    private fun setTotal4(holder: BaseViewHolder) {
        setTotal3(holder)
        holder.setVisible(R.id.tv4, true)
        holder.setText(R.id.tv4, "4")
    }

    private fun setTotal3(holder: BaseViewHolder) {
        setTotal2(holder)
        holder.setVisible(R.id.tv3, true)
        holder.setText(R.id.tv3, "3")
    }

    private fun setTotal2(holder: BaseViewHolder) {
        setTotal1(holder)
        holder.setVisible(R.id.tv2, true)
        holder.setText(R.id.tv2, "2")
    }

    private fun setTotal1(holder: BaseViewHolder) {
        holder.setVisible(R.id.tv1, true)
        holder.setText(R.id.tv1, "1")
    }
}