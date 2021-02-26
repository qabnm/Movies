package com.duoduovv.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.movie.bean.MovieLibListBean
import com.duoduovv.movie.repository.MovieRepository
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/1/21 15:28
 * @des: 片库列表
 */
class MovieLibListViewModel : BaseViewModel() {
    private var movieLibList: MutableLiveData<MovieLibListBean> = MutableLiveData()
    fun getMovieLibList() = movieLibList

    private val repository = MovieRepository()

    /**
     * 片库列表
     * @param map HashMap<String, Any>
     * @param page Int
     * @param typeId String
     * @return Job
     */
    fun movieLibList(map: HashMap<String, Any>, page: Int, typeId: String) = request {
        val result = repository.movieLibList(map, page, typeId)
        if (result.code == SUCCESS) movieLibList.postValue(result.data)
    }
}