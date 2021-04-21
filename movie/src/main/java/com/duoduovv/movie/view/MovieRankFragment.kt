package com.duoduovv.movie.view

import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.SharedPreferencesHelper
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.MovieRankAdapter
import com.duoduovv.movie.bean.MovieRankBean
import com.duoduovv.movie.viewmodel.MovieRankListViewModel
import dc.android.bridge.BridgeContext
import dc.android.bridge.view.BaseViewModelFragment
import kotlinx.android.synthetic.main.fragment_movie_rank.*

/**
 * @author: jun.liu
 * @date: 2021/1/12 11:10
 * @des:榜单页面
 */
class MovieRankFragment : BaseViewModelFragment<MovieRankListViewModel>() {
    override fun getLayoutId() = R.layout.fragment_movie_rank
    override fun providerVMClass() = MovieRankListViewModel::class.java

    private var rankAdapter: MovieRankAdapter? = null
    private var category = ""

    override fun initView() {
        viewModel.getMovieRankList().observe(this, { setData(viewModel.getMovieRankList().value) })
        rvList.layoutManager = LinearLayoutManager(requireActivity())
        rankAdapter = MovieRankAdapter()
        rvList.adapter = rankAdapter
        rankAdapter?.setOnItemClickListener { adapter, _, position ->
            val movieId = (adapter as MovieRankAdapter).data[position].strId
            val way = adapter.data[position].way
            val path = if (way == BridgeContext.WAY_VERIFY) {
                RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG
            } else {
                RouterPath.PATH_MOVIE_DETAIL
            }
            ARouter.getInstance().build(path)
                .withString(BridgeContext.ID, movieId).navigation()
        }
    }

    private fun setData(rankBean: MovieRankBean?) {
        dismissLoading()
        val rankList = rankBean?.ranks
        if (rankList?.isNotEmpty() == true) {
            rankAdapter?.setList(rankList)
        }
    }

    override fun initData() {
        showLoading()
        category = arguments?.getString(BridgeContext.ID, "") ?: ""
        viewModel.movieRankList(category)
    }
}