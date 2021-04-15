package com.duoduovv.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.movie.bean.MovieRankCategoryBean
import com.duoduovv.movie.repository.MovieRepository
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/1/25 13:41
 * @des: 榜单排名分类
 */
class MovieRankCategoryViewModel : BaseViewModel() {

    private var movieRankCategory: MutableLiveData<MovieRankCategoryBean> = MutableLiveData()
    fun getMovieRankCategory() = movieRankCategory
    private val repository = MovieRepository()

    /**
     * 获取榜单排名分类
     * @return Job
     */
    fun movieRankCategory() = request {
        val result = repository.movieRankCategory()
        if (result.code == SUCCESS) movieRankCategory.postValue(result.data)
    }
}