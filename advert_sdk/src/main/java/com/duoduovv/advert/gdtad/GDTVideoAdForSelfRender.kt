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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author: jun.liu
 * @date: 2021/6/16 17:31
 * @des:广点通字渲染贴片广告
 */
class GDTVideoAdForSelfRender {
    private var mAdData: NativeUnifiedADData? = null
    private lateinit var mAdManager: NativeUnifiedAD
    private var mContainer: NativeAdContainer? = null
    private val TAG = "adLoad"

    fun initVideoAd(
        context: Context,
        posId: String,
        mImagePoster: ImageView,
        mMediaView: MediaView
    ) {
        mAdManager = NativeUnifiedAD(context, posId, object : NativeADUnifiedListener {
            override fun onNoAD(error: AdError?) {
                Log.d(TAG, "onNoAD${error?.errorCode}${error?.errorMsg}")
                onVideoPrepare()
            }

            override fun onADLoaded(ads: MutableList<NativeUnifiedADData>?) {
                if (ads?.isNotEmpty() == true) {
                    mAdData = ads[0]
                    mAdData?.let { initAd(it,mImagePoster, mMediaView) }
                }
            }
        })
        mAdManager.loadData(1)
    }

    private fun onVideoPrepare(){
        LiveDataBus.get().with("onAdComplete").value = "onAdComplete"
    }

   private fun initAd(ad: NativeUnifiedADData,mImagePoster: ImageView, mMediaView: MediaView) {
        if (ad.adPatternType == AdPatternType.NATIVE_VIDEO){
            GlobalScope.launch(Dispatchers.Main) {
                if (ad.adPatternType == AdPatternType.NATIVE_VIDEO){
                    //视频广告
                    mImagePoster.visibility = View.GONE
                    mMediaView.visibility = View.VISIBLE
                    ad.bindMediaView(mMediaView,getVideoOption(),object : NativeADMediaListener{
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
                            Log.d(TAG, "onVideoReady,广告时长$videoDuration")
                            LiveDataBus.get().with("videoLength").value = videoDuration
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
                }
            }
        }
    }

    private fun getVideoOption():VideoOption{
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

    fun onDestroy(){
        mAdData?.destroy()
    }

    fun onResume(){
        mAdData?.resume()
    }
}