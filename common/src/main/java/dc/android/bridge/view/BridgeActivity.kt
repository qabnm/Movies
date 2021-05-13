package dc.android.bridge.view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import dc.android.bridge.util.StatusBarWrapper

/**
 * @author: jun.liu
 * @date: 2020/9/24 : 17:10
 */
open class BridgeActivity : BaseActivity() {
    private lateinit var barWrapper: StatusBarWrapper
    protected lateinit var layoutView:View
    override fun initAttach() {
        super.initAttach()
        barWrapper = StatusBarWrapper(this)
        setShowStatusBarView()
    }

    /**
     * 设置状态栏的颜色和状态栏文字颜色
     * @param isStatusColorDark  false 状态栏文字显示为白色；true状态栏文字显示为黑色
     * 给一个默认参数 默认状态栏文字是黑色的  状态栏一个默认颜色 白色
     * 如果状态栏是白色 文字黑色 就可以使用默认参数 不必传递参数
     * @param statusBarColor  状态栏的颜色
     *
     */
    open fun setLayout(
        isStatusColorDark: Boolean = true,
        @ColorInt statusBarColor: Int = Color.WHITE
    ) {
        layoutView = LayoutInflater.from(this).inflate(getLayoutId(), null)
        barWrapper.onCreate(layoutView,this)
        barWrapper.setStatusBarColor(isStatusColorDark, statusBarColor)
    }

    /**
     * 默认实现的白色状态栏 黑色字体
     * 如果需要改变状态栏默认的颜色和字体颜色 重写下该方法就好 传入需要的颜色值
     */
    override fun initLayout() {
        super.initLayout()
        setLayout()
    }

    open fun getLayoutId(): Int = 0

    /**
     * 如果不想显示状态栏位置的view,该参数传入false,如果需要显示不调用此方法，默认是显示的
     * 用于顶部banner显示,或者其他不需要显示状态栏的地方
     *
     * 此方法应在调用setLayout方法之前调用，注意调用顺序
     */
    private fun setShowStatusBarView() {
        barWrapper.showStatusBarView(showStatusBarView())
    }

    open fun showStatusBarView():Boolean = true

    fun setStatusBarVisible(visible:Int){
        barWrapper.setStatusBarVisible(visible)
    }

}