package com.junliu.hotspot.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.common.util.RouterPath
import com.junliu.hotspot.R
import dc.android.bridge.view.BaseFragment

/**
 * @author: jun.liu
 * @date: 2020/12/29 : 14:34
 */
@Route(path = RouterPath.PATH_HOTSPOT)
class HotSpotFragment :BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_hotspot
}