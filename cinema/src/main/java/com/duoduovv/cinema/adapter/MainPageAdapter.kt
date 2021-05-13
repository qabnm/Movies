package com.duoduovv.cinema.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duoduovv.cinema.CinemaContext
import com.duoduovv.cinema.R
import com.duoduovv.cinema.bean.FilmRecommendBean
import com.duoduovv.cinema.bean.MainBean
import com.duoduovv.cinema.databinding.*
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
    private lateinit var bannerBind: ItemMainBannerBinding
    private lateinit var categoryBind: ItemLayoutCategoryBinding
    private lateinit var todayBind: ItemTodayReccommendBinding
    private lateinit var titleBind: ItemMainTitleBinding
    private lateinit var recommendBind: ItemMainRecommendBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        CinemaContext.TYPE_BANNER -> {
            val itemView =
                LayoutInflater.from(context).inflate(R.layout.item_main_banner, parent, false)
            bannerBind = ItemMainBannerBinding.bind(itemView)
            BannerViewHolder(itemView)
        }
        CinemaContext.TYPE_CATEGORY -> {
            val itemView =
                LayoutInflater.from(context).inflate(R.layout.item_layout_category, parent, false)
            categoryBind = ItemLayoutCategoryBinding.bind(itemView)
            categoryBind.rvList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            CategoryViewHolder(itemView)
        }
        CinemaContext.TYPE_TODAY_RECOMMEND -> {
            val itemView =
                LayoutInflater.from(context).inflate(R.layout.item_today_reccommend, parent, false)
            todayBind = ItemTodayReccommendBinding.bind(itemView)
            todayBind.rvList.layoutManager = GridLayoutManager(context, 3)
            TodayRecommendViewHolder(itemView)
        }
        CinemaContext.TYPE_TITLE -> {
            val itemView =
                LayoutInflater.from(context).inflate(R.layout.item_main_title, parent, false)
            titleBind = ItemMainTitleBinding.bind(itemView)
            TitleViewHolder(itemView)
        }
        CinemaContext.TYPE_EMPTY -> EmptyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_main_page_empty, parent, false)
        )
//        CinemaContext.TYPE_ALL_LOOK -> AllLookViewHolder(
//            LayoutInflater.from(context).inflate(R.layout.item_main_all_look, parent, false),
//            context
//        )
        CinemaContext.TYPE_RECOMMEND_LIST -> {
            val itemView =
                LayoutInflater.from(context).inflate(R.layout.item_main_recommend, parent, false)
            recommendBind = ItemMainRecommendBinding.bind(itemView)
            RecommendViewHolder(itemView)
        }
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
            is BannerViewHolder -> bindBanner()
            is CategoryViewHolder -> bindCategory()
            is TodayRecommendViewHolder -> bindTodayRecommend()
            is TitleViewHolder -> bindTitle()
            is EmptyViewHolder -> {
            }
//            is AllLookViewHolder -> bindAllLook(holder)
            is RecommendViewHolder -> {
                if (position > 3) bindRecommend(position - 4)
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
     */
    private fun bindBanner() {
        bean.mainPageBean.banners?.let {
            bannerBind.layoutBanner.addBannerLifecycleObserver(context as AppCompatActivity)
                .setAdapter(BannerImgAdapter(it, context)).indicator = CircleIndicator(context)
            bannerBind.layoutBanner.setOnBannerListener { data, _ ->
                val jumpType = (data as com.duoduovv.cinema.bean.Banner).jumpType
                val movieId = data.movieId
                if (jumpType == "1") listener?.onMovieClick(movieId, "-1")
            }
        }
    }

    /**
     * 分类
     */
    private fun bindCategory() {
        val category = bean.mainPageBean.category
        Log.d("mainAdapter", "bindCategory执行了${category?.isNotEmpty()}")
        if (category?.isNotEmpty() == true) {
            val categoryAdapter = MainCategoryAdapter()
            categoryBind.rvList.adapter = categoryAdapter
            categoryAdapter.setList(category)
            categoryAdapter.setOnItemClickListener { _, _, position ->
                val typeId = category[position].typeSpeArray.typeId
                listener?.onCategoryClick(typeId = typeId)
            }
        }
    }

    private var adapter: FilmRecommendAdapter? = null

    /**
     * 今日推荐
     */
    private fun bindTodayRecommend() {
        if (bean.mainPageBean.selectRecommends?.isNotEmpty() == true) {
            if (null == adapter) adapter = FilmRecommendAdapter(true)
            todayBind.rvList.adapter = adapter
            adapter?.setList(bean.mainPageBean.selectRecommends)
            adapter?.setOnItemClickListener { adapter, _, position ->
                val movieId = (adapter as FilmRecommendAdapter).data[position].strId
                val way = adapter.data[position].way
                listener?.onMovieClick(movieId, way)
            }
            todayBind.tvMore.setOnClickListener { listener?.onTodayMoreClick(bean.mainPageBean.selectRecommends!!) }
        }
    }

    /**
     * 大家都在看
     * @param holder AllLookViewHolder
     */
    private fun bindAllLook(holder: AllLookViewHolder) {
        holder.rvList.adapter = FilmAllLookAdapter(bean.mainPageBean.playRecommends)
    }

    private fun bindTitle() {
        titleBind.tvTitle.text = "热门推荐"
        if (bean.mainPageBean.selectRecommends?.isNotEmpty() == true) {
            titleBind.vLine.visibility = View.GONE
        } else {
            titleBind.vLine.visibility = View.VISIBLE
        }
    }

    /**
     * 首页推荐
     * @param position Int
     */
    private fun bindRecommend(position: Int) {
        val dataList = bean.mainRecommendBean.recommends
        if (dataList != null && dataList.isNotEmpty()) {
            val bean = dataList[position]
            GlideUtils.setMovieImg(context, bean.coverUrl, recommendBind.imgCover)
            recommendBind.tvName.text = bean.vodName
            recommendBind.tvScore.text = StringUtils.getString(bean.remark)
            recommendBind.layoutContainer.setOnClickListener {
                listener?.onMovieClick(
                    bean.strId,
                    bean.way
                )
            }
        }
    }

    private class RecommendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private class AllLookViewHolder(itemView: View, context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val rvList: RecyclerView = itemView.findViewById(R.id.rvList)

        init {
            rvList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private class TodayRecommendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

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
        fun onMovieClick(movieId: String, way: String)
        fun onTodayMoreClick(dataList: List<FilmRecommendBean>)
    }
}

