package dc.android.bridge.net

import android.webkit.WebSettings
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.util.SharedPreferencesHelper
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.ADDRESS
import dc.android.bridge.BridgeContext.Companion.TOKEN
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.util.OsUtils
import dc.android.bridge.util.StringUtils
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
        var location = SharedPreferencesHelper.helper.getValue(ADDRESS, "") as? String
        val emptyStr = ""
        if (StringUtils.isEmpty(location)) {
            //还没有定位权限  拼接一个完整的json
            location = "{\"p\":\"${emptyStr}\",\"c\":\"${emptyStr}\",\"d\":\"${emptyStr}\",\"v\":${
                OsUtils.getVerCode(BaseApplication.baseCtx)
            },\"ch\":\"${AndroidUtils.getAppMetaData()}\"}"
        }
        if (OsUtils.isAppDebug()) {
            builder.addHeader(
                "WAYLEVEL",
                SharedPreferencesHelper.helper.getValue(BridgeContext.DEBUG_WAY, "1") as String
            )
        }
        builder.addHeader("User-Agent", "$userAgent---$location")
        builder.addHeader(
            "Authorization",
            SharedPreferencesHelper.helper.getValue(TOKEN, "") as? String ?: ""
        )
        return chain.proceed(builder.build())
    }
}