package dc.android.bridge.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @author: jun.liu
 * @date: 2021/5/28 11:25
 * @des:
 */
class OtherFactory private constructor() {
    private val retrofit: Retrofit

    init {
        val builder = OkHttpClient.Builder()
        builder.apply {
            connectTimeout(15, TimeUnit.SECONDS)
            readTimeout(15, TimeUnit.SECONDS)
//            if (!OsUtils.isAppDebug()) proxy(Proxy.NO_PROXY)
//            addInterceptor(HeaderInterceptor())
        }
        retrofit =
            Retrofit.Builder().baseUrl("https://www.hahh.cn/").client(builder.build()).build()
    }

    fun <T> createRetrofit(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }

    companion object {
        val instance by lazy { OtherFactory() }
    }
}