package dc.android.bridge

/**
 * @author: jun.liu
 * @date: 2020/9/24 : 11:31
 */
open class BridgeContext {
    companion object{

        const val NETWORK_ERROR = "网络连接异常"
        const val CONNECTION_ERROR = "连接异常"
        const val RUNTIME_ERROR="运行异常"
        const val TOKEN_ERROR = "登录超时"
        const val NOTIFICATION = "notification"
        const val DATA = "data"

        const val ADDRESS = "address"
        const val ADDRESS_CH = "address_ch"
        const val ID = "id"
        const val LIST = "list"
        const val NO_MORE_DATA = "noMoreData"
        const val SUCCESS = 200
        const val TOKEN = "token"
        const val TYPE_ID = "typeId"
        const val TITLE = "title"
        const val URL = "url"
        const val WAY_RELEASE = "1"
        const val WAY_H5 = "2"
        const val WAY_VERIFY = "3"
        const val AGREEMENT = "agree"
        const val WAY = "way"
        const val DEBUG_WAY = "debugWay"
        const val FLAG = "flag"
        const val TYPE_MOVIE = "1"
        const val TYPE_TV0 = "0"
        const val TYPE_TV = "2"
        const val TYPE_ALBUM = "3"
        const val PROVINCE = "province"
        const val CITY = "city"
        const val AREA = "area"
        
        //以下是广告相关的
        /**
         * 穿山甲广告
         */
        const val TYPE_TT_AD = 1

        /**
         * 广点通广告
         */
        const val TYPE_GDT_AD = 2

        const val BASE_URL = "https://www.duoduovv.cn/"

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