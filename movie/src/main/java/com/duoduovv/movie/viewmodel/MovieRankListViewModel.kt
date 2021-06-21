package com.duoduovv.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.movie.bean.MovieRankBean
import com.duoduovv.movie.repository.MovieRepository
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/1/25 14:06
 * @des:榜单列表
 */
class MovieRankListViewModel :BaseViewModel(){
    private var movieRankList:MutableLiveData<MovieRankBean> = MutableLiveData()
    fun getMovieRankList() = movieRankList
    private val repository = MovieRepository()

    /**
     * 榜单列表
     * @param column String  分类id
     * @return Job
     */
    fun movieRankList(column:String) = request {
        val result = repository.movieRankList(column = column)
       if (isSuccess(result.code)) movieRankList.postValue(result.data)
    }
}