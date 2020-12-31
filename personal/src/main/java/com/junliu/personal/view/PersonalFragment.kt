package com.junliu.personal.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.junliu.common.util.RouterPath.Companion.PATH_CONTRACT_SERVICE_ACTIVITY
import com.junliu.common.util.RouterPath.Companion.PATH_PERSONAL
import com.junliu.common.util.RouterPath.Companion.PATH_SETTING_ACTIVITY
import com.junliu.personal.R
import dc.android.bridge.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_personal.*

/**
 * @author: jun.liu
 * @date: 2020/12/29 : 14:49
 * 个人中心
 */
@Route(path = PATH_PERSONAL)
class PersonalFragment :BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_personal

    override fun initView() {
        layoutHistory.setOnClickListener {  }
        layoutDownload.setOnClickListener {  }
        layoutCollection.setOnClickListener {  }
        layoutShare.setOnClickListener {  }
        layoutContract.setOnClickListener {
            ARouter.getInstance().build(PATH_CONTRACT_SERVICE_ACTIVITY).navigation()
        }
        layoutSetting.setOnClickListener {
            ARouter.getInstance().build(PATH_SETTING_ACTIVITY).navigation()
        }
        layoutAbout.setOnClickListener {  }
    }
}