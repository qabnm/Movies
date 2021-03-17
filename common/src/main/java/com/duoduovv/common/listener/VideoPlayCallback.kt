package com.duoduovv.common.listener

import android.util.Log
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack

/**
 * @author: jun.liu
 * @date: 2021/1/7 : 15:28
 * 播放过程中的监听
 */
open class VideoPlayCallback : VideoAllCallBack {
    private val TAG = "videoPlayer"
    override fun onStartPrepared(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onStartPrepared")
    }

    override fun onPrepared(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onPrepared")
    }

    override fun onClickStartIcon(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onClickStartIcon")
    }

    override fun onClickStartError(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onClickStartError")
    }

    override fun onClickStop(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onClickStop")
    }

    override fun onClickStopFullscreen(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onClickStopFullscreen")
    }

    override fun onClickResume(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onClickResume")
    }

    override fun onClickResumeFullscreen(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onClickResumeFullscreen")
    }

    override fun onClickSeekbar(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onClickSeekbar")
    }

    override fun onClickSeekbarFullscreen(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onClickSeekbarFullscreen")
    }

    override fun onAutoComplete(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onAutoComplete")
    }

    override fun onComplete(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onComplete")
    }

    override fun onEnterFullscreen(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onEnterFullscreen")
    }

    override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onQuitFullscreen")
    }

    override fun onQuitSmallWidget(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onQuitSmallWidget")
    }

    override fun onEnterSmallWidget(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onEnterSmallWidget")
    }

    override fun onTouchScreenSeekVolume(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onTouchScreenSeekVolume")
    }

    override fun onTouchScreenSeekPosition(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onTouchScreenSeekPosition")
    }

    override fun onTouchScreenSeekLight(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onTouchScreenSeekLight")
    }

    override fun onPlayError(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onPlayError${url}****${objects[0]}")
    }

    override fun onClickStartThumb(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onClickStartThumb${url}****${objects[0]}")
    }

    override fun onClickBlank(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onClickBlank")
    }

    override fun onClickBlankFullscreen(url: String?, vararg objects: Any?) {
        Log.d(TAG, "onClickBlankFullscreen")
    }
}