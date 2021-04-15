package dc.android.bridge.net

import androidx.annotation.Keep

/**
 * @author: jun.liu
 * @date: 2020/9/25 : 16:54
 *
 * 根据定义的数据结构修改
 */
@Keep
data class BaseResponseData<out T>(
    val code: Int,
    val message: String,
    val data: T
)