package dc.android.bridge.net

import com.junliu.common.util.SharedPreferencesHelper
import dc.android.bridge.BridgeContext.Companion.ADDRESS
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author: jun.liu
 * @date: 2020/9/24 : 14:58
 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val userAgent = chain.request().header("User-Agent")
        val location = SharedPreferencesHelper.helper.getValue(ADDRESS, "")
        builder.addHeader("User-Agent", "$userAgent---$location")
        return chain.proceed(builder.build())
    }
}