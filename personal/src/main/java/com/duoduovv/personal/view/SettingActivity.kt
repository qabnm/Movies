package com.duoduovv.personal.view

import android.annotation.SuppressLint
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.util.FileUtils
import com.duoduovv.common.util.RouterPath.Companion.PATH_CONTRACT_SERVICE_ACTIVITY
import com.duoduovv.common.util.RouterPath.Companion.PATH_SETTING_ACTIVITY
import com.duoduovv.common.util.SharedPreferencesHelper
import com.duoduovv.common.view.AlertDialogFragment
import com.duoduovv.common.view.UpgradeDialogFragment
import com.duoduovv.personal.R
import com.duoduovv.personal.bean.VersionBean
import com.duoduovv.personal.viewmodel.SettingViewModel
import dc.android.bridge.BridgeContext.Companion.NOTIFICATION
import dc.android.bridge.util.LoggerSnack
import dc.android.bridge.util.OsUtils
import dc.android.bridge.view.BaseViewModelActivity
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_setting.*
import kotlin.system.exitProcess

/**
 * @author: jun.liu
 * @date: 2020/12/30 15:35
 * @des： 个人中心设置页面
 */
@Route(path = PATH_SETTING_ACTIVITY)
class SettingActivity : BaseViewModelActivity<SettingViewModel>() {
    override fun getLayoutId() = R.layout.activity_setting
    override fun providerVMClass() = SettingViewModel::class.java
    private var isOpen = true
    private var upgradeDialogFragment: UpgradeDialogFragment? = null

    override fun initView() {
        layoutContract.setOnClickListener {
            ARouter.getInstance().build(PATH_CONTRACT_SERVICE_ACTIVITY).navigation()
        }
        imgNotification.setOnClickListener { onSwitchClick() }
        val localState = SharedPreferencesHelper.helper.getValue(NOTIFICATION, true) as Boolean
        imgNotification.setImageResource(if (localState) R.drawable.notification_open else R.drawable.notification_close)
        isOpen = localState
        layoutCheck.setOnClickListener { viewModel.upgrade() }
        viewModel.getUpgrade().observe(this, { onCheckSuccess(viewModel.getUpgrade().value) })
        viewModel.getProgress().observe(this, {
            val progress = viewModel.getProgress().value
            upgradeDialogFragment?.onProgressUpdate(progress ?: 0)
        })
        viewModel.getInstallState().observe(this, {
            val intent = viewModel.getInstallState().value
            intent?.let { startActivity(it) }
            exitProcess(1)
        })
    }

    private fun onCheckSuccess(bean: VersionBean?) {
        bean?.let {
            val versionCode = OsUtils.getVerCode(this)
            if (versionCode != -1 && it.version_number > versionCode) {
                //需要升级  弹出升级框
                upgradeDialogFragment = UpgradeDialogFragment(it.is_force, it.content, it.url)
                upgradeDialogFragment?.showNow(supportFragmentManager, "upgrade")
                upgradeDialogFragment?.setOnUpgradeClickListener(upgradeListener)
            } else {
                LoggerSnack.show(this, "已经是最新版本了！")
            }
        }
    }

    /**
     * 点击了升级
     */
    private val upgradeListener = object : UpgradeDialogFragment.OnUpgradeClickListener {
        override fun onUpgradeClick(url: String) {
            viewModel.downloadApk(url)
        }
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

    @SuppressLint("SetTextI18n")
    override fun initData() {
        tvVersion.text = "v${OsUtils.getVerName(BaseApplication.baseCtx)}"
        tvCache.text = FileUtils.getTotalCacheSize(BaseApplication.baseCtx)
        layoutClearCache.setOnClickListener {
            val dialogFragment = AlertDialogFragment("确定要清除缓存吗？", listener)
            dialogFragment.showNow(supportFragmentManager, "clear")
        }
    }

    private val listener = object : AlertDialogFragment.OnDialogSureClickListener {
        override fun onSureClick() {
            FileUtils.clearAllCache(BaseApplication.baseCtx)
            tvCache.text = "0KB"
        }
    }
}