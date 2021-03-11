package dc.android.bridge.view

import androidx.lifecycle.ViewModelProvider
import dc.android.bridge.BridgeContext.Companion.CONNECTION_ERROR
import dc.android.bridge.BridgeContext.Companion.NETWORK_ERROR
import dc.android.bridge.BridgeContext.Companion.RUNTIME_ERROR
import dc.android.bridge.BridgeContext.Companion.TOKEN_ERROR
import dc.android.bridge.net.BaseRepository
import dc.android.bridge.net.BaseViewModel
import dc.android.bridge.util.LoggerSnack
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author: jun.liu
 * @date: 2020/9/24 : 17:32
 *
 * 如果不需要用到liveData可以直接继承BridgeActivity
 */
open class BaseViewModelActivity<VM : BaseViewModel> : BridgeActivity() {
    protected lateinit var viewModel: VM
    open fun providerVMClass(): Class<VM>? = null

    override fun initViewModel() {
        providerVMClass()?.let {
            viewModel = ViewModelProvider(this).get(it)
            lifecycle.addObserver(viewModel)
        }
    }

    override fun startObserve() {
        //处理一些通用的异常
        viewModel.run {
            getException().observe(this@BaseViewModelActivity, { requestError(it) })
        }
    }

    /**
     * 异常处理
     */
    open fun requestError(throwable: Throwable?) {
        throwable?.let {
            when (it) {
                is UnknownHostException -> showError(NETWORK_ERROR)
                is SocketTimeoutException -> showError(NETWORK_ERROR)
                is ConnectException -> showError(CONNECTION_ERROR)
                is IllegalStateException -> showError("数据解析异常")
                is RuntimeException -> showError(RUNTIME_ERROR)
                is BaseRepository.TokenException -> showError(TOKEN_ERROR)
                is BaseRepository.ParameterException -> parameterError(it.message.toString())
                else -> showError(it.message)
            }
        }
    }

    open fun showError(errMsg: String?) {
        LoggerSnack.show(this , errMsg)
    }

    private fun parameterError(msg: String) {
        LoggerSnack.show(this , msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }
}