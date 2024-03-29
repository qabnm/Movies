package dc.android.bridge.net

import dc.android.bridge.BridgeContext.Companion.BASE_URL
import dc.android.bridge.util.OsUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Proxy
import java.util.concurrent.TimeUnit

/**
 * @author: jun.liu
 * @date: 2020/9/24 : 11:30
 */
class RetrofitFactory private constructor() {
    private val retrofit: Retrofit

    init {
        val builder = OkHttpClient.Builder()
        builder.apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30,TimeUnit.SECONDS)
            if (!OsUtils.isAppDebug())proxy(Proxy.NO_PROXY)
            addInterceptor(HeaderInterceptor())
        }
        retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(builder.build()).build()
    }

    fun <T> createRetrofit(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }

    companion object {
        val instance by lazy { RetrofitFactory() }
    }
}