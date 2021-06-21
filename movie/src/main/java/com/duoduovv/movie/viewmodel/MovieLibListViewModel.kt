package com.duoduovv.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.movie.bean.MovieLibList
import com.duoduovv.movie.repository.MovieRepository
import dc.android.bridge.BridgeContext.Companion.NO_MORE_DATA
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/1/21 15:28
 * @des: 片库列表
 */
class MovieLibListViewModel : BaseViewModel() {
    private var movieLibList: MutableLiveData<List<MovieLibList>> = MutableLiveData()
    fun getMovieLibList() = movieLibList
    private var noMoreData: MutableLiveData<String> = MutableLiveData()
    fun getNoMoreData() = noMoreData

    private val repository = MovieRepository()
    private val dataList = ArrayList<MovieLibList>()

    /**
     * 片库列表
     * @param map HashMap<String, Any>
     * @param page Int
     * @param typeId String
     * @return Job
     */
    fun movieLibList(map: HashMap<String, Any>, page: Int, typeId: String) = request {
        val result = repository.movieLibList(map, page, typeId)
        if (isSuccess(result.code)) {
            if (page == 1) dataList.clear()
            val list = result.data.movies
            if (list?.isNotEmpty() == true) {
                dataList.addAll(list)
                movieLibList.postValue(dataList)
            } else {
                if (page != 1){
                    noMoreData.postValue(NO_MORE_DATA)
                }else{
                    movieLibList.postValue(dataList)
                }
            }
        }
    }
}