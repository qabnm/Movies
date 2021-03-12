package com.duoduovv.cinema.adapter

import android.content.Context
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
import com.duoduovv.cinema.bean.Category
import com.duoduovv.cinema.bean.MainBean
import com.youth.banner.Banner
import com.youth.banner.indicator.CircleIndicator
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.OsUtils
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
        CinemaContext.TYPE_TODAY_RECOMMEND -> TodayRecommendViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_today_reccommend, parent, false),
            context
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
        if (bean.mainRecommendBean.recommends?.isNotEmpty() == true) bean.mainRecommendBean.recommends!!.size + 2 else 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BannerViewHolder -> bindBanner(holder = holder)
            is TodayRecommendViewHolder -> bindTodayRecommend(holder)
//            is AllLookViewHolder -> bindAllLook(holder)
            is RecommendViewHolder -> {
                if (position > 1) bindRecommend(holder, position - 2)
            }
        }
    }

    override fun getItemViewType(position: Int) = when (position) {
        0 -> CinemaContext.TYPE_BANNER
        1 -> CinemaContext.TYPE_TODAY_RECOMMEND
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
        }
        val category = bean.mainPageBean.category
        if (category?.isNotEmpty() == true) {
            holder.rvList.visibility = View.VISIBLE
            val categoryAdapter = MainCategoryAdapter()
            holder.rvList.adapter = categoryAdapter
            categoryAdapter.setList(category)
            categoryAdapter.setOnItemClickListener { _, _, position ->
                val typeId = category[position].type_spe_array.type_id
                listener?.onCategoryClick(typeId = typeId)
            }
        } else {
            holder.rvList.visibility = View.GONE
        }
    }

    private var adapter: FilmRecommendAdapter? = null

    /**
     * 今日推荐
     * @param holder TodayRecommendViewHolder
     */
    private fun bindTodayRecommend(holder: TodayRecommendViewHolder) {
        if (bean.mainPageBean.selectRecommends?.isEmpty() == true) {
            holder.layoutContainer.visibility = View.GONE
        } else {
            holder.layoutContainer.visibility = View.VISIBLE
            if (null == adapter) adapter = FilmRecommendAdapter()
            holder.rvList.adapter = adapter
            adapter?.setList(bean.mainPageBean.selectRecommends)
            adapter?.setOnItemClickListener { adapter, _, position ->
                val movieId = (adapter as FilmRecommendAdapter).data[position].str_id
                listener?.onMovieClick(movieId)
            }
        }
    }

    /**
     * 大家都在看
     * @param holder AllLookViewHolder
     */
    private fun bindAllLook(holder: AllLookViewHolder) {
        holder.rvList.adapter = FilmAllLookAdapter(bean.mainPageBean.playRecommends)
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
            holder.layoutContainer.setOnClickListener { listener?.onMovieClick(bean.str_id) }
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
        val layoutContainer: ConstraintLayout = itemView.findViewById(R.id.layoutContainer)

        init {
            rvList.layoutManager = GridLayoutManager(context, 3)
        }
    }

    private class BannerViewHolder(itemView: View, context: Context) :
        RecyclerView.ViewHolder(itemView) {
        val banner: Banner<com.duoduovv.cinema.bean.Banner, BannerImgAdapter> =
            itemView.findViewById(R.id.layoutBanner)
        val rvList: RecyclerView = itemView.findViewById(R.id.rvList)

        init {
            rvList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

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
        fun onMovieClick(movieId: String)
    }
}

