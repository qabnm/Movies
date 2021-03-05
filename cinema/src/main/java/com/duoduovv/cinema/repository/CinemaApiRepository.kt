package com.duoduovv.cinema.repository

import com.duoduovv.cinema.ICinemaApiService
import dc.android.bridge.net.BaseRepository
import dc.android.bridge.net.RetrofitFactory

/**
 * @author: jun.liu
 * @date: 2021/1/15 13:58
 * @des:
 */
open class CinemaApiRepository :BaseRepository(){
    protected val apiService :ICinemaApiService by lazy {
        RetrofitFactory.instance.createRetrofit(ICinemaApiService::class.java)
    }
}