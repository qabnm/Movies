package dc.android.bridge.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * @author: jun.liu
 * @date: 2020/9/29 : 14:06
 *
 */
open class BaseFragment : Fragment() {
    private var isOk = false
    private var isFirstLoad = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initVM()
        initView()
        isOk = true
//        initData()
        startObserve()
    }

    override fun onDestroy() {
        super.onDestroy()
        isFirstLoad = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isOk = false
    }

    override fun onResume() {
        super.onResume()
        if (isOk && isFirstLoad) {
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