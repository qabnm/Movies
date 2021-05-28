package com.duoduovv.movie.repository

import com.duoduovv.movie.IJxApiService
import com.duoduovv.movie.IMovieApiService
import dc.android.bridge.net.BaseRepository
import dc.android.bridge.net.OtherFactory
import dc.android.bridge.net.RetrofitFactory

/**
 * @author: jun.liu
 * @date: 2021/1/21 9:43
 * @des:
 */
open class MovieApiRepository : BaseRepository() {
    protected val apiService: IMovieApiService by lazy {
        RetrofitFactory.instance.createRetrofit(IMovieApiService::class.java)
    }

    protected val jxApiService: IJxApiService by lazy {
        OtherFactory.instance.createRetrofit(IJxApiService::class.java)
    }
}