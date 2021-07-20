package com.duoduovv.movie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.util.RouterPath
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.MovieRankAdapter
import com.duoduovv.movie.bean.MovieRankBean
import com.duoduovv.movie.databinding.FragmentMovieRankBinding
import com.duoduovv.movie.viewmodel.MovieRankListViewModel
import com.umeng.analytics.MobclickAgent
import dc.android.bridge.BridgeContext
import dc.android.bridge.EventContext
import dc.android.bridge.view.BaseViewModelFragment

/**
 * @author: jun.liu
 * @date: 2021/1/12 11:10
 * @des:榜单页面
 */
class MovieRankFragment : BaseViewModelFragment<MovieRankListViewModel>() {
    override fun getLayoutId() = R.layout.fragment_movie_rank
    override fun providerVMClass() = MovieRankListViewModel::class.java
    private lateinit var mBind: FragmentMovieRankBinding
    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMovieRankBinding.inflate(inflater, container, false)

    private var rankAdapter: MovieRankAdapter? = null
    private var category = ""

    override fun initView() {
        mBind = baseBinding as FragmentMovieRankBinding
        viewModel.getMovieRankList().observe(this, { setData(viewModel.getMovieRankList().value) })
        mBind.rvList.layoutManager = LinearLayoutManager(requireActivity())
        rankAdapter = MovieRankAdapter()
        mBind.rvList.adapter = rankAdapter
        rankAdapter?.setOnItemClickListener { adapter, _, position ->
            val movieId = (adapter as MovieRankAdapter).data[position].strId
            val way = adapter.data[position].way
            val path = if (way == BridgeContext.WAY_VERIFY) {
                RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG
            } else {
                RouterPath.PATH_MOVIE_DETAIL
            }
            ARouter.getInstance().build(path).withString(BridgeContext.ID, movieId).navigation()
            MobclickAgent.onEventObject(BaseApplication.baseCtx,EventContext.EVENT_RANK_TO_DETAIL,null)
        }
    }

    companion object{
        @JvmStatic
        fun newInstance(id:String):MovieRankFragment{
            val fragment = MovieRankFragment()
            val bundle = Bundle()
            bundle.putString(BridgeContext.ID, id)
            fragment.arguments = bundle
            return fragment
        }
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