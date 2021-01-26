package com.junliu.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import com.junliu.movie.bean.MovieDetailBean
import com.junliu.movie.repository.MovieRepository
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/1/26 17:24
 * @des: 视频详情
 */
class MovieDetailViewModel : BaseViewModel() {
    private var movieDetail: MutableLiveData<MovieDetailBean> = MutableLiveData()
    fun getMovieDetail() = movieDetail
    private val repository = MovieRepository()

    /**
     * 视频详情
     * @param id String  视频ID
     * @param num String  播放级数
     * @return Job
     */
    fun movieDetail(id: String, num: String) = request {
        val result = repository.movieDetail(id = id, num = num)
        if (result.code == SUCCESS) movieDetail.postValue(result.data)
    }
}