package com.duoduovv.main.view

import android.os.Handler
import android.os.Looper
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.SharedPreferencesHelper
import com.duoduovv.common.view.AlertDialogFragment
import com.duoduovv.main.R
import com.duoduovv.main.component.PrivacyDialogFragment
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.AGREEMENT
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
    private var alertDialogFragment:AlertDialogFragment?= null

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
            it.showNow(supportFragmentManager,"alert")
            it.setTitleVisibility(View.GONE)
            it.setCancelText("不同意并退出")
            it.setSureText("同意并使用")
            it.setCanceledOnTouchOut(false)
            it.setCancel(false)
        }
    }
    private val alertListener = object :AlertDialogFragment.OnDialogSureClickListener{
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
        Handler(Looper.getMainLooper()).postDelayed({
            ARouter.getInstance().build(RouterPath.PATH_MAIN).navigation()
            this.finish()
        },2000)
    }
}