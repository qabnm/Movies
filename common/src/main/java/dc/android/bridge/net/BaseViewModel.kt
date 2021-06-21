package dc.android.bridge.net

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * @author: jun.liu
 * @date: 2020/9/24 : 16:18
 */
open class BaseViewModel : ViewModel(), LifecycleObserver {
    //异常处理
    private val error by lazy { MutableLiveData<Throwable>() }

    //加载loading状态
    private val loading by lazy { MutableLiveData<Boolean>() }

    fun request(loadingState: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch {
            loading.value = loadingState
            try {
                block()
                Log.d("tag", "请求成功")
            } catch (e: Exception) {
                error.value = e
                Log.d("tag", "请求异常$e")
            } finally {
                loading.value = false
                Log.d("tag", "请求完成")
            }
        }

    fun getException() = error

    fun getLoadingState() = loading

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun isSuccess(code: Int?) = code == SUCCESS
}