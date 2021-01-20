package com.junliu.download.download

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @author: jun.liu
 * @date: 2020/12/21 16:37
 * @des:
 */
object RetrofitDownload {
    val downLoadService: DownLoadService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        val okHttpClient = createOkHttpClient()
        val retrofit = createRetrofit(okHttpClient)
        retrofit.create(DownLoadService::class.java)
    }

    private fun createRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://download")//baseUrl的填写满足http://或者https://开头且后面有内容就可以了
            .client(client)
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(8,TimeUnit.SECONDS).build()
    }
}