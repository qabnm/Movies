package com.duoduovv.cinema.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.cinema.bean.ColumnBean
import com.duoduovv.cinema.bean.MovieMoreList
import com.duoduovv.cinema.repository.CinemaRepository
import dc.android.bridge.BridgeContext.Companion.NO_MORE_DATA
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/1/15 14:33
 * @des:首页列表
 */
class CinemaListViewModel : BaseViewModel() {
    private val cinemaList = MutableLiveData<List<ColumnBean>>()
    private var noMoreData: MutableLiveData<String> = MutableLiveData()
    private val movieMore = MutableLiveData<List<MovieMoreList>>()
    private val dataSet = ArrayList<ColumnBean>()
    private val dataMovieMore = ArrayList<MovieMoreList>()
    private val repository = CinemaRepository()
    fun getCinemaList() = cinemaList
    fun getNoMoreData() = noMoreData
    fun getMovieMore() = movieMore

    //    /**
//     * 合并首页三个请求接口
//     * @param page Int
//     */
//    fun main(page: Int, column: String) = request {
//        val result1: Deferred<BaseResponseData<MainPageBean>?> = async {
//            try {
//                repository.mainPage(column)
//            } catch (e: Exception) {
//                getException().value = e
//                null
//            }
//        }
//        val result2: Deferred<BaseResponseData<MainRecommendBean>?> = async {
//            try {
//                repository.mainRecommend(page, column)
//            } catch (e: Exception) {
//                getException().value = e
//                null
//            }
//        }
//        if (isSuccess(result1.await()?.code) && isSuccess(result2.await()?.code)) {
//            dataList.clear()
//            val list = result2.await()!!.data.recommends
//            if (list?.isNotEmpty() == true) dataList.addAll(list)
//            mainBean.postValue(MainBean(result1.await()!!.data, result2.await()!!.data))
//        }
//    }
    /**
     * 首页栏目查看更多
     * @param id String
     * @param page Int
     */
    fun movieMore(id: String, page: Int)= request {
        val result = repository.movieMoreList(id, page)
        if (isSuccess(result.code)){
            if (page == 1)dataMovieMore.clear()
            val data = result.data.movies
            if (data?.isNotEmpty() == true){
                dataMovieMore.addAll(data)
                movieMore.postValue(dataMovieMore)
            }else{
                if (page != 1){
                    noMoreData.postValue(NO_MORE_DATA)
                }else{
                    movieMore.postValue(dataMovieMore)
                }
            }
        }
    }

    /**
     * 首页栏目列表
     * @param page Int
     * @param column String
     * @return Job
     */
    fun cinemaList(page: Int, column: String) = request {
        val result = repository.cinemaList(page, column)
        if (isSuccess(result.code)) {
            if (page == 1) dataSet.clear()
            val data = result.data.columns
            if (data?.isNotEmpty() == true) {
                dataSet.addAll(data)
                cinemaList.postValue(dataSet)
            } else {
                if (page != 1) {
                    noMoreData.postValue(NO_MORE_DATA)
                } else {
                    cinemaList.postValue(dataSet)
                }
            }
        }
    }
}