package com.duoduovv.movie.view

import android.widget.LinearLayout
import com.duoduovv.movie.R
import dc.android.bridge.util.OsUtils
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_movie_detail_for_debug.*

/**
 * @author: jun.liu
 * @date: 2021/3/1 10:23
 * @des:审核版用的影视详情页
 */
class MovieDetailActivityForDebug :BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_movie_detail_for_debug
    override fun showStatusBarView() = false

    override fun initView() {
        val layoutParams = layoutTopBar.layoutParams as LinearLayout.LayoutParams
        layoutParams.topMargin = OsUtils.getStatusBarHeight(this)
    }
}