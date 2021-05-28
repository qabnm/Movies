package com.duoduovv.movie

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Url

/**
 * @author: jun.liu
 * @date: 2021/5/28 11:29
 * @des:
 */
interface IJxApiService {
    /**
     * 如果是解析源 需要先请求三方的地址
     * @param url String
     * @param headers Map<String, String>
     * @return Any
     */
    @GET
    suspend fun jxUrl(@Url url:String, @HeaderMap headers:Map<String,String>): ResponseBody
}