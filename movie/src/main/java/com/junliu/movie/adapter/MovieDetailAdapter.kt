package com.junliu.movie.adapter

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
import com.junliu.movie.R
import com.junliu.movie.bean.MovieDetailBean

/**
 * @author: jun.liu
 * @date: 2021/1/13 18:24
 * @des:影片详情
 */
class MovieDetailAdapter(private val context: Context, private val detailBean: MovieDetailBean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val typeDetail = 1
    private val typeList = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        typeDetail -> DetailViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_movie_detail_top, parent, false),
            context
        )
        else -> ListViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_recyclerview, parent, false), context
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    override fun getItemCount() = detailBean.recommends.size + 1

    override fun getItemViewType(position: Int) = when (position) {
        0 -> typeDetail
        else -> typeList
    }

    class DetailViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        val imgShare: ImageView = itemView.findViewById(R.id.imgShare)
        val imgDownload: ImageView = itemView.findViewById(R.id.imgDownload)
        val imgCollect: ImageView = itemView.findViewById(R.id.imgCollect)
        val imgIcon: ImageView = itemView.findViewById(R.id.imgIcon)
        val tvFrom: TextView = itemView.findViewById(R.id.tvFrom)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvDetail: TextView = itemView.findViewById(R.id.tvDetail)
        val tvScore: TextView = itemView.findViewById(R.id.tvScore)
        val tvType: TextView = itemView.findViewById(R.id.tvType)
        val layoutContainer: ConstraintLayout = itemView.findViewById(R.id.layoutContainer)
        val tvWhere: TextView = itemView.findViewById(R.id.tvWhere)
        val rvList: RecyclerView = itemView.findViewById(R.id.rvList)

        init {
            rvList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    class ListViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        val rvList: RecyclerView = itemView.findViewById(R.id.rvList)

        init {
            rvList.layoutManager = GridLayoutManager(context, 3)
        }
    }

}