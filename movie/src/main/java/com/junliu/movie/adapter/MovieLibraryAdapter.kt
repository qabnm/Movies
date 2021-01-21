package com.junliu.movie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.junliu.movie.R
import com.junliu.movie.bean.Filter
import com.junliu.movie.bean.MovieLibList
import com.junliu.movie.view.MovieTopLayout
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/1/13 9:45
 * @des:片库
 */
class MovieLibraryAdapter(
    private val context: Context,
    private val typeList: List<Filter>,
    private val movieList: List<MovieLibList>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_TOP = 1
    private val TYPE_LIIST = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_TOP -> TypeViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_lib_top_layout, parent, false)
        )
        else -> ListViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_movie_library, parent, false)
        )
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            object : SpanSizeLookup() {
                override fun getSpanSize(position: Int) = when (getItemViewType(position)) {
                    TYPE_TOP -> 3
                    else -> 1
                }
            }.also { manager.spanSizeLookup = it }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TypeViewHolder -> {
                holder.layoutContainer.removeAllViews()
                for (i in typeList.indices) {
                    val layoutType = MovieTopLayout(context)
                    layoutType.setList(typeList[i].array)
                    holder.layoutContainer.addView(layoutType)
                }
            }
            is ListViewHolder -> {
                if (position > 0) bindList(holder, movieList[position - 1])
            }
        }
    }

    private fun bindList(holder: ListViewHolder, movieBean: MovieLibList) {
        GlideUtils.setImg(context, movieBean.cover_url, holder.imgCover)
        holder.tvName.text = movieBean.vod_name
        holder.tvScore.text = movieBean.remark
    }

    override fun getItemCount() = movieList.size + 1

    override fun getItemViewType(position: Int) = when (position) {
        0 -> TYPE_TOP
        else -> TYPE_LIIST
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCover: ImageView = itemView.findViewById(R.id.imgCover)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvScore: TextView = itemView.findViewById(R.id.tvScore)
    }

    class TypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layoutContainer: MovieTopLayout = itemView.findViewById(R.id.layoutContainer)
    }
}