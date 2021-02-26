package com.junliu.download.download

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @author: jun.liu
 * @date: 2020/12/22 15:40
 * @des:文件下载
 */
interface DownLoadService {
    /**
     * @param start 从某个字节开始下载数据
     * @param url   文件下载的url
     * @Streaming 这个注解必须添加，否则文件全部写入内存，文件过大会造成内存溢出
     */
    @Streaming
    @GET
    suspend fun download(@Header("range") start: String?="0", @Url url: String?): Response<ResponseBody>
}