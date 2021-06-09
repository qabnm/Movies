package dc.android.bridge.net

import android.util.Log
import com.duoduovv.common.util.SharedPreferencesHelper
import dc.android.bridge.BridgeContext.Companion.TOKEN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author: jun.liu
 * @date: 2020/9/25 : 16:37
 */
open class BaseRepository {
    suspend fun <T : Any> request(call: suspend () -> BaseResponseData<T>): BaseResponseData<T> {
        return withContext(Dispatchers.IO) {
            call.invoke()
        }.apply {
            //这儿可以对返回结果errCode做一些特殊处理，比如上传参数错误等，可以通过抛出异常的方式实现
            when (code) {
                200 -> {
                    Log.d("","请求成功")
                }
                401 -> {
                    //清空本地token信息
                    SharedPreferencesHelper.helper.remove(TOKEN)
                    throw TokenException()
                }
                50012 -> {
                    throw JXException()
                }
                else -> throw ParameterException(message)//其他异常情况直接打印异常信息
            }
        }
    }

    //解析源异常
    class JXException : Exception()

    //token异常 重新登录
    class TokenException : Exception()

    //服务器返回的其他错误 打印下错误信息
    class ParameterException(message: String) : Exception(message)
}