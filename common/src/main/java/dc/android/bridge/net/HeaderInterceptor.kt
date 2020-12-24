package dc.android.bridge.net

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author: jun.liu
 * @date: 2020/9/24 : 14:58
 */
class HeaderInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.apply {
            addHeader("", "")
            addHeader("", "")
        }
        return chain.proceed(builder.build())
    }
}