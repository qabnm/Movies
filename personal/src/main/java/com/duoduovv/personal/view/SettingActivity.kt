package com.duoduovv.personal.view

import android.annotation.SuppressLint
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.component.AlertDialogFragment
import com.duoduovv.common.util.FileUtils
import com.duoduovv.common.util.RouterPath.Companion.PATH_SETTING_ACTIVITY
import com.duoduovv.common.util.SharedPreferencesHelper
import com.duoduovv.personal.R
import com.duoduovv.personal.databinding.ActivitySettingBinding
import dc.android.bridge.BridgeContext.Companion.NOTIFICATION
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.BridgeContext.Companion.TOKEN
import dc.android.bridge.util.StringUtils
import dc.android.bridge.view.BridgeActivity
import dc.android.tools.LiveDataBus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: jun.liu
 * @date: 2020/12/30 15:35
 * @des： 个人中心设置页面
 */
@Route(path = PATH_SETTING_ACTIVITY)
class SettingActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_setting
    private lateinit var mBind: ActivitySettingBinding
    private var isOpen = true

    override fun initView() {
        mBind = ActivitySettingBinding.bind(layoutView)
        mBind.layoutContract.setOnClickListener { FeedbackAPI.openFeedbackActivity() }
        mBind.imgNotification.setOnClickListener { onSwitchClick() }
        val localState = SharedPreferencesHelper.helper.getValue(NOTIFICATION, true) as Boolean
        mBind.imgNotification.setImageResource(if (localState) R.drawable.notification_open else R.drawable.notification_close)
        isOpen = localState
    }

    /**
     * 推送通知开关按钮
     */
    private fun onSwitchClick() {
        if (isOpen) {
            isOpen = false
            mBind.imgNotification.setImageResource(R.drawable.notification_close)
        } else {
            isOpen = true
            mBind.imgNotification.setImageResource(R.drawable.notification_open)
        }
    }

    override fun onPause() {
        super.onPause()
        //保存下开关的按钮状态
        SharedPreferencesHelper.helper.setValue(NOTIFICATION, isOpen)
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        val token = SharedPreferencesHelper.helper.getValue(TOKEN, "") as String
        if (!StringUtils.isEmpty(token)) {
            mBind.btnLogout.visibility = View.VISIBLE
            mBind.btnLogout.setOnClickListener { logout() }
        } else {
            mBind.btnLogout.visibility = View.GONE
        }
        mBind.tvCache.text = FileUtils.getTotalCacheSize(BaseApplication.baseCtx)
        mBind.layoutClearCache.setOnClickListener {
            val dialogFragment = AlertDialogFragment("确定要清除缓存吗？", 250f)
            dialogFragment.apply {
                setDialogClickListener(LogoutListener(this, "clear"))
                showNow(supportFragmentManager, "clear")
                setTitleVisibility(View.GONE)
            }
        }
    }

    /**
     * 退出登录
     */
    private fun logout() {
        val dialogFragment = AlertDialogFragment("确定要退出登录吗？", 250f)
        dialogFragment.apply {
            setDialogClickListener(LogoutListener(this, "logout"))
            showNow(supportFragmentManager, "logout")
            setTitleVisibility(View.GONE)
        }
    }

    private inner class LogoutListener(
        val dialog: AlertDialogFragment?,
        val flag: String
    ) : AlertDialogFragment.OnDialogSureClickListener {
        override fun onSureClick() {
            dialog?.let {
                if ("clear" == flag) {
                    FileUtils.clearAllCache(BaseApplication.baseCtx)
                    mBind.tvCache.text = "0KB"
                    it.dismiss()
                } else {
                    GlobalScope.launch(Dispatchers.Main) {
                        withContext(Dispatchers.IO) {
                            SharedPreferencesHelper.helper.remove(TOKEN)
                        }
                        LiveDataBus.get().with("logout").value = SUCCESS
                        it.dismiss()
                        this@SettingActivity.finish()
                    }
                }
            }
        }

        override fun onCancelClick() {
            dialog?.dismiss()
        }
    }
}