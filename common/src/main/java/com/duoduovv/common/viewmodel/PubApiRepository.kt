package com.duoduovv.common.viewmodel

import dc.android.bridge.net.BaseRepository
import dc.android.bridge.net.RetrofitFactory

/**
 * @author: jun.liu
 * @date: 2021/6/21 9:36
 * @des:
 */
open class PubApiRepository :BaseRepository() {
    protected val apiService: IPublicApiService by lazy {
        RetrofitFactory.instance.createRetrofit(IPublicApiService::class.java)
    }
}