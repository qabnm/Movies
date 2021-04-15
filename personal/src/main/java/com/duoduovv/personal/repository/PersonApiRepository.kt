package com.duoduovv.personal.repository

import com.duoduovv.personal.IPersonApiService
import dc.android.bridge.net.BaseRepository
import dc.android.bridge.net.RetrofitFactory

/**
 * @author: jun.liu
 * @date: 2021/2/24 18:01
 * @des:
 */
open class PersonApiRepository :BaseRepository() {
    protected val apiService :IPersonApiService by lazy {
        RetrofitFactory.instance.createRetrofit(IPersonApiService::class.java)
    }
}