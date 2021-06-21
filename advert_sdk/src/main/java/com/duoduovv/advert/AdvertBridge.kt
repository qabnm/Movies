package com.duoduovv.advert

import android.content.Context
import android.util.Log
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.qq.e.comm.managers.GDTADManager

/**
 * @author: jun.liu
 * @date: 2021/4/27 14:22
 * @des:
 */
class AdvertBridge {
    companion object {
        //穿山甲的appId
        private const val ttAdAppId = "5169708"

        //腾讯广点通的appId
        private const val gdtAppId = "1111833726"
        private const val appName = "多多影视大全"
        /**
         * 初始化穿山甲SDK
         * 调整说明：穿山甲在3450版本对SDK的初始化方法进行了较大的改动，支持了同步初始化和异步初始化两种方式，
         * 并且TTAdConfig.Builder中支持异步初始化API将不再生效。优化后同步初始化和异步初始化两种方式的耗时没有显着差异，
         * 后者将部分初始化逻辑放到了子线程。开发者若使用异步初始化方法，请注意需要在success回调之后再去请求广告。
         * 目前穿山甲版本 3506
         * @param context Context
         */
        fun ttAdSdkInit(context: Context, isDebug: Boolean) {
            Log.d("ttAd","穿山甲初始化方法执行了")
            //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
            TTAdSdk.init(
                context, TTAdConfig.Builder().appId(ttAdAppId).
                    //默认使用SurfaceView播放视频广告,当有SurfaceView冲突的场景，可以使用TextureView
                useTextureView(true).appName(appName).
                    //落地页主题
                titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK).
                    //是否允许sdk展示通知栏
                allowShowNotify(true).debug(isDebug).
                    //允许直接下载的网络状态集合,没有设置的网络下点击下载apk会有二次确认弹窗，弹窗中会披露应用信息
                directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI).
                    //是否支持多进程
                supportMultiProcess(true).build(), object : TTAdSdk.InitCallback {
                    /**
                     * 初始化成功回调
                     * 注意：开发者需要在success回调之后再去请求广告
                     */
                    override fun success() {
                        Log.d("ttAd", "穿山甲SDK初始化成功")
                    }

                    /**
                     * 初始化失败
                     * @param code Int  初始化失败回调错误码
                     * @param message String   初始化失败回调信息
                     */
                    override fun fail(code: Int, message: String?) {
                        Log.d("ttAd", "穿山甲SDK初始化失败$code$message")
                    }
                }
            )
        }

        /**
         * 初始化腾讯广点通SDK
         * @param context Context
         */
        fun gdtInit(context: Context) {
            GDTADManager.getInstance().initWith(context, gdtAppId)
        }
    }
}