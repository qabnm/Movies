package com.duoduovv.main.view

import android.Manifest
import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.advert.gdtad.GDTSplashAd
import com.duoduovv.advert.ttad.TTSplashAds
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.component.AlertDialogFragment
import com.duoduovv.common.domain.ConfigureBean
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.SharedPreferencesHelper
import com.duoduovv.common.viewmodel.ConfigureViewModel
import com.duoduovv.location.LocationHelper
import com.duoduovv.main.R
import com.duoduovv.main.component.PermissionDialogFragment
import com.duoduovv.main.component.PrivacyDialogFragment
import com.duoduovv.main.databinding.ActivitySplashBinding
import com.permissionx.guolindev.PermissionX
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.ADDRESS
import dc.android.bridge.BridgeContext.Companion.ADDRESS_CH
import dc.android.bridge.BridgeContext.Companion.AGREEMENT
import dc.android.bridge.BridgeContext.Companion.AUTHORIZATION
import dc.android.bridge.BridgeContext.Companion.DATA
import dc.android.bridge.BridgeContext.Companion.TYPE_GDT_AD
import dc.android.bridge.BridgeContext.Companion.TYPE_TT_AD
import dc.android.bridge.EventContext
import dc.android.bridge.EventContext.Companion.EVENT_WAY_RELEASE
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.util.OsUtils
import dc.android.bridge.util.StringUtils
import dc.android.bridge.view.BaseViewModelActivity
import dc.android.tools.LiveDataBus
import java.util.*


/**
 * @author: jun.liu
 * @date: 2021/1/22 11:48
 * @des:启动页 获取配置接口相关信息
 */
class SplashActivity : BaseViewModelActivity<ConfigureViewModel>(),
    PrivacyDialogFragment.OnDialogBtnClickListener {
    override fun getLayoutId() = R.layout.activity_splash
    override fun providerVMClass() = ConfigureViewModel::class.java
    private lateinit var mBind: ActivitySplashBinding
    override fun showStatusBarView() = false
    override fun setLayout(isStatusColorDark: Boolean, statusBarColor: Int) {
        super.setLayout(false, ContextCompat.getColor(this, android.R.color.transparent))
    }

    private var privacyDialogFragment: PrivacyDialogFragment? = null
    private var alertDialogFragment: AlertDialogFragment? = null
    private var locationHelper: LocationHelper? = null
    private var gdtSplashAd: GDTSplashAd? = null
    private var configureBean: ConfigureBean? = null

    override fun initView() {
        mBind = ActivitySplashBinding.bind(layoutView)
        viewModel.getConfigure().observe(this, {
            initConfig(viewModel.getConfigure().value)
        })
    }

    override fun initData() {
        val authorization = SharedPreferencesHelper.helper.getValue(AUTHORIZATION, "") as? String
        if (StringUtils.isEmpty(authorization)) {
            //存一个
            val identity = UUID.randomUUID().toString()
            SharedPreferencesHelper.helper.setValue(AUTHORIZATION,identity)
        }
        when (SharedPreferencesHelper.helper.getValue(AGREEMENT, false) as Boolean) {
            false -> {
                privacyDialogFragment = PrivacyDialogFragment()
                privacyDialogFragment?.setDialogBtnClickListener(this)
                privacyDialogFragment?.showNow(supportFragmentManager, "privacy")
            }
            else -> {
                location()
            }
        }
    }

    override fun showLoading() {}

    override fun dismissLoading() {}
    override fun finishLoading() {
        initSplashAd()
    }

    /**
     * 初始化配置接口
     * @param configureBean ConfigureBean?
     */
    private fun initConfig(configureBean: ConfigureBean?) {
        configureBean?.let {
            SharedPreferencesHelper.helper.setValue(BridgeContext.WAY, it.way)
            this.configureBean = it
            BaseApplication.configBean = it
            initSplashAd()
            //统计可播放的way
            if (it.way == BridgeContext.WAY_RELEASE) {
                Log.d("WAY_RELEASE", it.way)
                val location = SharedPreferencesHelper.helper.getValue(ADDRESS_CH, "") as String
                val map = mapOf("location" to location, "phone" to Build.MODEL)
                EventContext.uMenEvent(EVENT_WAY_RELEASE, map)
            }
        }
    }

    private var ttSplashAds: TTSplashAds? = null

    /**
     * 请求开屏广告
     */
    private fun initSplashAd() {
        LiveDataBus.get().with("start", String::class.java).observe(this, {
            if ("start" == it) start()
        })
        configureBean?.ad?.splash?.let {
            when (it.type) {
                TYPE_TT_AD -> {
                    ttSplashAds = TTSplashAds()
                    ttSplashAds?.initTTSplashAd(this, it.value, 4000, mBind.adContainer)
                }
                TYPE_GDT_AD -> {
                    initGDTSplash(it.value)
                }
                else -> start()
            }
        } ?: run {
            start()
        }
    }

    /**
     * 广点通开屏广告
     */
    private fun initGDTSplash(posId: String) {
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
            gdtSplashAd = GDTSplashAd()
            gdtSplashAd?.initGDTSplash(this, mBind.adContainer, posId)
        }
    }

    override fun onPause() {
        super.onPause()
        gdtSplashAd?.setCanJump(false)
    }

    override fun onResume() {
        super.onResume()
        if (gdtSplashAd?.getCanJump() == true) gdtSplashAd?.next()
        gdtSplashAd?.setCanJump(true)
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
                SharedPreferencesHelper.helper.remove(ADDRESS_CH)
                viewModel.configure()
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
                SharedPreferencesHelper.helper.setValue(
                    ADDRESS_CH, "{\"p\":\"$province\",\"c\":\"$city" +
                            "\",\"d\":\"$district\",\"v\":${
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
                SharedPreferencesHelper.helper.setValue(
                    ADDRESS_CH, "{\"p\":\"$dfProvince\",\"c\":\"$dfCity" +
                            "\",\"d\":\"$dfArea\",\"v\":${
                                OsUtils.getVerCode(BaseApplication.baseCtx)
                            },\"ch\":\"${AndroidUtils.getAppMetaData()}\"}"
                )
            }
            viewModel.configure()
        }

        override fun onLocationFail() {
            SharedPreferencesHelper.helper.remove(ADDRESS)
            SharedPreferencesHelper.helper.remove(ADDRESS_CH)
            viewModel.configure()
        }
    }

    override fun onDestroy() {
        mBind.adContainer.removeAllViews()
        super.onDestroy()
    }

    /**
     * 跳转首页activity
     */
    private fun start() {
        locationHelper?.destroyLocation()
        ARouter.getInstance().build(RouterPath.PATH_MAIN).withParcelable(DATA, configureBean)
            .navigation()
//        ARouter.getInstance().build(RouterPath.PATH_MOVIE_DETAIL).withString(BridgeContext.ID, "818953").navigation()
//        mBind.adContainer.removeAllViews()
        ttSplashAds?.onDestroy()
        ttSplashAds = null
        gdtSplashAd = null
        this.finish()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}