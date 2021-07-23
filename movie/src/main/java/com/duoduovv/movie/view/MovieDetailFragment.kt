package com.duoduovv.movie.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.duoduovv.advert.gdtad.GDTBannerAd
import com.duoduovv.advert.ttad.TTBannerAd
import com.duoduovv.common.BaseApplication
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.MovieAlbumAdapter
import com.duoduovv.movie.adapter.MovieEpisodesTvAdapter
import com.duoduovv.movie.bean.MovieDetailBean
import com.duoduovv.movie.bean.MovieItem
import com.duoduovv.movie.component.MovieDetailCallback
import com.duoduovv.movie.databinding.FragmentMovieDetailBinding
import com.duoduovv.room.domain.CollectionBean
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.TYPE_GDT_AD
import dc.android.bridge.BridgeContext.Companion.TYPE_TT_AD
import dc.android.bridge.util.OsUtils
import dc.android.bridge.view.BaseFragment
import dc.android.tools.LiveDataBus
import java.lang.ref.WeakReference

/**
 * @author: jun.liu
 * @date: 2021/5/21 11:49
 * @des:影视详情
 */
class MovieDetailFragment : BaseFragment() {
    private lateinit var mBind: FragmentMovieDetailBinding
    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMovieDetailBinding.inflate(inflater, container, false)

    private var callback: MovieDetailCallback? = null
    private var collectionBean: CollectionBean? = null
    private var gdtBannerAd: GDTBannerAd? = null
    private var ttBanner: TTBannerAd? = null
    private var tvAdapter: MovieEpisodesTvAdapter? = null
    private var albumAdapter: MovieAlbumAdapter? = null
    private var bannerWidth = 0f
    private var strId = ""

    fun setCallback(callback: MovieDetailCallback) {
        this.callback = callback
    }

    override fun initView() {
        mBind = baseBinding as FragmentMovieDetailBinding
        LiveDataBus.get().with("adClose", String::class.java).observe(this, {
            if ("adClose" == it) mBind.adContainer.visibility = View.GONE
        })
        bannerWidth =
            OsUtils.px2dip(requireContext(), OsUtils.getScreenWidth(requireContext()).toFloat())
                .toFloat() - 20
        tvAdapter = MovieEpisodesTvAdapter()
        mBind.rvList.adapter = tvAdapter
        tvAdapter?.setOnItemClickListener { ad, _, position ->
            val data = (ad as MovieEpisodesTvAdapter).data
            for (i in data.indices) {
                data[i].isSelect = false
            }
            data[position].isSelect = true
            ad.notifyDataSetChanged()
            val vid = data[position].vid
            callback?.onSelectClick(vid, strId, data[position].title,data[position].vip)
        }

        albumAdapter = MovieAlbumAdapter()
        mBind.rvAlbum.adapter = albumAdapter
        albumAdapter?.setOnItemClickListener { ad, _, position ->
            val data = (ad as MovieAlbumAdapter).data
            for (i in data.indices) {
                data[i].isSelect = false
            }
            data[position].isSelect = true
            ad.notifyDataSetChanged()
            val vid = data[position].vid
            callback?.onSelectClick(vid, strId, data[position].title,data[position].vip)
        }
    }

    /**
     * 顶部详情数据绑定
     */
    fun bindDetail(detailBean: MovieDetailBean) {
        if (detailBean.recommends?.isNotEmpty() == true) {
            mBind.tvCommend.visibility = View.VISIBLE
        } else {
            mBind.tvCommend.visibility = View.INVISIBLE
        }
        mBind.imgShare.setOnClickListener { callback?.onShareClick() }
        mBind.imgDownload.setOnClickListener { callback?.onDownLoadClick() }
        mBind.imgCollect.setOnClickListener {
            callback?.onCollectClick(collectionBean)
        }
        collectionBean?.let {
            mBind.imgCollect.setImageResource(if (it.isCollect) R.drawable.movie_has_collect else R.drawable.movie_collect)
        } ?: also {
            mBind.imgCollect.setImageResource(R.drawable.movie_collect)
        }
        mBind.tvName.text = detailBean.movie.vodName
        mBind.tvDetail.setOnClickListener { callback?.onDetailClick(bean = detailBean.movie) }
        mBind.tvScore.text = detailBean.movie.lastRemark
        mBind.tvType.text =
            " / ${detailBean.movie.vodArea} / ${detailBean.movie.vodLang}"
        if (BridgeContext.TYPE_TV == detailBean.movie.movieFlag || BridgeContext.TYPE_TV0 == detailBean.movie.movieFlag) {
            //是电视类型
            mBind.layoutContainer.visibility = View.VISIBLE
            mBind.tvWhere.text = detailBean.movie.lastRemark
            if (detailBean.movieItems?.size?:0 > 6) {
                mBind.tvWhere.visibility = View.VISIBLE
                mBind.tvWhere.setOnClickListener { callback?.onSelectClick(detailBean.movieItems!!) }
            } else {
                mBind.tvWhere.visibility = View.INVISIBLE
            }
            tvAdapter?.setList(detailBean.movieItems)
            if (detailBean.movieItems?.isNotEmpty() == true) {
                for (i in detailBean.movieItems.indices) {
                    if (detailBean.movieItems[i].isSelect) {
                        if (i != 0) (mBind.rvList.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
                            i,
                            0
                        )
                    }
                }
            }
            strId = detailBean.movie.strId
        } else {
            mBind.layoutContainer.visibility = View.GONE
        }

        if (BridgeContext.TYPE_ALBUM == detailBean.movie.movieFlag) {
            //综艺节目
            mBind.layoutZhuanJi.visibility = View.VISIBLE
            albumAdapter?.setList(detailBean.movieItems)
            if (detailBean.movieItems?.isNotEmpty() == true) {
                for (i in detailBean.movieItems.indices) {
                    if (detailBean.movieItems[i].isSelect) {
                        if (i != 0) (mBind.rvAlbum.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
                            i,
                            0
                        )
                    }
                }
                if (detailBean.movieItems.size > 5) {
                    mBind.tvZWhere.visibility = View.VISIBLE
                    mBind.tvZWhere.text = detailBean.movie.lastRemark
                    mBind.tvZWhere.setOnClickListener { callback?.onArtSelectClick(detailBean.movieItems) }
                } else {
                    mBind.tvZWhere.visibility = View.INVISIBLE
                }
            }
            strId = detailBean.movie.strId
        } else {
            mBind.layoutZhuanJi.visibility = View.GONE
        }
    }

    /**
     * 加载广告
     */
    fun updateAd(){
        //加载广告
        BaseApplication.configBean?.ad?.movieDetailBanner?.let {
            when(it.type){
                TYPE_TT_AD->{ initTTAd(it.value) }
                TYPE_GDT_AD ->{ initGDTAd(it.value) }
                else ->{ mBind.adContainer.visibility = View.GONE }
            }
        }?:also {
            mBind.adContainer.visibility = View.GONE
        }
    }

    /**
     * 更新选集的adapter选中的位置
     * @param movieItem List<MovieItem>
     * @param pos Int
     */
    fun updateSelect(movieItem: List<MovieItem>, pos: Int) {
        tvAdapter?.let {
            it.setList(movieItem)
            mBind.rvList.layoutManager?.scrollToPosition(pos)
        }
        albumAdapter?.let {
            it.setList(movieItem)
            mBind.rvAlbum.layoutManager?.scrollToPosition(pos)
        }
    }

    /**
     * 广点通广告
     * @param posId String
     */
    private fun initGDTAd(posId: String) {
        if (null == gdtBannerAd){
            gdtBannerAd = GDTBannerAd()
        }else{
            gdtBannerAd?.onDestroy()
        }
        gdtBannerAd?.initBanner(requireActivity(), posId, mBind.adContainer)
    }

    /**
     * 穿山甲广告s
     * @param posId String
     */
    private fun initTTAd(posId: String) {
        Log.d("TTBannerAd","fragment${ttBanner == null}")
        if (null == ttBanner) {
            ttBanner = TTBannerAd(WeakReference(requireActivity()).get())
        } else{
            ttBanner?.onDestroy()
        }
        val weakReference = WeakReference(requireActivity())
        val weakContainer = WeakReference(mBind.adContainer)
        weakContainer.get()?.let {
            ttBanner?.initBanner(posId, bannerWidth, 0f, it)
        }
    }

    override fun onDestroyView() {
        mBind.adContainer.removeAllViews()
        ttBanner?.onDestroy()
        gdtBannerAd?.onDestroy()
        ttBanner = null
        gdtBannerAd = null
        Log.d("onDestroyView","onDestroyView")
        super.onDestroyView()
    }

    /**
     * 收藏状态变化
     * @param collectionBean CollectionBean?
     */
    fun notifyCollectionChange(collectionBean: CollectionBean?) {
        this.collectionBean = collectionBean
        collectionBean?.let {
            mBind.imgCollect.setImageResource(if (it.isCollect) R.drawable.movie_has_collect else R.drawable.movie_collect)
        } ?: also {
            mBind.imgCollect.setImageResource(R.drawable.movie_collect)
        }
    }
}