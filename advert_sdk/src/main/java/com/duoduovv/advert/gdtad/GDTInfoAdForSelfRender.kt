package com.duoduovv.advert.gdtad

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.nativ.*
import com.qq.e.ads.nativ.widget.NativeAdContainer
import com.qq.e.comm.constants.AdPatternType
import com.qq.e.comm.util.AdError
import dc.android.tools.LiveDataBus

/**
 * @author: jun.liu
 * @date: 2021/6/21 13:51
 * @des:广点通信息流自渲染
 */
class GDTInfoAdForSelfRender {
    private var mAdData: NativeUnifiedADData? = null
    private var mAdManager: NativeUnifiedAD?=null
    private val TAG = "adLoad"

    /**
     * 广点通信息流广告
     * @param context Context
     * @param posId String
     * @param mImagePoster ImageView
     * @param mMediaView MediaView
     * @param layoutAd NativeAdContainer
     */
    fun initInfoAd(
        context: Context,
        posId: String,
        mImagePoster: ImageView,
        mMediaView: MediaView,
        layoutAd: NativeAdContainer
    ) {
        if (null == mAdManager){
            mAdManager = NativeUnifiedAD(context, posId, object : NativeADUnifiedListener {
                override fun onNoAD(error: AdError?) {
                    Log.d(TAG, "onNoAD${error?.errorCode}${error?.errorMsg}")
                }

                override fun onADLoaded(ads: MutableList<NativeUnifiedADData>?) {
                    if (ads?.isNotEmpty() == true) {
                        Log.d(TAG, "onADLoaded")
                        mAdData = ads[0]
                        mAdData?.let {
                            initAd(context, it, mImagePoster, mMediaView, layoutAd)
                        }
                    }
                }
            })
            mAdManager?.setMinVideoDuration(8)
            mAdManager?.setMaxVideoDuration(61)
        }
        mAdManager?.loadData(1)
    }

    private fun initAd(
        context: Context,
        ad: NativeUnifiedADData,
        mImagePoster: ImageView,
        mMediaView: MediaView,
        layoutAd: NativeAdContainer
    ) {
        Log.d(TAG, "${ad.adPatternType}")
        val imageViews = ArrayList<ImageView>()
        val clickableViews = ArrayList<View>()
        if (ad.adPatternType == AdPatternType.NATIVE_2IMAGE_2TEXT || ad.adPatternType == AdPatternType.NATIVE_1IMAGE_2TEXT) {
            clickableViews.add(mImagePoster)
            imageViews.add(mImagePoster)
        }
        ad.bindAdToView(context, layoutAd, null, clickableViews)
        when {
            imageViews.isNotEmpty() -> {
                layoutAd.visibility = View.VISIBLE
                mImagePoster.visibility = View.VISIBLE
                mMediaView.visibility = View.GONE
                ad.bindImageViews(imageViews, 0)
                LiveDataBus.get().with("videoLength").value = -1
            }
            ad.adPatternType == AdPatternType.NATIVE_VIDEO -> {
                layoutAd.visibility = View.VISIBLE
                //视频广告
                mImagePoster.visibility = View.GONE
                mMediaView.visibility = View.VISIBLE
                ad.bindMediaView(mMediaView, getVideoOption(), object : NativeADMediaListener {
                    override fun onVideoInit() {
                        Log.d(TAG, "onVideoInit")
                    }

                    override fun onVideoLoading() {
                        Log.d(TAG, "onVideoLoading")
                    }

                    override fun onVideoReady() {
                        Log.d(TAG, "onVideoReady")
                    }

                    override fun onVideoLoaded(videoDuration: Int) {
                        Log.d(TAG, "onVideoReady,广告时长${ad.videoDuration}")
                    }

                    override fun onVideoStart() {
                        Log.d(TAG, "onVideoStart")
                    }

                    override fun onVideoPause() {
                        Log.d(TAG, "onVideoPause")
                    }

                    override fun onVideoResume() {
                        Log.d(TAG, "onVideoResume")
                    }

                    override fun onVideoCompleted() {
                        Log.d(TAG, "onVideoCompleted")
//                        onDestroy()
                    }

                    override fun onVideoError(error: AdError?) {
                        Log.d(TAG, "onVideoError${error?.errorCode}${error?.errorMsg}")
                        onDestroy()
                    }

                    override fun onVideoStop() {
                        Log.d(TAG, "onVideoStop")
                    }

                    override fun onVideoClicked() {
                        Log.d(TAG, "onVideoClicked")
                    }
                })
            }
        }
    }

    private fun getVideoOption(): VideoOption {
        val builder = VideoOption.Builder()
        builder.apply {
            setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS)
            setAutoPlayMuted(true)
            setDetailPageMuted(false)
            setNeedCoverImage(true)
            setNeedProgressBar(true)
            setEnableDetailPage(true)
            setEnableUserControl(false)
        }
        return builder.build()
    }

    fun onDestroy() {
        mAdData?.destroy()
    }

    fun onResume() {
        mAdData?.resume()
    }
}