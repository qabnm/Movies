package com.duoduovv.main.view

import android.Manifest
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.component.AlertDialogFragment
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.SharedPreferencesHelper
import com.duoduovv.main.R
import com.duoduovv.main.component.PrivacyDialogFragment
import com.permissionx.guolindev.PermissionX
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.AGREEMENT
import dc.android.bridge.domain.LocationBean
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.util.LocationUtils
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
    private var privacyDialogFragment: PrivacyDialogFragment? = null
    private var alertDialogFragment: AlertDialogFragment? = null
    private var locationUtils: LocationUtils? = null

    override fun initData() {
        when (SharedPreferencesHelper.helper.getValue(AGREEMENT, false) as Boolean) {
            false -> {
                privacyDialogFragment = PrivacyDialogFragment(this)
                privacyDialogFragment?.showNow(supportFragmentManager, "privacy")
            }
            else -> toMainActivity()
        }
    }

    /**
     * 隐私政策弹窗点击暂不同意
     */
    override fun onDialogCancelClick() {
        privacyDialogFragment?.dismiss()
        alertDialogFragment = AlertDialogFragment("不同意将无法使用我们的产品和\n服务，并会退出App。", alertListener)
        alertDialogFragment?.let {
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
        toMainActivity()
    }

    private fun toMainActivity() {
        //跳转之前首先请求定位权限
        location()
    }

    private fun location() {
        PermissionX.init(this).permissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).onExplainRequestReason { scope, deniedList ->
            val msg = "多多影视需要获取您以下权限"
            scope.showRequestReasonDialog(deniedList, msg, "确定", "取消")
        }.onForwardToSettings { scope, deniedList ->
            val msg = "多多影视请求授予定位权限"
            scope.showForwardToSettingsDialog(deniedList, msg, "确定", "取消")
        }.request { allGranted, _, _ ->
            if (allGranted) {
                locationUtils = LocationUtils(locationListener)
                locationUtils?.startLocation()
            }
            start()
        }
    }

    private val locationListener = object : LocationUtils.LbsLocationListener {
        override fun onLocation(bean: LocationBean) {
            Log.i("address", "定位成功")
            locationUtils?.removeLocation()
            //将定位信息保存到本地
            SharedPreferencesHelper.helper.setValue(
                BridgeContext.ADDRESS,
                "{\"p\":\"${StringUtils.gbEncoding(bean.adminArea)}\",\"c\":\"${
                    StringUtils.gbEncoding(bean.locality)
                }\",\"d\":\"${StringUtils.gbEncoding(bean.subAdminArea)}\",\"v\":${
                    OsUtils.getVerCode(BaseApplication.baseCtx)
                },\"ch\":\"${AndroidUtils.getAppMetaData()}\"}"
            )
        }

        override fun gpsNotOpen() {
            AndroidUtils.toast("请打开GPS", this@SplashActivity)
        }
    }

    private fun start() {
        Handler(Looper.getMainLooper()).postDelayed({
            ARouter.getInstance().build(RouterPath.PATH_MAIN).navigation()
            this.finish()
        }, 1500)
    }
}