package com.junliu.movie.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.common.util.RouterPath
import com.junliu.movie.R
import dc.android.bridge.view.BaseFragment

/**
 * @author: jun.liu
 * @date: 2020/12/29 : 14:37
 */
@Route(path = RouterPath.PATH_MOVIE)
class MovieFragment :BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_movie
}