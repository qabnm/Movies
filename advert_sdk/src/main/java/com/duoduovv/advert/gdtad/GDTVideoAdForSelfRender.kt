package com.duoduovv.advert.gdtad

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.nativ.*
import com.qq.e.ads.nativ.widget.NativeAdContainer
import com.qq.e.comm.constants.AdPatternType
import com.qq.e.comm.util.AdError
import dc.android.tools.LiveDataBus

/**
 * @author: jun.liu
 * @date: 2021/6/16 17:31
 * @des:广点通字渲染贴片广告
 */
class GDTVideoAdForSelfRender {
    private var mAdData: NativeUnifiedADData? = null
    private lateinit var mAdManager: NativeUnifiedAD
    private val TAG = "adLoad"

    fun initVideoAd(
        context: Context,
        posId: String,
        mImagePoster: ImageView,
        mMediaView: MediaView,
        layoutAd: NativeAdContainer
    ) {
        mAdManager = NativeUnifiedAD(context, posId, object : NativeADUnifiedListener {
            override fun onNoAD(error: AdError?) {
                Log.d(TAG, "onNoAD${error?.errorCode}${error?.errorMsg}")
                onVideoPrepare()
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
        mAdManager.setMinVideoDuration(8)
        mAdManager.setMaxVideoDuration(61)
        mAdManager.loadData(1)
    }

    private fun onVideoPrepare() {
        LiveDataBus.get().with("onAdComplete").value = "onAdComplete"
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
                        LiveDataBus.get().with("videoLength").value = ad.videoDuration
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
                        onVideoPrepare()
                        onDestroy()
                    }

                    override fun onVideoError(error: AdError?) {
                        Log.d(TAG, "onVideoError${error?.errorCode}${error?.errorMsg}")
                        onVideoPrepare()
                        onDestroy()
                    }

                    override fun onVideoStop() {
                        Log.d(TAG, "onVideoStop")
                    }

                    override fun onVideoClicked() {
                        Log.d(TAG, "onVideoClicked")
                    }
                })
                ad.setNativeAdEventListener(object : NativeADEventListener {
                    override fun onADExposed() {
                        Log.d(TAG, "onADExposed")
                    }

                    override fun onADClicked() {
                        Log.d(TAG, "onADClicked")
                    }

                    override fun onADError(error: AdError?) {
                        Log.d(TAG, "onADError${error?.errorCode}${error?.errorMsg}")
                    }

                    override fun onADStatusChanged() {
                        Log.d(TAG, "onADStatusChanged")
                    }
                })
            }
            else -> {
                onVideoPrepare()
            }
        }
    }

    private fun getVideoOption(): VideoOption {
        val builder = VideoOption.Builder()
        builder.apply {
            setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS)
            setAutoPlayMuted(false)
            setDetailPageMuted(false)
            setNeedCoverImage(true)
            setNeedProgressBar(true)
            setEnableDetailPage(true)
            setEnableUserControl(false)
        }
        return builder.build()
    }

    fun onConfigurationChanged(container: FrameLayout,context: Context){
        val height = Resources.getSystem().displayMetrics.widthPixels.coerceAtMost(Resources.getSystem().displayMetrics.heightPixels)
        container.post {
            val configuration = context.resources.configuration
            val layoutParams: ViewGroup.LayoutParams = container.layoutParams
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutParams.height = height
            } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                layoutParams.height = (height*9/16f).toInt()
            }
            container.layoutParams = layoutParams
        }
    }

    fun onDestroy() {
        mAdData?.destroy()
    }

    fun onResume() {
        mAdData?.resume()
    }
}