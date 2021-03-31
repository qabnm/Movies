package com.duoduovv.movie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duoduovv.movie.MovieContext
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.MovieDetail
import com.duoduovv.movie.bean.MovieDetailBean
import com.duoduovv.movie.bean.MovieItem
import dc.android.bridge.util.GlideUtils

/**
 * @author: jun.liu
 * @date: 2021/1/13 18:24
 * @des:影片详情
 */
class MovieDetailAdapter(private val context: Context, private var detailBean: MovieDetailBean) :
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

    fun notifyDataChange(detailBean: MovieDetailBean) {
        this.detailBean = detailBean
        notifyItemChanged(0)
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
        holder.imgCollect.setOnClickListener {
            listener?.onCollectClick(
                detailBean.isFavorite,
                detailBean.movie.id
            )
        }
        if (detailBean.isFavorite == 0) holder.imgCollect.setImageResource(R.drawable.movie_collect) else holder.imgCollect.setImageResource(
            R.drawable.movie_has_collect
        )
        holder.tvName.text = detailBean.movie.vod_name
        holder.tvDetail.setOnClickListener { listener?.onDetailClick(bean = detailBean.movie) }
        holder.tvScore.text = detailBean.movie.last_remark
        holder.tvType.text =
            " /  ${detailBean.movie.vod_area_text}  /  ${detailBean.movie.vod_lang}"
        if (MovieContext.TYPE_TV == detailBean.movie.movie_flag) {
            //是电视类型
            holder.layoutContainer.visibility = View.VISIBLE
            holder.tvWhere.text = detailBean.movie.remark
            if (detailBean.movieItems.size > 6) {
                holder.tvWhere.visibility = View.VISIBLE
                holder.tvWhere.setOnClickListener { listener?.onSelectClick(detailBean.movieItems) }
            } else {
                holder.tvWhere.visibility = View.INVISIBLE
            }
            val adapter = MovieEpisodesTvAdapter(detailBean.movieItems as MutableList<MovieItem>)
            holder.rvList.adapter = adapter
            for (i in detailBean.movieItems.indices) {
                if (detailBean.movieItems[i].isSelect) {
                    if (i != 0) (holder.rvList.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
                        i,
                        0
                    )
                }
            }
            adapter.setOnItemClickListener { ad, _, position ->
                val data = (ad as MovieEpisodesTvAdapter).data
                for (i in data.indices) {
                    data[i].isSelect = false
                }
                data[position].isSelect = true
                ad.notifyDataSetChanged()
                val vid = data[position].vid
                listener?.onSelectClick(vid, detailBean.movie.str_id, data[position].title)
            }
        } else {
            holder.layoutContainer.visibility = View.GONE
        }

        if (MovieContext.TYPE_VARIETY == detailBean.movie.movie_flag) {
            //综艺节目
            holder.layoutAlbum.visibility = View.VISIBLE
            val adapter = MovieAlbumAdapter(detailBean.movieItems as MutableList<MovieItem>)
            holder.rvAlbum.adapter = adapter
            for (i in detailBean.movieItems.indices) {
                if (detailBean.movieItems[i].isSelect) {
                    if (i != 0) (holder.rvList.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(i, 0)
                }
            }
            adapter.setOnItemClickListener { ad, _, position ->
                val data = (ad as MovieAlbumAdapter).data
                for (i in data.indices) {
                    data[i].isSelect = false
                }
                data[position].isSelect = true
                ad.notifyDataSetChanged()
                val vid = data[position].vid
                listener?.onSelectClick(vid, detailBean.movie.str_id, data[position].title)
            }
            if (detailBean.movieItems.size > 5) {
                holder.tvZWhere.visibility = View.VISIBLE
                holder.tvZWhere.text = detailBean.movie.last_remark
                holder.tvZWhere.setOnClickListener { listener?.onArtSelectClick(detailBean.movieItems) }
            } else {
                holder.tvZWhere.visibility = View.INVISIBLE
            }
        } else {
            holder.layoutAlbum.visibility = View.GONE
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
        holder.layoutContainer.setOnClickListener { listener?.onMovieClick(bean.str_id) }
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
        val layoutAlbum: LinearLayout = itemView.findViewById(R.id.layoutZhuanJi)
        val rvAlbum: RecyclerView = itemView.findViewById(R.id.rvAlbum)
        val tvZWhere:TextView = itemView.findViewById(R.id.tvZWhere)

        init {
            rvList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvAlbum.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCover: ImageView = itemView.findViewById(R.id.imgCover)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvScore: TextView = itemView.findViewById(R.id.tvScore)
        val layoutContainer: ConstraintLayout = itemView.findViewById(R.id.layoutContainer)
    }

    fun setOnViewClick(listener: OnViewClickListener?) {
        this.listener = listener
    }

    interface OnViewClickListener {
        fun onShareClick()
        fun onDownLoadClick()
        fun onCollectClick(isCollection: Int, movieId: String)
        fun onDetailClick(bean: MovieDetail)
        fun onSelectClick(dataList: List<MovieItem>)
        fun onMovieClick(movieId: String)
        fun onSelectClick(vid: String, movieId: String, vidTitle: String)
        fun onArtSelectClick(dataList: List<MovieItem>)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) manager.spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = when (getItemViewType(position)) {
                    typeDetail -> 3
                    typeList -> 1
                    else -> 1
                }
            }
    }
}