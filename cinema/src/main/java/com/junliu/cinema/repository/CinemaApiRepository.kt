package com.junliu.cinema.repository

import com.junliu.cinema.CinemaApiService
import dc.android.bridge.net.BaseRepository
import dc.android.bridge.net.RetrofitFactory

/**
 * @author: jun.liu
 * @date: 2021/1/15 13:58
 * @des:
 */
open class CinemaApiRepository :BaseRepository(){
    protected val apiService :CinemaApiService by lazy {
        RetrofitFactory.instance.createRetrofit(CinemaApiService::class.java)
    }
}