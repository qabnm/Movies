package com.junliu.movie.repository

import com.junliu.movie.MovieApiService
import dc.android.bridge.net.BaseRepository
import dc.android.bridge.net.RetrofitFactory

/**
 * @author: jun.liu
 * @date: 2021/1/21 9:43
 * @des:
 */
open class MovieApiRepository :BaseRepository(){
    protected val apiService: MovieApiService by lazy {
        RetrofitFactory.instance.createRetrofit(MovieApiService::class.java)
    }
}