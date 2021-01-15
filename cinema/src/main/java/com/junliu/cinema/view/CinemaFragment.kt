package com.junliu.cinema.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.junliu.cinema.R
import com.junliu.common.util.RouterPath
import dc.android.bridge.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_cinema.*

/**
 * @author: jun.liu
 * @date: 2020/12/29 : 14:32
 * 首页
 */
@Route(path = RouterPath.PATH_CINEMA)
class CinemaFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_cinema

    override fun initView() {
        tvSearch.setOnClickListener {
            ARouter.getInstance().build(RouterPath.PATH_SEARCH_ACTIVITY).navigation()
        }
    }
}