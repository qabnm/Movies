package dc.android.bridge.net

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*

/**
 * @author: jun.liu
 * @date: 2020/9/24 : 16:18
 */
open class BaseViewModel : ViewModel(), LifecycleObserver {
    //异常处理
    private val error by lazy { MutableLiveData<Throwable>() }
    //加载loading状态
    private val loading by lazy { MutableLiveData<Boolean>() }

    fun request(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch {
        loading.value = true
        try {
            block()
            Log.d("tag", "请求成功")
        }catch (e:Exception){
            error.value = e
            Log.d("tag", "请求异常$e")
        }finally {
            loading.value = false
        }

//        kotlin.runCatching {
//            block()
//            Log.e("tag", "请求成功")
//        }.onFailure {
//            error.value = it
//            Log.e("tag", "请求异常$it")
//        }
    }

    fun getException() = error

    fun getLoadingState() = loading

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}