package com.duoduovv.main.view

import android.Manifest
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.component.AlertDialogFragment
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.SharedPreferencesHelper
import com.duoduovv.location.LocationHelper
import com.duoduovv.main.R
import com.duoduovv.main.component.PermissionDialogFragment
import com.duoduovv.main.component.PrivacyDialogFragment
import com.permissionx.guolindev.PermissionX
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.ADDRESS
import dc.android.bridge.BridgeContext.Companion.AGREEMENT
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
    override fun showStatusBarView() = false
    override fun setLayout(isStatusColorDark: Boolean, statusBarColor: Int) {
        super.setLayout(false, ContextCompat.getColor(this, R.color.colorTrans))
    }

    private var privacyDialogFragment: PrivacyDialogFragment? = null
    private var alertDialogFragment: AlertDialogFragment? = null
    private var locationHelper: LocationHelper? = null

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
                start()
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
//            AndroidUtils.toast("$province**$city**$district",this@SplashActivity)
            val dfProvince = SharedPreferencesHelper.helper.getValue(BridgeContext.PROVINCE, "") as String
            val dfCity = SharedPreferencesHelper.helper.getValue(BridgeContext.CITY, "") as String
            val dfArea = SharedPreferencesHelper.helper.getValue(BridgeContext.AREA, "") as String
            if (StringUtils.isEmpty(dfProvince)|| StringUtils.isEmpty(dfCity) || StringUtils.isEmpty(dfArea)) {
                //没有默认地址
                SharedPreferencesHelper.helper.setValue(
                    ADDRESS,
                    "{\"p\":\"${StringUtils.gbEncoding(province)}\",\"c\":\"${
                        StringUtils.gbEncoding(city)
                    }\",\"d\":\"${StringUtils.gbEncoding(district)}\",\"v\":${
                        OsUtils.getVerCode(BaseApplication.baseCtx)
                    },\"ch\":\"${AndroidUtils.getAppMetaData()}\"}"
                )
            }else{
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
//            SharedPreferencesHelper.helper.setValue(ADDRESS,
//                "{\"p\":\"${StringUtils.gbEncoding("湖北省")}\",\"c\":\"${
//                    StringUtils.gbEncoding("十堰")
//                }\",\"d\":\"${StringUtils.gbEncoding("房县")}\",\"v\":${
//                    OsUtils.getVerCode(BaseApplication.baseCtx)
//                },\"ch\":\"${AndroidUtils.getAppMetaData()}\"}"
//            )
            start()
        }

        override fun onLocationFail() {
            SharedPreferencesHelper.helper.remove(ADDRESS)
            start()
        }
    }

    /**
     * 跳转首页activity
     */
    private fun start() {
        locationHelper?.destroyLocation()
        Handler(Looper.getMainLooper()).postDelayed({
            ARouter.getInstance().build(RouterPath.PATH_MAIN).navigation()
            this.finish()
        }, 1000)
    }
}