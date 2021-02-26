package com.junliu.movie.view

import androidx.recyclerview.widget.LinearLayoutManager
import com.junliu.movie.R
import com.junliu.movie.adapter.MovieRankAdapter
import com.junliu.movie.bean.MovieRankBean
import com.junliu.movie.viewmodel.MovieRankListViewModel
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
    }

    private fun setData(rankBean: MovieRankBean?) {
        val rankList = rankBean?.ranks
        if (rankList?.isNotEmpty() == true) {
            rankAdapter?.setList(rankList)
        }
    }

    override fun initData() {
        category = arguments?.getString(BridgeContext.ID, "") ?: ""
        viewModel.movieRankList(category)
    }

}