package dc.android.bridge.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

/**
 * @author: jun.liu
 * @date: 2020/9/24 : 16:56
 */
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initOnCreate(savedInstanceState)
    }

    private fun initOnCreate(savedInstanceState: Bundle?) {
        initAttach()
        initViewModel()
        initLayout()
        startObserve()
        initData()
        initData(savedInstanceState)
    }

    open fun initViewModel() {}

    open fun isTranslucentStatus(): Boolean = false

    /**
     * 初始化布局
     */
    open fun initLayout() {}

    open fun initAttach() = setTranslucentStatus()

    open fun initData() {}

    /**
     * 需要用到bundle参数时候可以使用这个方法
     * @param savedInstanceState Bundle?
     */
    open fun initData(savedInstanceState: Bundle?){}

    open fun startObserve() {}

    /**
     * 设置透明状态栏
     */
    private fun setTranslucentStatus() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = Color.TRANSPARENT
            }
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        }
    }
}