package com.duoduovv.personal.view

import android.text.TextUtils
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.component.UpgradeDialogFragment
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.SharedPreferencesHelper
import com.duoduovv.personal.R
import com.duoduovv.personal.bean.VersionBean
import com.duoduovv.personal.databinding.ActivityAboutUsBinding
import com.duoduovv.personal.viewmodel.SettingViewModel
import com.duoduovv.weichat.WeiChatTool
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.TOKEN
import dc.android.bridge.BridgeContext.Companion.URL_PRIVACY
import dc.android.bridge.BridgeContext.Companion.URL_USER_AGREEMENT
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.util.OsUtils
import dc.android.bridge.view.BaseViewModelActivity
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
    private lateinit var mBind: ActivityAboutUsBinding
    private var upgradeDialogFragment: UpgradeDialogFragment? = null
    private var bean: VersionBean? = null
    private var lastClickTime: Long = 0
    private var clickTime = 0

    override fun initView() {
        mBind = ActivityAboutUsBinding.bind(layoutView)
        mBind.layoutCheck.setOnClickListener { checkUpgrade() }
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
        mBind.layoutUserAgreement.setOnClickListener {
            //用户协议
            toWebActivity("用户协议", URL_USER_AGREEMENT)
        }
        mBind.layoutPrivacy.setOnClickListener {
            //隐私政策
            toWebActivity("隐私政策", URL_PRIVACY)
        }
        mBind.imgIcon.setOnClickListener { onIconClick(0) }
        mBind.tvLogoName.setOnClickListener { onIconClick(1) }
        if (OsUtils.isAppDebug()) {
            mBind.imgIcon.setOnLongClickListener {
                mBind.layoutDebug.visibility = View.VISIBLE
                mBind.tvSure.setOnClickListener {
                    if (!TextUtils.isEmpty(mBind.etInput.text)) {
                        SharedPreferencesHelper.helper.setValue(
                            BridgeContext.DEBUG_WAY,
                            mBind.etInput.text.toString()
                        )
                        SharedPreferencesHelper.helper.remove(TOKEN)
                        WeiChatTool.mTenCent?.logout(this)
                        AndroidUtils.toast("切换完成,请重新进入再试！", this)
                        mBind.layoutDebug.visibility = View.GONE
                    }
                }
                true
            }
            mBind.tvClear.setOnClickListener {
                SharedPreferencesHelper.helper.remove(BridgeContext.DEBUG_WAY)
                AndroidUtils.toast("清除成功", this)
            }
        }
    }

    private fun onIconClick(flag: Int) {
        fastClick()
        if (clickTime > 5) {
            if (flag == 0) {
                //显示渠道名称
                mBind.vLine.visibility = View.VISIBLE
                mBind.layoutWhere.visibility = View.VISIBLE
                mBind.tvWhere.text = AndroidUtils.getAppMetaData()
            } else {
                //显示位置切换功能
                ARouter.getInstance().build(RouterPath.PATH_CITY_SELECT).navigation()
            }
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

    /**
     * 检查升级
     */
    private fun checkUpgrade() {
        //需要升级  弹出升级框
        bean?.let {
            val versionCode = OsUtils.getVerCode(this)
            if (versionCode != -1 && it.versionNum > versionCode) {
                upgradeDialogFragment = UpgradeDialogFragment(it.isForce, it.content, it.url)
                upgradeDialogFragment?.showNow(supportFragmentManager, "upgrade")
                upgradeDialogFragment?.setOnUpgradeClickListener(upgradeListener)
            } else {
                AndroidUtils.toast("已经是最新版本了！", this)
            }
        } ?: also {
            AndroidUtils.toast("已经是最新版本了！", this)
        }
    }

    /**
     * 检查升级接口请求成功
     * @param bean VersionBean?
     */
    private fun onCheckSuccess(bean: VersionBean?) {
        this.bean = bean
        bean?.let {
            val versionCode = OsUtils.getVerCode(this)
            if (versionCode != -1 && it.versionNum > versionCode) {
                //显示升级小红点
                mBind.vDot.visibility = View.VISIBLE
            } else {
                mBind.vDot.visibility = View.GONE
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
        mBind.tvVersion.text = "v${OsUtils.getVerName(BaseApplication.baseCtx)}"
    }

    private fun fastClick() {
        val time = System.currentTimeMillis()
        if (time - lastClickTime < 500) {
            clickTime++
        } else {
            clickTime = 0
        }
        lastClickTime = time
    }
}