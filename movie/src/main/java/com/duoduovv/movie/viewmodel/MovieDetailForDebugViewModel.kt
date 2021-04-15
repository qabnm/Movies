package com.duoduovv.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.movie.bean.MovieDetailForDebugBean
import com.duoduovv.movie.repository.MovieRepository
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/3/10 10:33
 * @des:审核版本影视详情
 */
class MovieDetailForDebugViewModel : BaseViewModel() {
    private var movieDetail: MutableLiveData<MovieDetailForDebugBean> = MutableLiveData()
    fun getMovieDetail() = movieDetail
    private val repository = MovieRepository()

    /**
     * 审核版的影视详情
     * @param movieId String
     * @return Job
     */
    fun movieDetailForDebug(movieId: String) = request {
        val result = repository.movieDetailForDebug(movieId)
        if (result.code == SUCCESS) movieDetail.postValue(result.data)
    }
}