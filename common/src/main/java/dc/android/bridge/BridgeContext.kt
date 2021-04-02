package dc.android.bridge

/**
 * @author: jun.liu
 * @date: 2020/9/24 : 11:31
 */
open class BridgeContext {
    companion object{

        const val BASE_URL = "https://www.duoduovv.cn/"

        const val NETWORK_ERROR = "网络连接异常"
        const val CONNECTION_ERROR = "连接异常"
        const val RUNTIME_ERROR="运行异常"
        const val TOKEN_ERROR = "登录超时"

        const val NOTIFICATION = "notification"
        const val ADDRESS = "address"
        const val ID = "id"
        const val LIST = "list"
        const val NO_MORE_DATA = "noMoreData"
        const val SUCCESS = 200
        const val TOKEN = "token"
        const val TYPE_ID = "typeId"
        const val TITLE = "title"
        const val URL = "url"
        const val WAY_RELEASE = 1
        const val WAY_H5 = 2
        const val WAY_VERIFY = 3
        const val AGREEMENT = "agree"
        const val CURRENT_LENGTH = "currentLength"
        const val WAY = "way"
        const val DEBUG_WAY = "debugWay"
        /**
         * 用户协议
         */
        const val URL_USER_AGREEMENT ="${BASE_URL}help/agreement.html"

        /**
         * 隐私政策
         */
        const val URL_PRIVACY ="${BASE_URL}help/privacy.html"
    }
}