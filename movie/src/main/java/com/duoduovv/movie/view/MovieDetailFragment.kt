package com.duoduovv.movie.view

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.duoduovv.advert.AdvertBridge
import com.duoduovv.advert.gdtad.GDTBannerAd
import com.duoduovv.advert.ttad.TTBannerAd
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.MovieAlbumAdapter
import com.duoduovv.movie.adapter.MovieEpisodesTvAdapter
import com.duoduovv.movie.bean.MovieDetailBean
import com.duoduovv.movie.bean.MovieItem
import com.duoduovv.movie.component.MovieDetailCallback
import com.duoduovv.movie.databinding.FragmentMovieDetailBinding
import com.duoduovv.room.domain.CollectionBean
import dc.android.bridge.BridgeContext
import dc.android.bridge.util.StringUtils
import dc.android.bridge.view.BaseFragment
import dc.android.tools.LiveDataBus

/**
 * @author: jun.liu
 * @date: 2021/5/21 11:49
 * @des:影视详情
 */
class MovieDetailFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_movie_detail
    private lateinit var mBind: FragmentMovieDetailBinding
    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMovieDetailBinding.inflate(inflater, container, false)

    private var callback: MovieDetailCallback? = null
    private var collectionBean: CollectionBean? = null

    fun setCallback(callback: MovieDetailCallback) {
        this.callback = callback
    }

    override fun initView() {
        mBind = baseBinding as FragmentMovieDetailBinding
        LiveDataBus.get().with("adClose",String::class.java).observe(this, {
            if ("adClose" == it) mBind.adContainer.visibility = View.GONE
        })
    }

    /**
     * 顶部详情数据绑定
     */
    fun bindDetail(detailBean: MovieDetailBean) {
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
            " /  ${detailBean.movie.vodArea}  /  ${detailBean.movie.vodLang}"
        if (BridgeContext.TYPE_TV == detailBean.movie.movieFlag || BridgeContext.TYPE_TV0 == detailBean.movie.movieFlag) {
            //是电视类型
            mBind.layoutContainer.visibility = View.VISIBLE
            mBind.tvWhere.text = detailBean.movie.lastRemark
            if (detailBean.movieItems.size > 6) {
                mBind.tvWhere.visibility = View.VISIBLE
                mBind.tvWhere.setOnClickListener { callback?.onSelectClick(detailBean.movieItems) }
            } else {
                mBind.tvWhere.visibility = View.INVISIBLE
            }
            val adapter = MovieEpisodesTvAdapter(detailBean.movieItems as MutableList<MovieItem>)
            mBind.rvList.adapter = adapter
            for (i in detailBean.movieItems.indices) {
                if (detailBean.movieItems[i].isSelect) {
                    if (i != 0) (mBind.rvList.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
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
                callback?.onSelectClick(vid, detailBean.movie.strId, data[position].title)
            }
        } else {
            mBind.layoutContainer.visibility = View.GONE
        }

        if (BridgeContext.TYPE_ALBUM == detailBean.movie.movieFlag) {
            //综艺节目
            mBind.layoutZhuanJi.visibility = View.VISIBLE
            val adapter = MovieAlbumAdapter(detailBean.movieItems as MutableList<MovieItem>)
            mBind.rvAlbum.adapter = adapter
            for (i in detailBean.movieItems.indices) {
                if (detailBean.movieItems[i].isSelect) {
                    if (i != 0) (mBind.rvAlbum.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
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
                callback?.onSelectClick(vid, detailBean.movie.strId, data[position].title)
            }
            if (detailBean.movieItems.size > 5) {
                mBind.tvZWhere.visibility = View.VISIBLE
                mBind.tvZWhere.text = detailBean.movie.lastRemark
                mBind.tvZWhere.setOnClickListener { callback?.onArtSelectClick(detailBean.movieItems) }
            } else {
                mBind.tvZWhere.visibility = View.INVISIBLE
            }
        } else {
            mBind.layoutZhuanJi.visibility = View.GONE
        }
        //加载广告
        if (!StringUtils.isEmpty(AdvertBridge.MOVIE_DETAIL_BANNER)) {
            if (AdvertBridge.TT_AD == AdvertBridge.AD_TYPE) {
                initTTAd(AdvertBridge.MOVIE_DETAIL_BANNER)
            } else {
                initGDTAd(AdvertBridge.MOVIE_DETAIL_BANNER)
            }
        }
    }

    private var gdtBannerAd: GDTBannerAd? = null
    private var ttBanner: TTBannerAd? = null
    private fun initGDTAd(posId: String) {
        gdtBannerAd = GDTBannerAd()
        gdtBannerAd?.initBanner(requireActivity(), posId, mBind.adContainer)
    }

    private fun initTTAd(posId: String) {
        ttBanner = TTBannerAd()
        ttBanner?.initBanner(requireActivity(), posId, 0f, 0f, mBind.adContainer)
    }

    override fun onDestroy() {
        super.onDestroy()
        ttBanner?.onDestroy()
        gdtBannerAd?.onDestroy()
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