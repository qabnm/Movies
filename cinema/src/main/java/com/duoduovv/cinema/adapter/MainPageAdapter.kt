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
import com.duoduovv.cinema.bean.Banner
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        CinemaContext.TYPE_BANNER -> {
            bean.mainPageBean.banners?.add(0, Banner("","","",""))
            val bannerBind = ItemMainBannerBinding.inflate(LayoutInflater.from(context),parent,false)
            BannerViewHolder(bannerBind)
        }
        CinemaContext.TYPE_CATEGORY -> {
           val  categoryBind = ItemLayoutCategoryBinding.inflate(LayoutInflater.from(context),parent,false)
            categoryBind.rvList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            CategoryViewHolder(categoryBind)
        }
        CinemaContext.TYPE_TODAY_RECOMMEND -> {
            val todayBind = ItemTodayReccommendBinding.inflate(LayoutInflater.from(context),parent,false)
            todayBind.rvList.layoutManager = GridLayoutManager(context, 3)
            TodayRecommendViewHolder(todayBind)
        }
        CinemaContext.TYPE_TITLE -> {
            val titleBind = ItemMainTitleBinding.inflate(LayoutInflater.from(context),parent,false)
            TitleViewHolder(titleBind)
        }
        CinemaContext.TYPE_EMPTY -> EmptyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_main_page_empty, parent, false)
        )
//        CinemaContext.TYPE_ALL_LOOK -> AllLookViewHolder(
//            LayoutInflater.from(context).inflate(R.layout.item_main_all_look, parent, false),
//            context
//        )
        else -> {
            val recommendBind = ItemMainRecommendBinding.inflate(LayoutInflater.from(context),parent,false)
            RecommendViewHolder(recommendBind)
        }
    }

    fun notifyDataChanged(bean: MainBean) {
        this.bean = bean
        notifyDataSetChanged()
    }

    override fun getItemCount() =
        if (bean.mainRecommendBean.recommends?.isNotEmpty() == true) bean.mainRecommendBean.recommends!!.size + 4 else 4

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BannerViewHolder -> bindBanner(holder)
            is CategoryViewHolder -> bindCategory(holder)
            is TodayRecommendViewHolder -> bindTodayRecommend(holder)
            is TitleViewHolder -> bindTitle(holder)
            is EmptyViewHolder -> {
            }
//            is AllLookViewHolder -> bindAllLook(holder)
            is RecommendViewHolder -> {
                if (position > 3) bindRecommend(holder,position - 4)
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
    private fun bindBanner(holder:BannerViewHolder) {
        bean.mainPageBean.banners?.let {
            holder.bannerBind.layoutBanner.addBannerLifecycleObserver(context as AppCompatActivity)
                .setAdapter(BannerImgAdapter(it, context)).indicator = CircleIndicator(context)
            holder.bannerBind.layoutBanner.setOnBannerListener { data, _ ->
                val jumpType = (data as Banner).jumpType
                val movieId = data.movieId
                if (jumpType == "1") listener?.onMovieClick(movieId, "-1")
            }
        }
    }

    /**
     * 分类
     */
    private fun bindCategory(holder:CategoryViewHolder) {
        val category = bean.mainPageBean.category
        Log.d("mainAdapter", "bindCategory执行了${category?.isNotEmpty()}")
        if (category?.isNotEmpty() == true) {
            val categoryAdapter = MainCategoryAdapter()
            holder.categoryBind.rvList.adapter = categoryAdapter
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
    private fun bindTodayRecommend(holder:TodayRecommendViewHolder) {
        if (bean.mainPageBean.selectRecommends?.isNotEmpty() == true) {
            if (null == adapter) adapter = FilmRecommendAdapter(true)
            holder.todayBind.rvList.adapter = adapter
            adapter?.setList(bean.mainPageBean.selectRecommends)
            adapter?.setOnItemClickListener { adapter, _, position ->
                val movieId = (adapter as FilmRecommendAdapter).data[position].strId
                val way = adapter.data[position].way
                listener?.onMovieClick(movieId, way)
            }
            holder.todayBind.tvMore.setOnClickListener { listener?.onTodayMoreClick(bean.mainPageBean.selectRecommends!!) }
        }
    }

    /**
     * 大家都在看
     * @param holder AllLookViewHolder
     */
    private fun bindAllLook(holder: AllLookViewHolder) {
        holder.rvList.adapter = FilmAllLookAdapter(bean.mainPageBean.playRecommends)
    }

    private fun bindTitle(holder:TitleViewHolder) {
        holder.titleBind.tvTitle.text = "热门推荐"
        if (bean.mainPageBean.selectRecommends?.isNotEmpty() == true) {
            holder.titleBind.vLine.visibility = View.GONE
        } else {
            holder.titleBind.vLine.visibility = View.VISIBLE
        }
    }

    /**
     * 首页推荐
     * @param position Int
     */
    private fun bindRecommend(holder:RecommendViewHolder,position: Int) {
        val dataList = bean.mainRecommendBean.recommends
        if (dataList != null && dataList.isNotEmpty()) {
            val bean = dataList[position]
            GlideUtils.setMovieImg(context, bean.coverUrl, holder.recommendBind.imgCover)
            holder.recommendBind.tvName.text = bean.vodName
            holder.recommendBind.tvScore.text = StringUtils.getString(bean.remark)
            holder.recommendBind.layoutContainer.setOnClickListener {
                listener?.onMovieClick(
                    bean.strId,
                    bean.way
                )
            }
        }
    }

    private class RecommendViewHolder(val recommendBind:ItemMainRecommendBinding) : RecyclerView.ViewHolder(recommendBind.root)

    private class AllLookViewHolder(itemView: View, context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val rvList: RecyclerView = itemView.findViewById(R.id.rvList)

        init {
            rvList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private class TodayRecommendViewHolder(val todayBind:ItemTodayReccommendBinding) : RecyclerView.ViewHolder(todayBind.root)

    private class BannerViewHolder(val bannerBind:ItemMainBannerBinding) : RecyclerView.ViewHolder(bannerBind.root)

    private class CategoryViewHolder(val categoryBind:ItemLayoutCategoryBinding) : RecyclerView.ViewHolder(categoryBind.root)

    private class TitleViewHolder(val titleBind:ItemMainTitleBinding) : RecyclerView.ViewHolder(titleBind.root)

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

