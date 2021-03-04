package com.duoduovv.personal.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.util.RouterPath.Companion.PATH_CONTRACT_SERVICE_ACTIVITY
import com.duoduovv.common.util.RouterPath.Companion.PATH_SETTING_ACTIVITY
import com.duoduovv.common.util.SharedPreferencesHelper
import com.duoduovv.personal.R
import dc.android.bridge.BridgeContext.Companion.NOTIFICATION
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * @author: jun.liu
 * @date: 2020/12/30 15:35
 * @des： 个人中心设置页面
 */
@Route(path = PATH_SETTING_ACTIVITY)
class SettingActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_setting
    private var isOpen = true

    override fun initView() {
        layoutContract.setOnClickListener {
            ARouter.getInstance().build(PATH_CONTRACT_SERVICE_ACTIVITY).navigation()
        }
        imgNotification.setOnClickListener { onSwitchClick() }
        val localState = SharedPreferencesHelper.helper.getValue(NOTIFICATION, true) as Boolean
        imgNotification.setImageResource(if (localState) R.drawable.notification_open else R.drawable.notification_close)
        isOpen = localState
    }

    /**
     * 推送通知开关按钮
     */
    private fun onSwitchClick() {
        if (isOpen) {
            isOpen = false
            imgNotification.setImageResource(R.drawable.notification_close)
        } else {
            isOpen = true
            imgNotification.setImageResource(R.drawable.notification_open)
        }
    }

    override fun onPause() {
        super.onPause()
        //保存下开关的按钮状态
        SharedPreferencesHelper.helper.setValue(NOTIFICATION, isOpen)
    }

    override fun initData() {

    }
}