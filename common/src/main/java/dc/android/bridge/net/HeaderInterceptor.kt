package dc.android.bridge.net

import android.webkit.WebSettings
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.util.SharedPreferencesHelper
import dc.android.bridge.BridgeContext.Companion.ADDRESS
import dc.android.bridge.BridgeContext.Companion.TOKEN
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author: jun.liu
 * @date: 2020/9/24 : 14:58
 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val userAgent = WebSettings.getDefaultUserAgent(BaseApplication.baseCtx)
        val location = SharedPreferencesHelper.helper.getValue(ADDRESS, "")
        builder.addHeader("User-Agent", "$userAgent---$location")
        builder.addHeader("Authorization", SharedPreferencesHelper.helper.getValue(TOKEN, "") as? String?:"")
        return chain.proceed(builder.build())
    }
}