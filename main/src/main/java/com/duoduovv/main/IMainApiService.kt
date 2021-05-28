package com.duoduovv.main

import com.duoduovv.common.domain.ConfigureBean
import dc.android.bridge.net.BaseResponseData
import retrofit2.http.GET

/**
 * @author: jun.liu
 * @date: 2021/5/28 18:24
 * @des:
 */
interface IMainApiService {

    /**
     * 获取配置信息
     */
    @GET("api/config")
    suspend fun configure(): BaseResponseData<ConfigureBean>
}