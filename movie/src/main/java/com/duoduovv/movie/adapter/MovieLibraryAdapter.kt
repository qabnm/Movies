package com.duoduovv.movie.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.duoduovv.advert.gdtad.GDTInfoAdForSelfRender
import com.duoduovv.advert.ttad.TTInfoAd
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.databinding.ItemMovieLibraryBinding
import com.duoduovv.common.databinding.ItemTypeAdBinding
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.Filter
import com.duoduovv.movie.bean.MovieLibList
import com.duoduovv.movie.bean.TypeListArray
import com.duoduovv.movie.databinding.ItemLibTopLayoutBinding
import com.duoduovv.movie.view.MovieTopLayout
import dc.android.bridge.BridgeContext.Companion.TYPE_GDT_AD
import dc.android.bridge.BridgeContext.Companion.TYPE_TT_AD
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.OsUtils

/**
 * @author: jun.liu
 * @date: 2021/1/13 9:45
 * @des:片库
 */
class MovieLibraryAdapter(
    private val context: Context,
    private val typeList: List<Filter>,
    private var movieList: List<MovieLibList>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_TOP = 1
    private val TYPE_LIIST = 2
    private val TYPE_EMPTY = 3
    private val TYPE_AD = 4
    private var itemClickListener: OnItemClickListener? = null
    private var isUpdate = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_TOP -> {
            val bind = ItemLibTopLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            TypeViewHolder(bind)
        }
        TYPE_LIIST -> {
            val bind = ItemMovieLibraryBinding.inflate(LayoutInflater.from(context), parent, false)
            ListViewHolder(bind)
        }
        TYPE_EMPTY -> EmptyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_movie_lib_empty, parent, false)
        )
        else -> {
            val bind = ItemTypeAdBinding.inflate(LayoutInflater.from(context), parent, false)
            AdViewHolder(bind)
        }
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
                for (i in typeList.indices) {
                    val layoutType = MovieTopLayout(context)
                    layoutType.setOnTypeClickListener(TypeClickListener())
                    layoutType.setList(typeList[i].array as ArrayList<TypeListArray>, typeList[i].key)
                    holder.bind.layoutContainer.addView(layoutType)
                }
            }
            is ListViewHolder -> {
                if (position > 0) bindList(holder, movieList!![position - 1])
            }
            is EmptyViewHolder ->{}
            is AdViewHolder -> {
                Log.d("adLoad1","itemId:${holder.itemId}position:${holder.adapterPosition}")
                if (position > 0) initAd(holder,position-1)
            }
        }
    }

    fun onDestroy(){
        ttAd?.destroyInfoAd()
        gdtAd?.onDestroy()
    }

    private var ttAd:TTInfoAd?=null
    private var gdtAd:GDTInfoAdForSelfRender?=null
    /**
     * 广告类型的item
     * @param holder AdViewHolder
     */
    private fun initAd(holder: AdViewHolder,position: Int) {
        if (!movieList!![position].hasLoad) {
            movieList!![position].hasLoad = true
            BaseApplication.configBean?.ad?.libraryAd?.let {
                when (it.type) {
                    TYPE_TT_AD -> {
                        holder.bind.layoutTTAd.visibility = View.VISIBLE
                        holder.bind.layoutGdt.visibility = View.GONE
                        if (null == ttAd) ttAd = TTInfoAd()
                        val width = OsUtils.px2dip(context, OsUtils.getScreenWidth(context).toFloat()) - 24
                        ttAd?.initTTInfoAd(context as Activity, it.value, width.toFloat(), 0f, holder.bind.layoutTTAd)
                    }
                    TYPE_GDT_AD -> {
                        holder.bind.layoutTTAd.visibility = View.GONE
                        holder.bind.layoutGdt.visibility = View.VISIBLE
                        if (null == gdtAd) gdtAd = GDTInfoAdForSelfRender()
                        gdtAd?.initInfoAd(context, it.value, holder.bind.adImgCover, holder.bind.mediaView, holder.bind.layoutGdt)
                    }
                    else -> { }
                }
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

    private fun bindList(holder: ListViewHolder, movieBean: MovieLibList) {
        GlideUtils.setMovieImg(context, movieBean.coverUrl, holder.bind.imgCover)
        holder.bind.tvName.text = movieBean.vodName
        holder.bind.tvScore.text = movieBean.lastRemark
        holder.bind.layoutContainer.setOnClickListener {
            itemClickListener?.onMovieClick(movieBean.strId, movieBean.way)
        }
    }

    override fun getItemCount() = if (movieList?.isNotEmpty() == true) {
        movieList!!.size + 1
    } else 2

    override fun getItemViewType(position: Int) = when (position) {
        0 -> TYPE_TOP
        else -> {
            if (movieList?.isNotEmpty() == true) {
                if (movieList!![position - 1].itemType == "ad") {
                    position
                } else {
                    TYPE_LIIST
                }
            } else {
                TYPE_EMPTY
            }
        }
    }

    private class ListViewHolder(val bind: ItemMovieLibraryBinding) :
        RecyclerView.ViewHolder(bind.root)

    private class TypeViewHolder(val bind: ItemLibTopLayoutBinding) :
        RecyclerView.ViewHolder(bind.root)

    private class AdViewHolder(val bind: ItemTypeAdBinding) : RecyclerView.ViewHolder(bind.root)

    private class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}