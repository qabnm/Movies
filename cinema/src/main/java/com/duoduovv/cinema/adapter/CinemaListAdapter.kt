package com.duoduovv.cinema.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duoduovv.advert.gdtad.GDTInfoAdForSelfRender
import com.duoduovv.advert.ttad.TTInfoAd
import com.duoduovv.cinema.bean.BannerBean
import com.duoduovv.cinema.bean.ColumnBean
import com.duoduovv.cinema.databinding.ItemLayoutCategoryBinding
import com.duoduovv.cinema.databinding.ItemMainBannerBinding
import com.duoduovv.cinema.databinding.ItemMainRecommendBinding
import com.duoduovv.cinema.databinding.ItemMainTitleBinding
import com.duoduovv.cinema.view.CinemaListFragment
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.databinding.ItemTypeAdBinding
import com.youth.banner.indicator.CircleIndicator
import dc.android.bridge.BridgeContext
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.OsUtils
import dc.android.bridge.util.StringUtils

/**
 * @author: jun.liu
 * @date: 2021/7/20 22:43
 * @des:首页栏目页面adapter
 */
class CinemaListAdapter(
    private var dataList: ArrayList<ColumnBean>,
    private val context: Context,
    private val fragment: CinemaListFragment
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val typeBanner = -100
    private val typeCategory = -200
    private val typeTitle = -300
    private val typeList = -400
    private var listener: OnItemClickListener? = null
    private var width = 0f
    private var ttAd: TTInfoAd? = null
    private var gdtAd: GDTInfoAdForSelfRender? = null
    private var bannerAdapter: BannerImgAdapter? = null

    init {
        width = OsUtils.px2dip(context, OsUtils.getScreenWidth(context).toFloat()).toFloat() - 20
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        typeBanner -> {
            val bannerBind =
                ItemMainBannerBinding.inflate(LayoutInflater.from(context), parent, false)
            BannerViewHolder(bannerBind)
        }
        typeCategory -> {
            val categoryBind =
                ItemLayoutCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
            CategoryViewHolder(categoryBind)
        }
        typeTitle -> {
            val titleBind =
                ItemMainTitleBinding.inflate(LayoutInflater.from(context), parent, false)
            TitleViewHolder(titleBind)
        }
        typeList -> {
            val listBind =
                ItemMainRecommendBinding.inflate(LayoutInflater.from(context), parent, false)
            ListViewHolder(listBind)
        }
        else -> {
            val adBinder = ItemTypeAdBinding.inflate(LayoutInflater.from(context), parent, false)
            TypeAdViewHolder(adBinder)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BannerViewHolder -> {
                bindBanner(holder, position)
            }
            is CategoryViewHolder -> {
                bindCategory(holder, position)
            }
            is TitleViewHolder -> {
                bindTitle(holder, position)
            }
            is ListViewHolder -> {
                bindList(holder, position)
            }
            is TypeAdViewHolder -> {
                bindAd(holder, position)
            }
        }
    }

    fun onDestroy() {
        ttAd?.destroyInfoAd()
        gdtAd?.onDestroy()
        bannerAdapter?.onDestroy()
        dataList.clear()
    }

    /**
     * 广告数据
     * @param holder TypeAdViewHolder
     * @param position Int
     */
    private fun bindAd(holder: TypeAdViewHolder, position: Int) {
        if (!dataList[position].hasLoad) {
            dataList[position].hasLoad = true
            BaseApplication.configBean?.ad?.libraryAd?.let {
                when (it.type) {
                    BridgeContext.TYPE_TT_AD -> {
                        holder.adBinder.layoutTTAd.visibility = View.VISIBLE
                        holder.adBinder.layoutGdt.visibility = View.GONE
                        if (null == ttAd) ttAd = TTInfoAd()
                        ttAd?.initTTInfoAd(
                            context as Activity,
                            it.value,
                            width,
                            0f,
                            holder.adBinder.layoutTTAd
                        )
                    }
                    BridgeContext.TYPE_GDT_AD -> {
                        holder.adBinder.layoutTTAd.visibility = View.GONE
                        holder.adBinder.layoutGdt.visibility = View.VISIBLE
                        if (null == gdtAd) gdtAd = GDTInfoAdForSelfRender()
                        gdtAd?.initInfoAd(
                            context,
                            it.value,
                            holder.adBinder.adImgCover,
                            holder.adBinder.mediaView,
                            holder.adBinder.layoutGdt
                        )
                    }
                    else -> {
                    }
                }
            }
        }
    }

    /**
     * 列表数据
     * @param holder ListViewHolder
     * @param position Int
     */
    private fun bindList(holder: ListViewHolder, position: Int) {
        val bean = dataList[position]
        GlideUtils.setMovieImg(context, bean.coverUrl, holder.listBind.imgCover)
        holder.listBind.tvName.text = bean.vodName
        holder.listBind.tvScore.text = StringUtils.getString(bean.remark)
        holder.listBind.layoutContainer.setOnClickListener {
            listener?.onMovieClick(bean.strId?:"", bean.way?:"")
        }
    }

    /**
     * 标题类型的布局
     * @param holder TitleViewHolder
     * @param position Int
     */
    private fun bindTitle(holder: TitleViewHolder, position: Int) {
        holder.titleBind.tvTitle.text = dataList[position].titleName
        holder.titleBind.layoutMore.setOnClickListener {
            listener?.onMoreClick(dataList[position].titleName ?: "", dataList[position].id ?: "")
        }
    }

    /**
     * 分类 数据
     * @param holder CategoryViewHolder
     * @param position Int
     */
    private fun bindCategory(holder: CategoryViewHolder, position: Int) {
        val category = dataList[position].category
        Log.d("mainAdapter", "bindCategory执行了${category?.isNotEmpty()}")
        if (category?.isNotEmpty() == true) {
            val categoryAdapter = MainCategoryAdapter()
            holder.categoryBind.rvList.adapter = categoryAdapter
            categoryAdapter.setList(category)
            categoryAdapter.setOnItemClickListener { _, _, pos ->
                val typeId = category[pos].typeSpeArray.typeId
                val typeName = category[pos].name
                listener?.onCategoryClick(typeId = typeId, typeName = typeName)
            }
        }
    }

    /**
     * 绑定banner数据
     * @param holder BannerViewHolder
     */
    private fun bindBanner(holder: BannerViewHolder, position: Int) {
        val bannerData = dataList[position].banner
        if (bannerData?.isNotEmpty() == true) {
            bannerAdapter = BannerImgAdapter(bannerData, context)
            holder.bannerBind.layoutBanner.addBannerLifecycleObserver(fragment)
                .setAdapter(bannerAdapter).indicator = CircleIndicator(context)
            holder.bannerBind.layoutBanner.setOnBannerListener { data, _ ->
                val jumpType = (data as BannerBean).jumpType
                val movieId = data.movieId
                if (jumpType == "1") listener?.onMovieClick(movieId, "-1")
            }
        }
    }

    override fun getItemCount() = dataList.size

    override fun getItemViewType(position: Int) = when (dataList[position].type) {
        "banner" -> typeBanner
        "category" -> typeCategory
        "title" -> typeTitle
        "video" -> typeList
        else -> {
            position
        }
    }

    private class BannerViewHolder(val bannerBind: ItemMainBannerBinding) :
        RecyclerView.ViewHolder(bannerBind.root)

    private class CategoryViewHolder(val categoryBind: ItemLayoutCategoryBinding) :
        RecyclerView.ViewHolder(categoryBind.root)

    private class TypeAdViewHolder(val adBinder: ItemTypeAdBinding) :
        RecyclerView.ViewHolder(adBinder.root)

    private class TitleViewHolder(val titleBind: ItemMainTitleBinding) :
        RecyclerView.ViewHolder(titleBind.root)

    private class ListViewHolder(val listBind: ItemMainRecommendBinding) :
        RecyclerView.ViewHolder(listBind.root)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) manager.spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = when (getItemViewType(position)) {
                    typeList -> 1
                    else -> 3
                }
            }
    }

    fun notifyDataChanged(dataList: ArrayList<ColumnBean>, page: Int) {
        if (page == 1) {
            //代表这时候是下拉刷新了，这时候会重新请求广告，所以这时候要把之前的广告数据给销毁掉
            ttAd?.destroyInfoAd()
            gdtAd?.onDestroy()
            bannerAdapter?.onDestroy()
        }
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onCategoryClick(typeId: String, typeName: String)
        fun onMovieClick(movieId: String, way: String)
        fun onMoreClick(titleName: String, id: String)
    }
}