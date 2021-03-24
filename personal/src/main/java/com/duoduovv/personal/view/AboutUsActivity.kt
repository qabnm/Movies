package com.duoduovv.personal.view

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.view.UpgradeDialogFragment
import com.duoduovv.personal.PersonalContext
import com.duoduovv.personal.R
import com.duoduovv.personal.bean.VersionBean
import com.duoduovv.personal.viewmodel.SettingViewModel
import dc.android.bridge.BridgeContext
import dc.android.bridge.util.LoggerSnack
import dc.android.bridge.util.OsUtils
import dc.android.bridge.view.BaseViewModelActivity
import kotlinx.android.synthetic.main.activity_about_us.*
import kotlin.system.exitProcess

/**
 * @author: jun.liu
 * @date: 2021/3/24 10:13
 * @des:关于我们
 */
@Route(path = RouterPath.PATH_ABOUT_US)
class AboutUsActivity : BaseViewModelActivity<SettingViewModel>() {
    override fun getLayoutId() = R.layout.activity_about_us
    override fun providerVMClass() = SettingViewModel::class.java
    private var upgradeDialogFragment: UpgradeDialogFragment? = null
    private var bean:VersionBean?=null

    override fun initView() {
        layoutCheck.setOnClickListener { checkUpgrade() }
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
        layoutUserAgreement.setOnClickListener {
            //用户协议
            toWebActivity("用户协议", PersonalContext.URL_USER_AGREEMENT)
        }
        layoutPrivacy.setOnClickListener {
            //隐私政策
            toWebActivity("隐私政策", PersonalContext.URL_PRIVACY)
        }
    }

    /**
     * 跳转H5页面
     * @param title String
     * @param url String
     */
    private fun toWebActivity(title: String, url: String) {
        ARouter.getInstance().build(RouterPath.PATH_WEB_VIEW).withString(BridgeContext.TITLE, title)
            .withString(BridgeContext.URL, url).navigation()
    }

    private fun checkUpgrade(){
        //需要升级  弹出升级框
        bean?.let {
            val versionCode = OsUtils.getVerCode(this)
            if (versionCode != -1 && it.version_number > versionCode) {
                upgradeDialogFragment = UpgradeDialogFragment(it.is_force, it.content, it.url)
                upgradeDialogFragment?.showNow(supportFragmentManager, "upgrade")
                upgradeDialogFragment?.setOnUpgradeClickListener(upgradeListener)
            } else {
                LoggerSnack.show(this,"已经是最新版本了！")
            }
        }
    }

    private fun onCheckSuccess(bean: VersionBean?) {
        this.bean = bean
        bean?.let {
            val versionCode = OsUtils.getVerCode(this)
            if (versionCode != -1 && it.version_number > versionCode) {
                //显示升级小红点
                vDot.visibility = View.VISIBLE
            } else {
                vDot.visibility = View.GONE
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

    override fun initData() {
        viewModel.upgrade()
        tvVersion.text = "v${OsUtils.getVerName(BaseApplication.baseCtx)}"
    }
}