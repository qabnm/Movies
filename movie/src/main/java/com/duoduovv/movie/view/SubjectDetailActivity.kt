package com.duoduovv.movie.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.util.RouterPath
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.MovieSubjectDetailAdapter
import com.duoduovv.movie.bean.SubjectDetailListBean
import com.duoduovv.movie.databinding.ActivitySubjectDetailBinding
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.TITLE
import dc.android.bridge.BridgeContext.Companion.WAY_VERIFY
import dc.android.bridge.view.BridgeActivity

/**
 * @author: jun.liu
 * @date: 2021/6/25 13:32
 * @des:专题详情页
 */
@Route(path = RouterPath.PATH_SUBJECT_DETAIL)
class SubjectDetailActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_subject_detail
    private lateinit var mbind: ActivitySubjectDetailBinding
    private var detailAdapter: MovieSubjectDetailAdapter? = null

    override fun initView() {
        mbind = ActivitySubjectDetailBinding.bind(layoutView)
        detailAdapter = MovieSubjectDetailAdapter()
        mbind.rvList.adapter = detailAdapter

        detailAdapter?.setOnItemClickListener { adapter, _, position ->
            val bean = (adapter as MovieSubjectDetailAdapter).data[position]
            onMovieClick(bean.strId, bean.way)
        }
    }

    override fun initData() {
        val title = intent.getStringExtra(TITLE) ?: "专题"
        mbind.layoutTopBar.setTopTitle(title)
        val dataList = ArrayList<SubjectDetailListBean>()
        val coverUrl =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimages.qdmama.net%2Fdata%2Fattachment%2Fforum%2F201609%2F19%2F111435bb1iqaq8vv39fw8b.png&refer=http%3A%2F%2Fimages.qdmama.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1627201636&t=badec1684a85f42ccc498845b42a4098"
        for (i in 0 until 9) {
            val bean = SubjectDetailListBean(coverUrl, "8.9", "123", "釜山行（2）", "1")
            dataList.add(bean)
        }
        detailAdapter?.setList(dataList)
    }

    /**
     * 跳转影视详情
     * @param movieId String
     */
    private fun onMovieClick(movieId: String, way: String) {
        val path = if (way == WAY_VERIFY) {
            RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG
        } else {
            RouterPath.PATH_MOVIE_DETAIL
        }
        ARouter.getInstance().build(path).withString(ID, movieId).navigation()
    }
}