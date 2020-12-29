package com.junliu.cinema.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.cinema.R
import com.junliu.common.util.RouterPath
import dc.android.bridge.view.BaseFragment

/**
 * @author: jun.liu
 * @date: 2020/12/29 : 14:32
 */
@Route(path = RouterPath.PATH_CINEMA)
class CinemaFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_cinema
}