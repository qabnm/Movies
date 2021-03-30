package com.duoduovv.cinema.view

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.cinema.R
import com.duoduovv.cinema.adapter.SearchEpisodesAdapter
import com.duoduovv.cinema.bean.MovieItem
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.SharedPreferencesHelper
import dc.android.bridge.BridgeContext
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_search_more_select.*

/**
 * @author: jun.liu
 * @date: 2021/3/30 13:57
 * @des:搜索结果更多点击
 */
@Route(path = RouterPath.PATH_SEARCH_MORE_SELECT)
class SearchMoreSelectActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_search_more_select
    private var tvAdapter: SearchEpisodesAdapter? = null
    private var title = ""
    private var movieId = ""
    private var dataList: List<MovieItem>? = null

    override fun initView() {
        rvList.layoutManager = GridLayoutManager(this, 5)
    }

    override fun initData() {
        title = intent.getStringExtra(BridgeContext.TITLE) ?: ""
        dataList = intent.getParcelableArrayListExtra(BridgeContext.LIST)
        movieId = intent.getStringExtra(BridgeContext.ID) ?: ""
        layoutTopBar.setTopTitle(title)
        tvAdapter = SearchEpisodesAdapter(dataList as MutableList<MovieItem>)
        rvList.adapter = tvAdapter
        tvAdapter?.setOnItemClickListener { adapter, _, position ->
            val data = (adapter as SearchEpisodesAdapter).data[position]
            onItemClick(data.vid)
        }
    }

    private fun onItemClick(vid: String) {
        val flag = SharedPreferencesHelper.helper.getValue(BridgeContext.isRes, 1)
        val path = if (flag == 1) {
            RouterPath.PATH_MOVIE_DETAIL
        } else {
            RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG
        }
        ARouter.getInstance().build(path)
            .withString(BridgeContext.ID, movieId).withString(BridgeContext.TYPE_ID, vid)
            .navigation()
    }

}