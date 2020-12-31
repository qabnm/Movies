package com.junliu.personal.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.junliu.common.util.RouterPath.Companion.PATH_CONTRACT_SERVICE_ACTIVITY
import com.junliu.common.util.RouterPath.Companion.PATH_SETTING_ACTIVITY
import com.junliu.personal.R
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

    override fun initData() {
        layoutContract.setOnClickListener {
            ARouter.getInstance().build(PATH_CONTRACT_SERVICE_ACTIVITY).navigation()
        }
    }
}