package com.duoduovv.movie.view

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.MovieLibraryAdapter
import com.duoduovv.movie.bean.Filter
import com.duoduovv.movie.bean.MovieLibList
import com.duoduovv.movie.viewmodel.MovieLibListViewModel
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.LIST
import dc.android.bridge.view.BaseViewModelFragment
import kotlinx.android.synthetic.main.fragment_movie_library.*

/**
 * @author: jun.liu
 * @date: 2021/1/12 11:09
 * @des:片库fragment
 */
class MovieLibraryFragment : BaseViewModelFragment<MovieLibListViewModel>(),
    MovieLibraryAdapter.OnItemClickListener {
    override fun getLayoutId() = R.layout.fragment_movie_library
    override fun providerVMClass() = MovieLibListViewModel::class.java

    private var typeId = ""
    private var typeList: ArrayList<Filter>? = null
    private val map = HashMap<String, Any>()
    private var page = 1
    private var movieLibAdapter: MovieLibraryAdapter? = null

    override fun initView() {
        rvList.layoutManager = GridLayoutManager(requireActivity(), 3)
        viewModel.getMovieLibList().observe(this, Observer {
            val result = viewModel.getMovieLibList().value
            setData(result?.movies)
        })
    }

    override fun initData() {
        typeId = arguments?.getString(ID, "") ?: ""
        typeList = arguments?.getParcelableArrayList(LIST)
        if (typeList?.isNotEmpty() == true) {
            for (i in typeList!!.indices) {
                map[typeList!![i].key] = ""
            }
        }
        viewModel.movieLibList(map, page, typeId)
    }

    private fun setData(movies: List<MovieLibList>?) {
        if (typeList?.isNotEmpty() == true) {
            if (null == movieLibAdapter){
                movieLibAdapter = MovieLibraryAdapter(requireActivity(), typeList!!, movies)
                movieLibAdapter!!.setItemClickListener(this)
                rvList.adapter = movieLibAdapter
            }else{
                movieLibAdapter?.notifyDataChanged(movies)
            }
        }else{

        }
    }

    /**
     * 筛选条件点击
     * @param key String 筛选分类的key  地区 时间
     * @param name String  点击的条件  大陆 美国
     */
    override fun onTypeClick(key: String, name: String) {
        map[key] = name
        viewModel.movieLibList(map, page, typeId)
    }
}