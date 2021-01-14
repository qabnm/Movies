package dc.android.bridge

/**
 * @author: jun.liu
 * @date: 2020/9/24 : 11:31
 */
open class BridgeContext {
    companion object{
        const val BASE_URL = "https://wanandroid.com/"

        const val NETWORK_ERROR = "网络连接异常"
        const val CONNECTION_ERROR = "连接异常"
        const val RUNTIME_ERROR="运行异常"
        const val TOKEN_ERROR = "登录超时"

        const val NOTIFICATION = "notification"

    }
}