package dc.android.bridge.net

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.util.logging.Logger

/**
 * @author: jun.liu
 * @date: 2020/9/24 : 16:18
 */
open class BaseViewModel : ViewModel(), LifecycleObserver {
    private val error by lazy { MutableLiveData<Throwable>() }

    fun request(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch {
        kotlin.runCatching {
            block()
            Log.e("tag", "请求成功")
        }.onFailure {
            error.value = it
            Log.e("tag", "请求异常$it")
        }
    }

    fun getException(): LiveData<Throwable> {
        return error
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}