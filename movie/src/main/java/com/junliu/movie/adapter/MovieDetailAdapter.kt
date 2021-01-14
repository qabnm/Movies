package com.junliu.movie.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.junliu.movie.bean.MovieDetailBean

/**
 * @author: jun.liu
 * @date: 2021/1/13 18:24
 * @des:影片详情
 */
class MovieDetailAdapter(private val context: Context, private val detailBean:MovieDetailBean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val typeDetail = 1
    private val typeList = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
}