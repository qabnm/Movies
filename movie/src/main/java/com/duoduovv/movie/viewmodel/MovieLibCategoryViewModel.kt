package com.duoduovv.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.movie.bean.MovieLibCategoryBean
import com.duoduovv.movie.repository.MovieRepository
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/1/21 9:51
 * @des:片库获取分类及分类的筛选条件
 */
class MovieLibCategoryViewModel :BaseViewModel(){
    private var movieLibCategory: MutableLiveData<MovieLibCategoryBean> = MutableLiveData()
    fun getMovieLibCategory() = movieLibCategory
    private val repository = MovieRepository()

    /**
     * 获取片库顶部分类及不同分类的筛选条件
     * @return Job
     */
    fun movieLibCategory() = request {
        val result = repository.movieLibCategory()
        if (result.code == SUCCESS) movieLibCategory.postValue(result.data)
    }

}