package com.duoduovv.cinema.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duoduovv.cinema.CinemaContext
import com.duoduovv.cinema.R
import com.duoduovv.cinema.bean.FilmRecommendBean
import com.duoduovv.cinema.bean.MainBean
import com.youth.banner.Banner
import com.youth.banner.indicator.CircleIndicator
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.StringUtils

/**
 * @author: jun.liu
 * @date: 2021/1/15 16:38
 * @des:首页adapter
 */
class MainPageAdapter(
    private val context: Context,
    private var bean: MainBean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        CinemaContext.TYPE_BANNER -> BannerViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_main_banner, parent, false), context
        )
        CinemaContext.TYPE_CATEGORY -> CategoryViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_layout_category, parent, false),
            context
        )
        CinemaContext.TYPE_TODAY_RECOMMEND -> TodayRecommendViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_today_reccommend, parent, false),
            context
        )
        CinemaContext.TYPE_TITLE -> TitleViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_main_title, parent, false)
        )
        CinemaContext.TYPE_EMPTY -> EmptyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_main_page_empty, parent, false)
        )
//        CinemaContext.TYPE_ALL_LOOK -> AllLookViewHolder(
//            LayoutInflater.from(context).inflate(R.layout.item_main_all_look, parent, false),
//            context
//        )
        CinemaContext.TYPE_RECOMMEND_LIST -> RecommendViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_main_recommend, parent, false)
        )
        else -> RecommendViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_main_recommend, parent, false)
        )
    }

    fun notifyDataChanged(bean: MainBean) {
        this.bean = bean
        notifyDataSetChanged()
    }

    override fun getItemCount() =
        if (bean.mainRecommendBean.recommends?.isNotEmpty() == true) bean.mainRecommendBean.recommends!!.size + 4 else 4

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BannerViewHolder -> bindBanner(holder = holder)
            is CategoryViewHolder -> bindCategory(holder = holder)
            is TodayRecommendViewHolder -> bindTodayRecommend(holder)
            is TitleViewHolder -> bindTitle(holder = holder)
            is EmptyViewHolder -> {
            }
//            is AllLookViewHolder -> bindAllLook(holder)
            is RecommendViewHolder -> {
                if (position > 3) bindRecommend(holder, position - 4)
            }
        }
    }

    override fun getItemViewType(position: Int) = when (position) {
        0 -> {
            if (bean.mainPageBean.banners?.isNotEmpty() == true) {
                CinemaContext.TYPE_BANNER
            } else {
                CinemaContext.TYPE_EMPTY
            }
        }
        1 -> {
            if (bean.mainPageBean.category?.isNotEmpty() == true) {
                CinemaContext.TYPE_CATEGORY
            } else {
                CinemaContext.TYPE_EMPTY
            }
        }
        2 -> {
            if (bean.mainPageBean.selectRecommends?.isNotEmpty() == true) {
                CinemaContext.TYPE_TODAY_RECOMMEND
            } else {
                CinemaContext.TYPE_EMPTY
            }
        }
        3 -> CinemaContext.TYPE_TITLE
//        2 -> CinemaContext.TYPE_ALL_LOOK
        else -> CinemaContext.TYPE_RECOMMEND_LIST
    }

    /**
     * banner显示
     * @param holder BannerViewHolder
     */
    private fun bindBanner(holder: BannerViewHolder) {
        bean.mainPageBean.banners?.let {
            holder.banner.addBannerLifecycleObserver(context as AppCompatActivity)
                .setAdapter(BannerImgAdapter(it, context)).indicator = CircleIndicator(context)
            holder.banner.setOnBannerListener { data, _ ->
                val jumpType = (data as com.duoduovv.cinema.bean.Banner).jump_type
                val movieId = data.movie_id
                if (jumpType == "1") listener?.onMovieClick(movieId, -1)
            }
        }
    }

    /**
     * 分类
     * @param holder CategoryViewHolder
     */
    private fun bindCategory(holder: CategoryViewHolder) {
        val category = bean.mainPageBean.category
        Log.d("mainAdapter", "bindCategory执行了${category?.isNotEmpty()}")
        if (category?.isNotEmpty() == true) {
            val categoryAdapter = MainCategoryAdapter()
            holder.rvList.adapter = categoryAdapter
            categoryAdapter.setList(category)
            categoryAdapter.setOnItemClickListener { _, _, position ->
                val typeId = category[position].type_spe_array.type_id
                listener?.onCategoryClick(typeId = typeId)
            }
        }
    }

    private var adapter: FilmRecommendAdapter? = null

    /**
     * 今日推荐
     * @param holder TodayRecommendViewHolder
     */
    private fun bindTodayRecommend(holder: TodayRecommendViewHolder) {
        if (bean.mainPageBean.selectRecommends?.isNotEmpty() == true) {
            if (null == adapter) adapter = FilmRecommendAdapter(true)
            holder.rvList.adapter = adapter
            adapter?.setList(bean.mainPageBean.selectRecommends)
            adapter?.setOnItemClickListener { adapter, _, position ->
                val movieId = (adapter as FilmRecommendAdapter).data[position].str_id
                val way = adapter.data[position].way
                listener?.onMovieClick(movieId, way)
            }
            holder.tvMore.setOnClickListener { listener?.onTodayMoreClick(bean.mainPageBean.selectRecommends!!) }
        }
    }

    /**
     * 大家都在看
     * @param holder AllLookViewHolder
     */
    private fun bindAllLook(holder: AllLookViewHolder) {
        holder.rvList.adapter = FilmAllLookAdapter(bean.mainPageBean.playRecommends)
    }

    private fun bindTitle(holder: TitleViewHolder) {
        holder.tvTitle.text = "热门推荐"
        if (bean.mainPageBean.selectRecommends?.isNotEmpty() == true) {
            holder.vLine.visibility = View.GONE
        } else {
            holder.vLine.visibility = View.VISIBLE
        }
    }

    /**
     * 首页推荐
     * @param holder RecommendViewHolder
     * @param position Int
     */
    private fun bindRecommend(holder: RecommendViewHolder, position: Int) {
        val dataList = bean.mainRecommendBean.recommends
        if (dataList != null && dataList.isNotEmpty()) {
            val bean = dataList[position]
            GlideUtils.setMovieImg(context, bean.cover_url, holder.coverImg)
            holder.tvName.text = bean.vod_name
            holder.tvScore.text = StringUtils.getString(bean.remark)
            holder.layoutContainer.setOnClickListener {
                listener?.onMovieClick(
                    bean.str_id,
                    bean.way
                )
            }
        }
    }

    private class RecommendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coverImg: ImageView = itemView.findViewById(R.id.imgCover)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvScore: TextView = itemView.findViewById(R.id.tvScore)
        val layoutContainer: ConstraintLayout = itemView.findViewById(R.id.layoutContainer)
    }

    private class AllLookViewHolder(itemView: View, context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val rvList: RecyclerView = itemView.findViewById(R.id.rvList)

        init {
            rvList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private class TodayRecommendViewHolder(itemView: View, context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val tvMore: TextView = itemView.findViewById(R.id.tvMore)
        val tvChange: TextView = itemView.findViewById(R.id.tvChange)
        val rvList: RecyclerView = itemView.findViewById(R.id.rvList)

        init {
            rvList.layoutManager = GridLayoutManager(context, 3)
        }
    }

    private class BannerViewHolder(itemView: View, context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val banner: Banner<com.duoduovv.cinema.bean.Banner, BannerImgAdapter> =
            itemView.findViewById(R.id.layoutBanner)
    }

    private class CategoryViewHolder(itemView: View, context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val rvList: RecyclerView = itemView.findViewById(R.id.rvList)

        init {
            rvList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val vLine: View = itemView.findViewById(R.id.vLine)
    }

    private class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) manager.spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = when (getItemViewType(position)) {
                    CinemaContext.TYPE_BANNER -> 3
                    CinemaContext.TYPE_TODAY_RECOMMEND -> 3
                    CinemaContext.TYPE_ALL_LOOK -> 3
                    CinemaContext.TYPE_RECOMMEND_LIST -> 1
                    else -> 3
                }
            }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onCategoryClick(typeId: String)
        fun onMovieClick(movieId: String, way: Int)
        fun onTodayMoreClick(dataList:List<FilmRecommendBean>)
    }
}

