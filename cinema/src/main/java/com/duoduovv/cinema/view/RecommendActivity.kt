package com.duoduovv.cinema.view

import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.cinema.R
import com.duoduovv.cinema.adapter.FilmRecommendAdapter
import com.duoduovv.cinema.bean.FilmRecommendBean
import com.duoduovv.cinema.databinding.ActivityRecommendBinding
import com.duoduovv.common.util.RouterPath
import dc.android.bridge.BridgeContext
import dc.android.bridge.view.BridgeActivity

/**
 * @author: jun.liu
 * @date: 2021/4/6 10:08
 * @des:今日推荐 热门推荐查看更多
 */
@Route(path = RouterPath.PATH_RECOMMEND)
class RecommendActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_recommend
    private var adapter: FilmRecommendAdapter? = null
    private lateinit var mBind:ActivityRecommendBinding

    override fun initView() {
        mBind = ActivityRecommendBinding.bind(layoutView)
        mBind.rvList.layoutManager = GridLayoutManager(this, 3)
        adapter = FilmRecommendAdapter(false)
        mBind.rvList.adapter = adapter
        adapter?.setOnItemClickListener { adapter, _, position ->
            val movieId = (adapter as FilmRecommendAdapter).data[position].strId
            val way = adapter.data[position].way
            onMovieClick(movieId, way)
        }
    }

    override fun initData() {
        val dataList: List<FilmRecommendBean> =
            intent.getParcelableArrayListExtra(BridgeContext.LIST) ?: ArrayList()
        adapter?.setList(dataList)
    }

    /**
     * 跳转影视详情
     * @param movieId String
     */
    private fun onMovieClick(movieId: String, way: String) {
        val path = if (way == BridgeContext.WAY_VERIFY) {
            RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG
        } else {
            RouterPath.PATH_MOVIE_DETAIL
        }
        ARouter.getInstance().build(path).withString(BridgeContext.ID, movieId).navigation()
    }
}