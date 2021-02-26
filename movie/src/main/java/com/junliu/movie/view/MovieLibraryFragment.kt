package com.junliu.movie.view

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.junliu.movie.R
import com.junliu.movie.adapter.MovieLibraryAdapter
import com.junliu.movie.bean.Filter
import com.junliu.movie.bean.MovieLibList
import com.junliu.movie.viewmodel.MovieLibListViewModel
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

//        val db = AppDatabase.getInstance(BaseApplication.baseCtx)
//        val historyBean = VideoWatchHistoryBean(
//            coverUrl = "",
//            playUrl = "",
//            title = "",
//            type = "",
//            videoId = "",
//            where = "",
//            currentLength = 0,
//            time = 0
//        )
//        val insert = db.history().insert(historyBean)
//        val data = db.history().queryAll()
    }

    private fun setData(movies: List<MovieLibList>?) {
        if (movies?.isNotEmpty() == true && typeList?.isNotEmpty() == true) {
            movieLibAdapter?.takeIf { null == movieLibAdapter }?.also {
                movieLibAdapter = MovieLibraryAdapter(requireActivity(), typeList!!, movies)
                it.setItemClickListener(this)
                rvList.adapter = it
            } ?: run {
                movieLibAdapter?.notifyDataSetChanged()
            }
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