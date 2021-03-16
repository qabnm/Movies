package com.duoduovv.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.movie.bean.MovieDetailBean
import com.duoduovv.movie.bean.MoviePlayInfoBean
import com.duoduovv.movie.repository.MovieRepository
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
    private var addCollectionState: MutableLiveData<Int> = MutableLiveData()
    fun getAddState() = addCollectionState
    private var deleteCollectionState: MutableLiveData<Int> = MutableLiveData()
    fun getDeleteState() = deleteCollectionState
    private var moviePlayInfo: MutableLiveData<MoviePlayInfoBean> = MutableLiveData()
    fun getMoviePlayInfo() = moviePlayInfo

    //以下是点击播放  直接可以播放
    private var movieClickInfo:MutableLiveData<MoviePlayInfoBean> = MutableLiveData()
    fun getMovieClickInfo() = movieClickInfo

    private val repository = MovieRepository()

    /**
     * 视频详情
     * @param id String  视频ID
     * @param num String  播放级数
     * @return Job
     */
    fun movieDetail(id: String, vid: String = "") = request {
        val result = repository.movieDetail(id = id, vid = vid)
        if (result.code == SUCCESS) movieDetail.postValue(result.data)
    }

    /**
     * 获取播放信息
     * @param vid String
     * @param id String
     * @return Job
     */
    fun moviePlayInfo(vid: String, id: String,flag:Int = 0) = request {
        val result = repository.moviePlayInfo(vid, id)
        if (result.code == SUCCESS){
            if (flag == 0){
                moviePlayInfo.postValue(result.data)
            }else{
                movieClickInfo.postValue(result.data)
            }
        }
    }

    /**
     * 添加收藏
     * @param movieId String
     * @return Job
     */
    fun addCollection(movieId: String) = request {
        val result = repository.addCollection(movieId)
        if (result.code == SUCCESS) addCollectionState.postValue(200)
    }

    /**
     * 删除收藏
     * @param movieId String
     * @return Job
     */
    fun deleteCollection(movieId: String) = request {
        val result = repository.deleteCollection(movieId)
        if (result.code == SUCCESS) deleteCollectionState.postValue(200)
    }
}