package com.duoduovv.common.viewmodel

import com.duoduovv.common.domain.ConfigureBean
import dc.android.bridge.net.BaseResponseData
import retrofit2.http.GET

/**
 * @author: jun.liu
 * @date: 2021/6/21 9:35
 * @des:
 */
interface IPublicApiService {
    /**
     * 获取配置信息
     */
    @GET("api/config")
    suspend fun configure(): BaseResponseData<ConfigureBean>
}