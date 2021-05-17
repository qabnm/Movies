package com.duoduovv.main.view

import android.Manifest
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTSplashAd
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.component.AlertDialogFragment
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.SharedPreferencesHelper
import com.duoduovv.location.LocationHelper
import com.duoduovv.main.R
import com.duoduovv.main.component.PermissionDialogFragment
import com.duoduovv.main.component.PrivacyDialogFragment
import com.duoduovv.main.databinding.ActivitySplashBinding
import com.permissionx.guolindev.PermissionX
import com.qq.e.ads.splash.SplashAD
import com.qq.e.ads.splash.SplashADListener
import com.qq.e.comm.util.AdError
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.ADDRESS
import dc.android.bridge.BridgeContext.Companion.AGREEMENT
import dc.android.bridge.BridgeContext.Companion.GDT_AD_SPLASH_ID
import dc.android.bridge.BridgeContext.Companion.TT_AD_SPLASH_ID
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.util.OsUtils
import dc.android.bridge.util.StringUtils
import dc.android.bridge.view.BridgeActivity


/**
 * @author: jun.liu
 * @date: 2021/1/22 11:48
 * @des:启动页
 */
class SplashActivity : BridgeActivity(), PrivacyDialogFragment.OnDialogBtnClickListener {
    override fun getLayoutId() = R.layout.activity_splash
    private lateinit var mBind: ActivitySplashBinding
    override fun showStatusBarView() = false
    private val timeOut = 4000
    private var canJump = false
    override fun setLayout(isStatusColorDark: Boolean, statusBarColor: Int) {
        super.setLayout(false, ContextCompat.getColor(this, R.color.colorTrans))
    }

    private var privacyDialogFragment: PrivacyDialogFragment? = null
    private var alertDialogFragment: AlertDialogFragment? = null
    private var locationHelper: LocationHelper? = null

    override fun initView() {
        mBind = ActivitySplashBinding.bind(layoutView)
    }

    override fun initData() {
        when (SharedPreferencesHelper.helper.getValue(AGREEMENT, false) as Boolean) {
            false -> {
                privacyDialogFragment = PrivacyDialogFragment(this)
                privacyDialogFragment?.showNow(supportFragmentManager, "privacy")
            }
            else -> location()
        }
    }

    /**
     * 请求开屏广告
     */
    private fun initSplashAd() {
        //请求开屏广告
        //穿山甲开屏广告
        initTTSplashAd()
        //广点通的广告
//        initGDTSplash()
    }

    /**
     * 广点通开屏广告
     */
    private fun initGDTSplash() {
        //SDK不强制校验下列权限（即:无下面权限sdk也可正常工作），但建议开发者申请下面权限，尤其是READ_PHONE_STATE权限
        //READ_PHONE_STATE权限用于允许SDK获取用户标识
        //针对单媒体的用户，允许获取权限的，投放定向广告；不允许获取权限的用户，投放通投广告，媒体可以选择是否把用户标识数据提供给优量汇，并承担相应广告填充和eCPM单价下降损失的结果
        PermissionX.init(this).permissions(
            Manifest.permission.READ_PHONE_STATE
        ).onExplainRequestReason { scope, deniedList ->
            val msg = "多多影视请求获取设备信息"
            scope.showRequestReasonDialog(PermissionDialogFragment(deniedList, msg))
        }.onForwardToSettings { scope, deniedList ->
            val msg = "多多影视获取设备信息\n拒绝可能影响您的正常体验"
            scope.showForwardToSettingsDialog(PermissionDialogFragment(deniedList, msg))
        }.request { _, _, _ ->
            //不管有没有获取到想要的权限都请求广点通的广告
            val splashAD = SplashAD(
                this,
                GDT_AD_SPLASH_ID,
                object : SplashADListener {
                    /**
                     * 广告关闭时调用，可能是用户关闭或者展示时间到
                     */
                    override fun onADDismissed() {
                        Log.d("AD_DEMO", "onADDismissed")
                        next()
                    }

                    /**
                     * 广告加载失败
                     * @param error AdError
                     */
                    override fun onNoAD(error: AdError?) {
                        start()
                    }

                    /**
                     * 广告展示成功
                     */
                    override fun onADPresent() {
                        Log.d("AD_DEMO", "onADPresent")
                    }

                    /**
                     * 广告被点击时调用，不代表满足计费条件（如点击时网络异常）
                     */
                    override fun onADClicked() {
                        Log.d("AD_DEMO", "onADClicked")
                    }

                    /**
                     * 倒计时的回调
                     * @param p0 Long
                     */
                    override fun onADTick(p0: Long) {}

                    /**
                     * 广告曝光时调用
                     */
                    override fun onADExposure() {
                        Log.d("AD_DEMO", "onADExposure")
                    }

                    /**
                     * 广告加载成功的回调，在fetchAdOnly的情况下，表示广告拉取成功可以显示了。广告需要在SystemClock.elapsedRealtime <expireTimestamp前展示，
                     * 否则在showAd时会返回广告超时错误。
                     * @param expireTimestamp Long
                     */
                    override fun onADLoaded(expireTimestamp: Long) {
                        Log.d("AD_DEMO", "onADLoaded")
                    }
                },
                0
            )
            splashAD.fetchAndShowIn(mBind.adContainer)
        }
    }

    private fun next() {
        if (canJump) {
            start()
        } else {
            canJump = true
        }
    }

    override fun onPause() {
        super.onPause()
        canJump = false
    }

    override fun onResume() {
        super.onResume()
        if (canJump) next()
        canJump = true
    }


    /**
     * 穿山甲开屏广告
     */
    private fun initTTSplashAd() {
        val width = OsUtils.getScreenWidth(applicationContext)
        val totalHeight = OsUtils.getScreenHeight(applicationContext)
        val navHeight = OsUtils.getNavigationBarHeight(this)
        val height = totalHeight - navHeight - OsUtils.dip2px(this, 70f)
        // 创建TTAdNative对象，createAdNative(Context context) context需要传入Activity对象
        val mTTAdNative = TTAdSdk.getAdManager().createAdNative(this)

        //创建广告请求AdSlot
        val adSlot = AdSlot.Builder()
            .setCodeId(TT_AD_SPLASH_ID)
            .setImageAcceptedSize(width, height)
            .build()
        //加载开屏广告
        mTTAdNative.loadSplashAd(adSlot, object : TTAdNative.SplashAdListener {
            override fun onError(code: Int, msg: String?) {
                Log.d("ttAd", "开屏广告请求失败：$code${msg}codeId=${adSlot.codeId}")
                start()
            }

            override fun onTimeout() {
                Log.d("ttAd", "开屏广告请求超时")
                start()
            }

            override fun onSplashAdLoad(ad: TTSplashAd?) {
                ad?.let {
                    //获取SplashView
                    val splashView = it.splashView
                    if (!this@SplashActivity.isFinishing) {
                        Log.d("ttAd", "开屏广告请求成功")
                        mBind.adContainer.removeAllViews()
                        mBind.adContainer.addView(splashView)
                        it.setSplashInteractionListener(object : TTSplashAd.AdInteractionListener {
                            /**
                             * Splash广告的点击回调
                             *
                             * @param view Splash广告
                             * @param type Splash广告的交互类型
                             */
                            override fun onAdClicked(view: View?, type: Int) {}

                            /**
                             * Splash广告的展示回调
                             *
                             * @param view Splash广告
                             * @param type Splash广告的交互类型
                             */
                            override fun onAdShow(view: View?, type: Int) {}

                            /**
                             * 点击跳过时回调
                             */
                            override fun onAdSkip() {
                                start()
                            }

                            /**
                             * 广告播放时间结束
                             */
                            override fun onAdTimeOver() {
                                start()
                            }
                        })
                    } else {
                        start()
                    }
                }
            }

        }, timeOut)
    }

    /**
     * 隐私政策弹窗点击暂不同意
     */
    override fun onDialogCancelClick() {
        privacyDialogFragment?.dismiss()
        alertDialogFragment = AlertDialogFragment("不同意将无法使用我们的产品和\n服务，并会退出App。", 260f)
        alertDialogFragment?.let {
            it.setDialogClickListener(alertListener)
            it.showNow(supportFragmentManager, "alert")
            it.setTitleVisibility(View.GONE)
            it.setCancelText("不同意并退出")
            it.setSureText("同意并使用")
            it.setCanceledOnTouchOut(false)
            it.setCancel(false)
        }
    }

    private val alertListener = object : AlertDialogFragment.OnDialogSureClickListener {
        override fun onSureClick() {
            alertDialogFragment?.dismiss()
            onDialogSureClick()
        }

        override fun onCancelClick() {
            alertDialogFragment?.dismiss()
            this@SplashActivity.finish()
        }
    }

    /**
     * 同意隐私政策弹窗内容
     */
    override fun onDialogSureClick() {
        //保存一个标识位到本地
        SharedPreferencesHelper.helper.setValue(AGREEMENT, true)
        privacyDialogFragment?.dismiss()
        location()
    }

    /**
     * 申请定位权限
     */
    private fun location() {
        PermissionX.init(this).permissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).onExplainRequestReason { scope, deniedList ->
            val msg = "多多影视需要获取您的位置信息"
            scope.showRequestReasonDialog(PermissionDialogFragment(deniedList, msg))
        }.onForwardToSettings { scope, deniedList ->
            val msg = "多多影视需要定位权限\n拒绝可能影响您的正常体验"
            scope.showForwardToSettingsDialog(PermissionDialogFragment(deniedList, msg))
        }.request { allGranted, _, _ ->
            if (allGranted) {
                locationHelper = LocationHelper(BaseApplication.baseCtx, locationListener)
                locationHelper?.startLocation(OsUtils.isAppDebug())
            } else {
                SharedPreferencesHelper.helper.remove(ADDRESS)
                initSplashAd()
            }
        }
    }

    /**
     * 定位的监听
     */
    private val locationListener = object : LocationHelper.OnLocationListener {
        override fun onLocationChange(
            latitude: Double,
            longitude: Double,
            country: String,
            province: String,
            city: String,
            district: String,
            street: String,
            aioName: String
        ) {
            Log.d("address", "定位成功$province$city$district$street")
            val dfProvince =
                SharedPreferencesHelper.helper.getValue(BridgeContext.PROVINCE, "") as String
            val dfCity = SharedPreferencesHelper.helper.getValue(BridgeContext.CITY, "") as String
            val dfArea = SharedPreferencesHelper.helper.getValue(BridgeContext.AREA, "") as String
            if (StringUtils.isEmpty(dfProvince) || StringUtils.isEmpty(dfCity) || StringUtils.isEmpty(
                    dfArea
                )
            ) {
                //没有默认地址
                SharedPreferencesHelper.helper.setValue(
                    ADDRESS,
                    "{\"p\":\"${StringUtils.gbEncoding(province)}\",\"c\":\"${
                        StringUtils.gbEncoding(city)
                    }\",\"d\":\"${StringUtils.gbEncoding(district)}\",\"v\":${
                        OsUtils.getVerCode(BaseApplication.baseCtx)
                    },\"ch\":\"${AndroidUtils.getAppMetaData()}\"}"
                )
            } else {
                //有默认地址就用默认地址
                SharedPreferencesHelper.helper.setValue(
                    ADDRESS,
                    "{\"p\":\"${StringUtils.gbEncoding(dfProvince)}\",\"c\":\"${
                        StringUtils.gbEncoding(dfCity)
                    }\",\"d\":\"${StringUtils.gbEncoding(dfArea)}\",\"v\":${
                        OsUtils.getVerCode(BaseApplication.baseCtx)
                    },\"ch\":\"${AndroidUtils.getAppMetaData()}\"}"
                )
            }
            initSplashAd()
        }

        override fun onLocationFail() {
            SharedPreferencesHelper.helper.remove(ADDRESS)
            initSplashAd()
        }
    }

    /**
     * 跳转首页activity
     */
    private fun start() {
        locationHelper?.destroyLocation()
        ARouter.getInstance().build(RouterPath.PATH_MAIN).navigation()
        this.finish()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}