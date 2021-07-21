package com.duoduovv.movie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.util.RouterPath
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
import com.umeng.analytics.MobclickAgent
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.LIST
import dc.android.bridge.BridgeContext.Companion.NO_MORE_DATA
import dc.android.bridge.EventContext
import dc.android.bridge.view.BaseViewModelFragment

/**
 * @author: jun.liu
 * @date: 2021/1/12 11:09
 * @des:片库fragment
 */
class MovieLibraryFragment : BaseViewModelFragment<MovieLibListViewModel>(),
    MovieLibraryAdapter.OnItemClickListener, OnRefreshListener, OnLoadMoreListener {
    override fun providerVMClass() = MovieLibListViewModel::class.java
    private lateinit var mBind: FragmentMovieLibraryBinding

    private var typeId = ""
    private var typeList: ArrayList<Filter>? = null
    private val map = HashMap<String, Any>()
    private var page = 1
    private var movieLibAdapter: MovieLibraryAdapter? = null

    companion object{
        @JvmStatic
        fun newInstance(key:String, filter: ArrayList<Filter>):MovieLibraryFragment{
            val fragment = MovieLibraryFragment()
            val bundle = Bundle()
            bundle.putString(ID,key)
            bundle.putParcelableArrayList(LIST, filter)
            fragment.arguments = bundle
            return fragment
        }
    }

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
    }

    override fun onDestroyView() {
        movieLibAdapter?.onDestroy()
        movieLibAdapter = null
        super.onDestroyView()
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
        if (typeList?.isNotEmpty() == true) {
            if (null == movieLibAdapter) {
                movieLibAdapter = MovieLibraryAdapter(requireActivity(), typeList!!, movies)
                movieLibAdapter?.setItemClickListener(this)
                mBind.rvList.adapter = movieLibAdapter
            } else {
                movieLibAdapter?.notifyDataChanged(movies)
            }
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
        ARouter.getInstance().build(path).withString(ID, movieId).navigation()
        MobclickAgent.onEventObject(BaseApplication.baseCtx,EventContext.EVENT_MOVIELIB_TO_DETAIL,null)
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