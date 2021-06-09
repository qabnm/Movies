package com.duoduovv.movie.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.util.RouterPath
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.MovieLibraryAdapter
import com.duoduovv.movie.bean.Filter
import com.duoduovv.movie.bean.MovieLibList
import com.duoduovv.movie.databinding.FragmentMovieLibraryBinding
import com.duoduovv.movie.viewmodel.MovieLibListViewModel
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.LIST
import dc.android.bridge.BridgeContext.Companion.NO_MORE_DATA
import dc.android.bridge.view.BaseViewModelFragment

/**
 * @author: jun.liu
 * @date: 2021/1/12 11:09
 * @des:片库fragment
 */
class MovieLibraryFragment : BaseViewModelFragment<MovieLibListViewModel>(),
    MovieLibraryAdapter.OnItemClickListener, OnRefreshListener, OnLoadMoreListener {
    override fun getLayoutId() = R.layout.fragment_movie_library
    override fun providerVMClass() = MovieLibListViewModel::class.java
    private lateinit var mBind: FragmentMovieLibraryBinding

    private var typeId = ""
    private var typeList: ArrayList<Filter>? = null
    private val map = HashMap<String, Any>()
    private var page = 1
    private var movieLibAdapter: MovieLibraryAdapter? = null

    override fun initView() {
        mBind = baseBinding as FragmentMovieLibraryBinding
        mBind.rvList.layoutManager = GridLayoutManager(requireActivity(), 3)
        viewModel.getMovieLibList().observe(this, {
            val result = viewModel.getMovieLibList().value
            setData(result)
        })
        viewModel.getNoMoreData().observe(this, { noMoreData(viewModel.getNoMoreData().value) })
        mBind.refreshLayout.apply {
            setRefreshHeader(ClassicsHeader(requireActivity()))
            setRefreshFooter(ClassicsFooter(requireActivity()))
            setOnRefreshListener(this@MovieLibraryFragment)
            setOnLoadMoreListener(this@MovieLibraryFragment)
        }
        movieLibAdapter = null
    }

    override fun initData() {
        typeId = arguments?.getString(ID, "") ?: ""
        typeList = arguments?.getParcelableArrayList(LIST)
        if (typeList?.isNotEmpty() == true) {
            for (i in typeList!!.indices) {
                map[typeList!![i].key] = typeList!![i].array[0].key
            }
        }
        viewModel.movieLibList(map, page, typeId)
    }

    private fun setData(movies: List<MovieLibList>?) {
        Log.i("typeList", "${typeList?.isNotEmpty()}")
        if (typeList?.isNotEmpty() == true) {
            Log.i(
                "typeList",
                "adapter::${null == movieLibAdapter}movie:&&&&${movies?.isNotEmpty()}"
            )
            if (null == movieLibAdapter) {
                movieLibAdapter = MovieLibraryAdapter(requireActivity(), typeList!!, movies)
                movieLibAdapter?.setItemClickListener(this)
                mBind.rvList.adapter = movieLibAdapter
            } else {
                movieLibAdapter?.notifyDataChanged(movies)
            }
        } else {

        }
        finishLoading()
    }

    override fun finishLoading() {
        if (mBind.refreshLayout.isRefreshing) mBind.refreshLayout.finishRefresh()
        if (mBind.refreshLayout.isLoading) mBind.refreshLayout.finishLoadMore()
    }

    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMovieLibraryBinding.inflate(inflater, container, false)

    /**
     * 筛选条件点击
     * @param key String 筛选分类的key  地区 时间
     * @param name String  点击的条件  大陆 美国
     */
    override fun onTypeClick(key: String, name: String) {
        page = 1
        map[key] = name
        viewModel.movieLibList(map, page, typeId)
    }

    /**
     * 点击影片 跳转详情
     * @param movieId String
     */
    override fun onMovieClick(movieId: String, way: String) {
        val path = if (way == BridgeContext.WAY_VERIFY) {
            RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG
        } else {
            RouterPath.PATH_MOVIE_DETAIL
        }
        ARouter.getInstance().build(path)
            .withString(ID, movieId).navigation()
    }

    /**
     * 下拉刷新
     * @param refreshLayout RefreshLayout
     */
    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        refreshLayout.resetNoMoreData()
        viewModel.movieLibList(map, page, typeId)
    }

    /**
     * 分页加载
     * @param refreshLayout RefreshLayout
     */
    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page++
        viewModel.movieLibList(map, page, typeId)
    }

    /**
     * 分页没有更多数据的通知
     * @param flag String?
     */
    private fun noMoreData(flag: String?) {
        if (NO_MORE_DATA == flag) {
            //没有更多数据了
            mBind.refreshLayout.apply {
                finishLoadMoreWithNoMoreData()
                setNoMoreData(true)
            }
        }
    }
}