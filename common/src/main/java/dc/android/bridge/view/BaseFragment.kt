package dc.android.bridge.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * @author: jun.liu
 * @date: 2020/9/29 : 14:06
 *
 */
abstract class BaseFragment : Fragment() {
    private var isFirstLoad = true
    protected lateinit var baseBinding: ViewBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        baseBinding = initBind(inflater, container)
        return baseBinding.root
    }

    abstract fun initBind(inflater: LayoutInflater, container: ViewGroup?): ViewBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initVM()
        initView()
        startObserve()
    }

    override fun onDestroy() {
        super.onDestroy()
        isFirstLoad = true
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            isFirstLoad = false
            initData()
        }
    }

    open fun initVM() {}

    open fun getLayoutId() = 0

    open fun initView() {}

    open fun initData() {}

    open fun startObserve() {}
}