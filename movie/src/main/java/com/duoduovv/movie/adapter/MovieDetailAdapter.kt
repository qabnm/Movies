package com.duoduovv.movie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.MovieDetail
import com.duoduovv.movie.bean.MovieDetailBean
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/1/13 18:24
 * @des:影片详情
 */
class MovieDetailAdapter(private val context: Context, private val detailBean: MovieDetailBean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val typeDetail = 1
    private val typeList = 2
    private var listener: OnViewClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        typeDetail -> DetailViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_movie_detail_top, parent, false),
            context
        )
        else -> ListViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_movie_recommend, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DetailViewHolder) bindDetail(holder)
        if (holder is ListViewHolder && position > 0) bindList(holder, position - 1)
    }

    override fun getItemCount() = detailBean.recommends.size + 1

    override fun getItemViewType(position: Int) = when (position) {
        0 -> typeDetail
        else -> typeList
    }

    /**
     * 顶部详情数据绑定
     * @param holder DetailViewHolder
     */
    private fun bindDetail(holder: DetailViewHolder) {
        holder.imgShare.setOnClickListener { listener?.onShareClick() }
        holder.imgDownload.setOnClickListener { listener?.onDownLoadClick() }
        holder.imgCollect.setOnClickListener { listener?.onCollectClick(detailBean.isFavorite) }
        holder.tvName.text = detailBean.movie.vod_name
        holder.tvDetail.setOnClickListener { listener?.onDetailClick(bean = detailBean.movie) }
        holder.tvScore.text = detailBean.movie.score
        holder.tvType.text = "  /  ${detailBean.movie.vod_area_text}"
        holder.tvWhere.text = detailBean.movie.remark
        val number = detailBean.movie.vod_number.toInt()
        if (number > 0) {
            val list = ArrayList<String>()
            for (i in 1 until number + 1) {
                list.add(i.toString())
            }
            val adapter = MovieEpisodesAdapter(list)
            holder.rvList.adapter = adapter
        }
    }

    /**
     * 推荐列表
     * @param holder ListViewHolder
     * @param position Int
     */
    private fun bindList(holder: ListViewHolder, position: Int) {
        val bean = detailBean.recommends[position]
        GlideUtils.setMovieImg(context, bean.cover_url, holder.imgCover)
        holder.tvName.text = bean.vod_name
        holder.tvScore.text = bean.remark
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
        val layoutAlbum:LinearLayout = itemView.findViewById(R.id.layoutZhuanJi)


        init {
            rvList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCover: ImageView = itemView.findViewById(R.id.imgCover)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvScore: TextView = itemView.findViewById(R.id.tvScore)
    }

    fun setOnViewClick(listener: OnViewClickListener?) {
        this.listener = listener
    }

    interface OnViewClickListener {
        fun onShareClick()
        fun onDownLoadClick()
        fun onCollectClick(isCollection: Int)
        fun onDetailClick(bean: MovieDetail)
    }

}