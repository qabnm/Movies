package com.duoduovv.movie.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duoduovv.advert.gdtad.GDTBannerAd
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.MovieDetail
import com.duoduovv.movie.bean.MovieDetailBean
import com.duoduovv.movie.bean.MovieItem
import com.duoduovv.movie.databinding.ItemMovieDetailTopBinding
import com.duoduovv.movie.databinding.ItemMovieRecommendBinding
import com.duoduovv.room.domain.CollectionBean
import dc.android.bridge.BridgeContext.Companion.TYPE_ALBUM
import dc.android.bridge.BridgeContext.Companion.TYPE_TV
import dc.android.bridge.BridgeContext.Companion.TYPE_TV0
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
    private var collectionBean: CollectionBean? = null
    private lateinit var detailBind: ItemMovieDetailTopBinding
    private lateinit var recommendBind: ItemMovieRecommendBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        typeDetail -> {
            val itemView =
                LayoutInflater.from(context).inflate(R.layout.item_movie_detail_top, parent, false)
            detailBind = ItemMovieDetailTopBinding.bind(itemView)
            detailBind.rvList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            detailBind.rvAlbum.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            DetailViewHolder(itemView)
        }
        else -> {
            val itemView =
                LayoutInflater.from(context).inflate(R.layout.item_movie_recommend, parent, false)
            recommendBind = ItemMovieRecommendBinding.bind(itemView)
            ListViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DetailViewHolder) bindDetail(holder)
        if (holder is ListViewHolder && position > 0) bindList(position - 1)
    }

    fun notifyDataChange(detailBean: MovieDetailBean) {
        this.detailBean = detailBean
        notifyItemChanged(0)
    }

    fun notifyCollectionChange(collectionBean: CollectionBean?) {
        this.collectionBean = collectionBean
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
        detailBind.imgShare.setOnClickListener { listener?.onShareClick() }
        detailBind.imgDownload.setOnClickListener { listener?.onDownLoadClick() }
        detailBind.imgCollect.setOnClickListener {
            listener?.onCollectClick(collectionBean)
        }
        collectionBean?.let {
            detailBind.imgCollect.setImageResource(if (it.isCollect) R.drawable.movie_has_collect else R.drawable.movie_collect)
        } ?: also {
            detailBind.imgCollect.setImageResource(R.drawable.movie_collect)
        }
        detailBind.tvName.text = detailBean.movie.vodName
        detailBind.tvDetail.setOnClickListener { listener?.onDetailClick(bean = detailBean.movie) }
        detailBind.tvScore.text = detailBean.movie.lastRemark
        detailBind.tvType.text =
            " /  ${detailBean.movie.vodArea}  /  ${detailBean.movie.vodLang}"
        if (TYPE_TV == detailBean.movie.movieFlag || TYPE_TV0 == detailBean.movie.movieFlag) {
            //是电视类型
            detailBind.layoutContainer.visibility = View.VISIBLE
            detailBind.tvWhere.text = detailBean.movie.lastRemark
            if (detailBean.movieItems.size > 6) {
                detailBind.tvWhere.visibility = View.VISIBLE
                detailBind.tvWhere.setOnClickListener { listener?.onSelectClick(detailBean.movieItems) }
            } else {
                detailBind.tvWhere.visibility = View.INVISIBLE
            }
            val adapter = MovieEpisodesTvAdapter(detailBean.movieItems as MutableList<MovieItem>)
            detailBind.rvList.adapter = adapter
            for (i in detailBean.movieItems.indices) {
                if (detailBean.movieItems[i].isSelect) {
                    if (i != 0) (detailBind.rvList.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
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
                listener?.onSelectClick(vid, detailBean.movie.strId, data[position].title)
            }
        } else {
            detailBind.layoutContainer.visibility = View.GONE
        }

        if (TYPE_ALBUM == detailBean.movie.movieFlag) {
            //综艺节目
            detailBind.layoutZhuanJi.visibility = View.VISIBLE
            val adapter = MovieAlbumAdapter(detailBean.movieItems as MutableList<MovieItem>)
            detailBind.rvAlbum.adapter = adapter
            for (i in detailBean.movieItems.indices) {
                if (detailBean.movieItems[i].isSelect) {
                    if (i != 0) (detailBind.rvAlbum.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
                        i,
                        0
                    )
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
                listener?.onSelectClick(vid, detailBean.movie.strId, data[position].title)
            }
            if (detailBean.movieItems.size > 5) {
                detailBind.tvZWhere.visibility = View.VISIBLE
                detailBind.tvZWhere.text = detailBean.movie.lastRemark
                detailBind.tvZWhere.setOnClickListener { listener?.onArtSelectClick(detailBean.movieItems) }
            } else {
                detailBind.tvZWhere.visibility = View.INVISIBLE
            }
        } else {
            detailBind.layoutZhuanJi.visibility = View.GONE
        }

        //加载广告
        val gdtBannerAd = GDTBannerAd()
        gdtBannerAd.initBanner(context as Activity, "5011588732659291",detailBind.adContainer)
    }

    /**
     * 推荐列表
     * @param position Int
     */
    private fun bindList(position: Int) {
        val bean = detailBean.recommends[position]
        GlideUtils.setMovieImg(context, bean.coverUrl, recommendBind.imgCover)
        recommendBind.tvName.text = bean.vodName
        recommendBind.tvScore.text = bean.lastRemark
        recommendBind.layoutContainer.setOnClickListener { listener?.onMovieClick(bean.strId) }
    }

    private class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun setOnViewClick(listener: OnViewClickListener?) {
        this.listener = listener
    }

    interface OnViewClickListener {
        fun onShareClick()
        fun onDownLoadClick()
        fun onCollectClick(collectionBean: CollectionBean?)
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