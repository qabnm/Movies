package com.duoduovv.cinema.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duoduovv.cinema.R
import com.duoduovv.cinema.bean.MovieItem
import com.duoduovv.cinema.bean.SearchResultList
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false), context
    )

    fun notifyDataChanged(dataList: List<SearchResultList>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bean = dataList[position]
        GlideUtils.setMovieImg(context, bean.cover_url, holder.imgCover)
        holder.tvTitle.text = bean.vod_name
        holder.tvTime.text = StringUtils.append(
            bean.vod_year,
            " ", bean.type_id_text,
            " ", bean.vod_area_text,
            " ", bean.vod_lang
        )
        holder.tvDirector.text = "导演：${bean.vod_director}"
        when (bean.movie_flag) {
            1 -> {
                //电影
                holder.rvListAlbum.visibility = View.GONE
                holder.rvListTv.visibility = View.GONE
            }
            2 -> {
                //电视
                holder.rvListAlbum.visibility = View.GONE
                holder.rvListTv.visibility = View.VISIBLE
                val data = bean.movie_items
                if (data?.isNotEmpty() == true) {
                    val adapter = if (bean.vod_number <= 6) {
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
                    holder.rvListTv.adapter = adapter
                    adapter.setOnItemClickListener { ad, _, pos ->
                        val dataList = (ad as SearchTvSelectAdapter).data
//                        for (i in dataList.indices) {
//                            dataList[i].isSelect = false
//                        }
//                        dataList[pos].isSelect = true
//                        ad.notifyDataSetChanged()
//                        listener?.onSelectClick(dataList[pos].vid)
                        if (data.size <= 6) {
                            listener?.onSelectClick(dataList[pos].vid, bean.str_id,bean.way)
                        } else {
                            if (pos != 2){
                                listener?.onSelectClick(dataList[pos].vid, bean.str_id, bean.way)
                            }else{
                                listener?.onMoreSelectClick(data, bean.str_id,bean.vod_name, bean.way)
                            }
                        }
                    }
                }
            }
            else -> {
                //综艺
                holder.rvListAlbum.visibility = View.VISIBLE
                holder.rvListTv.visibility = View.GONE
                val data = bean.movie_items
                if (data?.isNotEmpty() == true) {
                    val adapter = if (bean.vod_number <= 6) {
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
                    holder.rvListAlbum.adapter = adapter
                    adapter.setOnItemClickListener { ad, _, pos ->
                        val dataList = (ad as SearchAlbumSelectAdapter).data
                        for (i in dataList.indices) dataList[i].isSelect = false
                        dataList[pos].isSelect = true
                        ad.notifyDataSetChanged()
                        listener?.onSelectClick(dataList[pos].vid, bean.str_id, bean.way)
                    }
                }
            }
        }
        holder.layoutContainer.setOnClickListener { listener?.onItemClick(bean.str_id, bean.way) }
    }

    override fun getItemCount() = dataList.size

    class MyViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        val imgCover: ImageView = itemView.findViewById(R.id.imgCover)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvDirector: TextView = itemView.findViewById(R.id.tvDirector)
        val tvPlay: TextView = itemView.findViewById(R.id.tvPlay)
        val rvListTv: RecyclerView = itemView.findViewById(R.id.rvListTv)
        val rvListAlbum: RecyclerView = itemView.findViewById(R.id.rvListAlbum)
        val layoutContainer: ConstraintLayout = itemView.findViewById(R.id.layoutContainer)

        init {
            rvListTv.layoutManager = GridLayoutManager(context, 6)
            rvListTv.setHasFixedSize(true)
            rvListAlbum.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(movieId: String,way:Int)
        fun onSelectClick(vid: String,movieId: String, way: Int)
        fun onMoreSelectClick(dataList:List<MovieItem>,movieId: String, title:String,way: Int)
    }
}
