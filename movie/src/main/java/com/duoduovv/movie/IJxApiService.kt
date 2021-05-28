package com.duoduovv.movie

import okhttp3.ResponseBody
import retrofit2.http.*

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
     * @return ResponseBody
     */
    @GET
    suspend fun jxUrlForGEet(
        @Url url: String,
        @HeaderMap headers: Map<String, String>
    ): ResponseBody

    /**
     * 解析源
     * @param url String
     * @param headers Map<String, String>
     * @param formParams Map<String, String>
     * @return ResponseBody
     */
    @FormUrlEncoded
    @POST
    suspend fun jxUrlForPost(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @FieldMap formParams: Map<String, String>
    ): ResponseBody
}