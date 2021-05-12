package dc.android.bridge.view

import androidx.lifecycle.ViewModelProvider
import com.duoduovv.common.component.LoadingDialogFragment
import dc.android.bridge.BridgeContext.Companion.CONNECTION_ERROR
import dc.android.bridge.BridgeContext.Companion.NETWORK_ERROR
import dc.android.bridge.BridgeContext.Companion.RUNTIME_ERROR
import dc.android.bridge.BridgeContext.Companion.TOKEN_ERROR
import dc.android.bridge.net.BaseRepository
import dc.android.bridge.net.BaseViewModel
import dc.android.bridge.util.AndroidUtils
import retrofit2.HttpException
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
    private var loadingDialog: LoadingDialogFragment? = null

    override fun initViewModel() {
        providerVMClass()?.let {
            viewModel = ViewModelProvider(this).get(it)
            lifecycle.addObserver(viewModel)
        }
        loadingDialog = LoadingDialogFragment()
    }

    override fun startObserve() {
        //处理一些通用的异常
        viewModel.run {
            getException().observe(this@BaseViewModelActivity, { requestError(it) })
            getLoadingState().observe(this@BaseViewModelActivity, {
                if (it) showLoading() else dismissLoading()
            })
        }
    }

    /**
     * 异常处理
     */
    open fun requestError(throwable: Throwable?) {
        throwable?.let {
            dismissLoading()
            when (it) {
                is UnknownHostException -> showError(NETWORK_ERROR)
                is SocketTimeoutException -> showError(NETWORK_ERROR)
                is ConnectException -> showError(CONNECTION_ERROR)
                is HttpException -> showError("服务繁忙，请稍后再试")
                is IllegalStateException -> showError("数据解析异常")
                is RuntimeException -> showError(RUNTIME_ERROR)
                is BaseRepository.TokenException -> showError(TOKEN_ERROR)
                is BaseRepository.ParameterException -> parameterError(it.message.toString())
                else -> showError(it.message)
            }
        }
    }

    private fun showLoading() {
        loadingDialog?.showNow(supportFragmentManager, "loading")
    }

    private fun dismissLoading() {
        if (loadingDialog?.isAdded == true) loadingDialog?.dismiss()
    }

    open fun showError(errMsg: String?) {
        AndroidUtils.toast(errMsg, this)
        finishLoading()
    }

    private fun parameterError(msg: String) {
        AndroidUtils.toast(msg, this)
        finishLoading()
    }

    open fun finishLoading() {}

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }
}