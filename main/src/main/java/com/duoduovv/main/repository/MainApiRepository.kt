package com.duoduovv.main.repository

import com.duoduovv.main.IMainApiService
import dc.android.bridge.net.BaseRepository
import dc.android.bridge.net.RetrofitFactory

/**
 * @author: jun.liu
 * @date: 2021/5/28 18:23
 * @des:
 */
open class MainApiRepository : BaseRepository() {
    protected val apiService: IMainApiService by lazy {
        RetrofitFactory.instance.createRetrofit(IMainApiService::class.java)
    }
}