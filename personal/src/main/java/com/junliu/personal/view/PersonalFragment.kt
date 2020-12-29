package com.junliu.personal.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.common.util.RouterPath
import com.junliu.personal.R
import dc.android.bridge.view.BaseFragment

/**
 * @author: jun.liu
 * @date: 2020/12/29 : 14:49
 */
@Route(path = RouterPath.PATH_PERSONAL)
class PersonalFragment :BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_personal
}