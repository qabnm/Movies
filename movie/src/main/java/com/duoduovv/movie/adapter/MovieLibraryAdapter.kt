package com.duoduovv.movie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.duoduovv.common.databinding.ItemMovieLibraryBinding
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.Filter
import com.duoduovv.movie.bean.MovieLibList
import com.duoduovv.movie.bean.TypeListArray
import com.duoduovv.movie.databinding.ItemLibTopLayoutBinding
import com.duoduovv.movie.view.MovieTopLayout
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/1/13 9:45
 * @des:片库
 */
class MovieLibraryAdapter(
    private val context: Context,
    private val typeList: List<Filter>,
    private var movieList: List<MovieLibList>?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_TOP = 1
    private val TYPE_LIIST = 2
    private val TYPE_EMPTY = 3
    private var itemClickListener: OnItemClickListener? = null
    private var isUpdate = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_TOP -> {
            val bind = ItemLibTopLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
            TypeViewHolder(bind)
        }
        TYPE_LIIST -> {
            val bind = ItemMovieLibraryBinding.inflate(LayoutInflater.from(context),parent, false)
            ListViewHolder(bind)
        }
        else -> EmptyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_movie_lib_empty, parent, false)
        )
    }

    fun notifyDataChanged(movieList: List<MovieLibList>?) {
        this.movieList = movieList
        isUpdate = true
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            object : SpanSizeLookup() {
                override fun getSpanSize(position: Int) = when (getItemViewType(position)) {
                    TYPE_TOP -> 3
                    TYPE_LIIST -> 1
                    else -> 3
                }
            }.also { manager.spanSizeLookup = it }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TypeViewHolder -> {
                if (isUpdate) return
                holder.bind.layoutContainer.removeAllViews()
//                topBind.layoutContainer.removeAllViews()
                for (i in typeList.indices) {
                    val layoutType = MovieTopLayout(context)
                    layoutType.setOnTypeClickListener(TypeClickListener())
                    layoutType.setList(
                        typeList[i].array as ArrayList<TypeListArray>,
                        typeList[i].key
                    )
                    holder.bind.layoutContainer.addView(layoutType)
                }
            }
            is ListViewHolder -> {
                if (position > 0) bindList(holder,movieList!![position - 1])
            }
        }
    }

    /**
     * 筛选条件点击
     */
    private inner class TypeClickListener : MovieTopLayout.OnTypeClickListener {
        override fun onTypeClick(key: String, name: String) {
            itemClickListener?.onTypeClick(key, name)
        }
    }

    interface OnItemClickListener {
        fun onTypeClick(key: String, name: String)

        fun onMovieClick(movieId: String, way: String)
    }

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    private fun bindList(holder:ListViewHolder,movieBean: MovieLibList) {
        GlideUtils.setMovieImg(context, movieBean.coverUrl, holder.bind.imgCover)
        holder.bind.tvName.text = movieBean.vodName
        holder.bind.tvScore.text = movieBean.lastRemark
        holder.bind.layoutContainer.setOnClickListener {
            itemClickListener?.onMovieClick(
                movieBean.strId,
                movieBean.way
            )
        }
    }

    override fun getItemCount() = if (movieList?.isNotEmpty() == true) {
        movieList!!.size + 1
    } else 2

    override fun getItemViewType(position: Int) = when (position) {
        0 -> TYPE_TOP
        else -> if (movieList?.isNotEmpty() == true) TYPE_LIIST else TYPE_EMPTY
    }

    private class ListViewHolder(val bind: ItemMovieLibraryBinding) : RecyclerView.ViewHolder(bind.root)

    private class TypeViewHolder(val bind:ItemLibTopLayoutBinding) : RecyclerView.ViewHolder(bind.root)

    private class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}