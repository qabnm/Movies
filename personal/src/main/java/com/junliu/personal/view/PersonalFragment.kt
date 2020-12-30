package com.junliu.personal.view

import android.content.Intent
import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.common.util.RouterPath
import com.junliu.personal.R
import dc.android.bridge.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_personal.*

/**
 * @author: jun.liu
 * @date: 2020/12/29 : 14:49
 */
@Route(path = RouterPath.PATH_PERSONAL)
class PersonalFragment :BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_personal

    override fun initView() {
        tvMine.setOnClickListener {
            startActivity(Intent(requireActivity() , SettingActivity::class.java))
        }
    }
}