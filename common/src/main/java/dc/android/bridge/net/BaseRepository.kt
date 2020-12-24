package dc.android.bridge.net

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author: jun.liu
 * @date: 2020/9/25 : 16:37
 */
open class BaseRepository {
    suspend fun <T : Any> request(call: suspend () -> BaseResponseData<T>): BaseResponseData<T> {
        return withContext(Dispatchers.IO) { call.invoke() }.apply {
            //这儿可以对返回结果errCode做一些特殊处理，比如上传参数错误等，可以通过抛出异常的方式实现
            when (errorCode) {
                0 -> Log.e("请求状态值:$errorCode", "请求成功")
                -1001 -> throw TokenException()
                else -> throw ParameterException(errorMsg)//其他异常情况直接打印异常信息
            }
        }
    }

    //token异常 重新登录
    class TokenException : Exception()

    //服务器返回的其他错误 打印下错误信息
    class ParameterException(message: String) : Exception(message)
}