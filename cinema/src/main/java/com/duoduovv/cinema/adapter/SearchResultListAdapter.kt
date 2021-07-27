package com.duoduovv.cinema.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duoduovv.cinema.R
import com.duoduovv.cinema.bean.MovieItem
import com.duoduovv.cinema.bean.SearchResultList
import com.duoduovv.cinema.databinding.ItemSearchResultBinding
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.TYPE_ALBUM
import dc.android.bridge.BridgeContext.Companion.TYPE_TV
import dc.android.bridge.BridgeContext.Companion.TYPE_TV0
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.StringUtils

/**
 * @author: jun.liu
 * @date: 2021/1/11 10:03
 * @des：搜索结果页
 */
class SearchResultListAdapter(
    private var dataList: List<SearchResultList>,
    private val context: Context,
    private val listener: OnItemClickListener? = null
) : RecyclerView.Adapter<SearchResultListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val mBind = ItemSearchResultBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(mBind)
    }

    fun notifyDataChanged(dataList: List<SearchResultList>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bean = dataList[position]
        if (bean.way == BridgeContext.WAY_VERIFY) {
            holder.mBind.tvPlay.visibility = View.INVISIBLE
        }
        GlideUtils.setMovieImg(context, bean.coverUrl, holder.mBind.imgCover)
        holder.mBind.tvTitle.text = bean.vodName
        holder.mBind.tvScore.text = bean.remark
        holder.mBind.tvTime.text = StringUtils.append(
            bean.vodYear,
            " ", bean.typeText,
            " ", bean.vodArea,
            " ", bean.vodLang
        )
        holder.mBind.tvDirector.text = "导演：${bean.vodDirector}"
        when (bean.movieFlag) {
            TYPE_TV, TYPE_TV0 -> {
                //电视
                holder.mBind.rvList.visibility = View.VISIBLE
                holder.mBind.rvList.layoutManager = GridLayoutManager(context, 6)
                val data = bean.movieItems
                if (data?.isNotEmpty() == true) {
                    val adapter = if (data.size <= 6) {
                        SearchTvSelectAdapter(data as MutableList<MovieItem>)
                    } else {
                        val tempData = ArrayList<MovieItem>()
                        tempData.add(data[0])
                        tempData.add(data[1])
                        tempData.add(MovieItem("", "..."))
                        tempData.add(data[data.size - 3])
                        tempData.add(data[data.size - 2])
                        tempData.add(data[data.size - 1])
                        SearchTvSelectAdapter(tempData)
                    }
                    holder.mBind.rvList.adapter = adapter
                    adapter.setOnItemClickListener { ad, _, pos ->
                        val dataList = (ad as SearchTvSelectAdapter).data
                        if (data.size <= 6) {
                            listener?.onSelectClick(dataList[pos].vid, bean.strId, bean.way)
                        } else {
                            if (pos != 2) {
                                listener?.onSelectClick(dataList[pos].vid, bean.strId, bean.way)
                            } else {
                                listener?.onMoreSelectClick(
                                    data,
                                    bean.strId,
                                    bean.vodName,
                                    bean.way,
                                    bean.movieFlag
                                )
                            }
                        }
                    }
                }
            }
            TYPE_ALBUM -> {
                //综艺
                holder.mBind.rvList.visibility = View.VISIBLE
                holder.mBind.rvList.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                val data = bean.movieItems
                if (data?.isNotEmpty() == true) {
                    val adapter = if (data.size <= 6) {
                        SearchAlbumSelectAdapter(data as MutableList<MovieItem>)
                    } else {
                        val tempData = ArrayList<MovieItem>()
                        tempData.add(data[0])
                        tempData.add(data[1])
                        tempData.add(MovieItem("", "..."))
                        tempData.add(data[data.size - 3])
                        tempData.add(data[data.size - 2])
                        tempData.add(data[data.size - 1])
                        SearchAlbumSelectAdapter(tempData)
                    }
                    holder.mBind.rvList.adapter = adapter
                    adapter.setOnItemClickListener { ad, _, pos ->
                        val dataList = (ad as SearchAlbumSelectAdapter).data
                        if (data.size <= 6) {
                            listener?.onSelectClick(dataList[pos].vid, bean.strId, bean.way)
                        } else {
                            if (pos != 2) {
                                listener?.onSelectClick(dataList[pos].vid, bean.strId, bean.way)
                            } else {
                                listener?.onMoreSelectClick(
                                    data,
                                    bean.strId,
                                    bean.vodName,
                                    bean.way,
                                    bean.movieFlag
                                )
                            }
                        }
                    }
                }
            }
            else -> {
                holder.mBind.rvList.visibility = View.GONE
            }
        }
        holder.mBind.layoutContainer.setOnClickListener { listener?.onItemClick(bean.strId, bean.way) }
    }

    override fun getItemCount() = dataList.size

    class MyViewHolder(val mBind:ItemSearchResultBinding) : RecyclerView.ViewHolder(mBind.root)

    interface OnItemClickListener {
        fun onItemClick(movieId: String, way: String)
        fun onSelectClick(vid: String, movieId: String, way: String)
        fun onMoreSelectClick(
            dataList: List<MovieItem>,
            movieId: String,
            title: String,
            way: String,
            movieFlag: String
        )
    }
}
