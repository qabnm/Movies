package dc.android.bridge.net

/**
 * @author: jun.liu
 * @date: 2020/9/25 : 16:54
 *
 * 根据定义的数据结构修改
 */
data class BaseResponseData<out T>(
    val errorCode: Int,
    val errorMsg: String,
    val data: T
)