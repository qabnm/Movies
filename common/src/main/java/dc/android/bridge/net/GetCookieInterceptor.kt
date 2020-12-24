package dc.android.bridge.net

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.lang.StringBuilder

/**
 * @author: jun.liu
 * @date: 2020/9/27 : 17:28
 */
class GetCookieInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val headers: List<String> = response.headers("Set-Cookie")
        if (headers.isNotEmpty()) {
            val builder = StringBuilder()
            for (index in headers.indices) {
                builder.append(headers[index]).append(if (index == headers.size - 1) "" else ";")
            }
            val result = builder.toString()
            Log.i("cookie", result)
        }
        return response
    }
}