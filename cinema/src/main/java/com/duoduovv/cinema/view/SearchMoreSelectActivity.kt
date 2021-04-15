package com.duoduovv.cinema.view

import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.cinema.R
import com.duoduovv.cinema.adapter.SearchAlbumEpisodesAdapter
import com.duoduovv.cinema.adapter.SearchTvEpisodesAdapter
import com.duoduovv.cinema.bean.MovieItem
import com.duoduovv.common.util.RouterPath
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.TYPE_TV
import dc.android.bridge.BridgeContext.Companion.TYPE_TV0
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
    private var title = ""
    private var movieId = ""
    private var dataList: List<MovieItem>? = null
    private var way = 0
    private var movieFlag = ""

    override fun initView() {
        movieFlag = intent.getStringExtra(BridgeContext.FLAG) ?: ""
        rvList.layoutManager = GridLayoutManager(this, if (movieFlag == TYPE_TV|| movieFlag == TYPE_TV0) 5 else 2)
    }

    override fun initData() {
        way = intent.getIntExtra(BridgeContext.WAY, 0)
        title = intent.getStringExtra(BridgeContext.TITLE) ?: ""
        dataList = intent.getParcelableArrayListExtra(BridgeContext.LIST)
        movieId = intent.getStringExtra(BridgeContext.ID) ?: ""
        layoutTopBar.setTopTitle(title)
        val rvAdapter = if (movieFlag == TYPE_TV|| movieFlag == TYPE_TV0) {
            SearchTvEpisodesAdapter(dataList as MutableList<MovieItem>)
        } else {
            SearchAlbumEpisodesAdapter(dataList as MutableList<MovieItem>)
        }
        rvList.adapter = rvAdapter
        rvAdapter.setOnItemClickListener { adapter, _, position ->
            val data = (adapter.data[position] as MovieItem)
            onItemClick(data.vid)
        }
    }

    private fun onItemClick(vid: String) {
        val path = if (way == BridgeContext.WAY_VERIFY) {
            RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG
        } else {
            RouterPath.PATH_MOVIE_DETAIL
        }
        ARouter.getInstance().build(path)
            .withString(BridgeContext.ID, movieId).withString(BridgeContext.TYPE_ID, vid)
            .navigation()
    }
}