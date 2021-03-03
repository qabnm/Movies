package dc.android.bridge.view

import androidx.lifecycle.ViewModelProvider
import dc.android.bridge.BridgeContext.Companion.CONNECTION_ERROR
import dc.android.bridge.BridgeContext.Companion.NETWORK_ERROR
import dc.android.bridge.BridgeContext.Companion.RUNTIME_ERROR
import dc.android.bridge.BridgeContext.Companion.TOKEN_ERROR
import dc.android.bridge.net.BaseRepository
import dc.android.bridge.net.BaseViewModel
import dc.android.bridge.util.LoggerSnack
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author: jun.liu
 * @date: 2020/9/29 : 14:18
 *
 * 如果不需要用到liveData可以直接继承BaseFragment
 */
open class BaseViewModelFragment<VM : BaseViewModel> : BaseFragment() {
    protected lateinit var viewModel: VM
    open fun providerVMClass(): Class<VM>? = null

    override fun initVM() {
        providerVMClass()?.let {
            viewModel = ViewModelProvider(this).get(it)
            lifecycle.addObserver(viewModel)
        }
    }

    override fun startObserve() {
        viewModel.run {
            getException().observe(this@BaseViewModelFragment, { requestError(it) })
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
                is HttpException -> showError("${it.code()}${it.message()}")
                is RuntimeException -> showError(RUNTIME_ERROR)
                is BaseRepository.TokenException -> showError(TOKEN_ERROR)
                is BaseRepository.ParameterException -> parameterError(it.message.toString())
                else -> showError(it.message)
            }
        }
    }

    open fun showError(errMsg: String?) {
        LoggerSnack.show(requireActivity(), errMsg)
    }

    private fun parameterError(msg: String) {
        LoggerSnack.show(requireActivity(), msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }
}