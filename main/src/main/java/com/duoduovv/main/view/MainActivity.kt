package com.duoduovv.main.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.component.AlertDialogFragment
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.RouterPath.Companion.PATH_CINEMA
import com.duoduovv.common.util.RouterPath.Companion.PATH_MOVIE
import com.duoduovv.common.util.RouterPath.Companion.PATH_PERSONAL
import com.duoduovv.main.R
import com.duoduovv.weichat.WeiChatTool
import com.tencent.connect.common.UIListenerManager
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.TYPE_ID
import dc.android.bridge.view.BaseFragment
import dc.android.bridge.view.BridgeActivity
import dc.android.tools.LiveDataBus
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

/**
 * 首页activity
 */
@Route(path = RouterPath.PATH_MAIN)
class MainActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_main
    private var exitTime = 0L
    private var currentPosition = 0
    private val position = "position"
    private var typeId: String? = null

    private var cinemaFragment: BaseFragment? = null

    //    private var hotSpotFragment: BaseFragment? = null
    private var movieFragment: BaseFragment? = null
    private var mineFragment: BaseFragment? = null

    override fun showStatusBarView() = false

    override fun initView() {
//        navigation.setNavBarClickListener(this)
        LiveDataBus.get().with(TYPE_ID, String::class.java).observe(this, {
            typeId = it
            navigation.selectedItemId = checkPage(1)
            LiveDataBus.get().with(ID).value = it
        })
    }

    override fun initData(savedInstanceState: Bundle?) {
        navigation.itemIconTintList = null
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(position)
            cinemaFragment = supportFragmentManager.getFragment(
                savedInstanceState, PATH_CINEMA
            ) as? BaseFragment?
//            hotSpotFragment = supportFragmentManager.getFragment(
//                savedInstanceState, PATH_HOTSPOT
//            ) as? BaseFragment?
            movieFragment =
                supportFragmentManager.getFragment(savedInstanceState, PATH_MOVIE) as? BaseFragment?
            mineFragment = supportFragmentManager.getFragment(
                savedInstanceState, PATH_PERSONAL
            ) as? BaseFragment?
        }
        navigation.setOnNavigationItemSelectedListener {
            val ts = supportFragmentManager.beginTransaction()
            hideAllFragment(ts)
            when (it.itemId) {
                R.id.action_main -> showFragment(ts, cinemaFragment, PATH_CINEMA, 0)
                R.id.action_discover -> showFragment(ts, movieFragment, PATH_MOVIE, 1)
//                R.id.action_hot -> showFragment(ts, hotSpotFragment, PATH_HOTSPOT, 2)
                R.id.action_mine -> showFragment(ts, mineFragment, PATH_PERSONAL, 3)
            }
            ts.commitAllowingStateLoss()
            true
        }
        navigation.selectedItemId = checkPage(currentPosition)
//        onNavClick(0)
    }

    /**
     * 切换首页tab显示
     * @param position Int
     * @return Int
     */
    private fun checkPage(position: Int) = when (position) {
        0 -> R.id.action_main
//        1 -> R.id.action_hot
        1 -> R.id.action_discover
        2 -> R.id.action_mine
        else -> R.id.action_main
    }

    /**
     * 意外退出时，保存当前的fragment
     * @param outState Bundle
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(position, currentPosition)
        cinemaFragment?.let { supportFragmentManager.putFragment(outState, PATH_CINEMA, it) }
//        hotSpotFragment?.let { supportFragmentManager.putFragment(outState, PATH_HOTSPOT, it) }
        movieFragment?.let { supportFragmentManager.putFragment(outState, PATH_MOVIE, it) }
        mineFragment?.let { supportFragmentManager.putFragment(outState, PATH_PERSONAL, it) }
        super.onSaveInstanceState(outState)
    }

    /**
     * 显示点击的fragment
     */
    private fun showFragment(
        transaction: FragmentTransaction,
        fragment: BaseFragment?,
        path: String,
        position: Int
    ) {
        currentPosition = position
        fragment.takeIf { null != fragment }?.also { transaction.show(it) } ?: run {
            (ARouter.getInstance().build(path).withString(BridgeContext.TYPE_ID, typeId)
                .navigation() as? BaseFragment)?.let {
                getFragment(path, it)
                transaction.add(R.id.layoutContainer, it, path)
            }
        }
    }

    private fun getFragment(path: String, fragment: BaseFragment?) {
        when (path) {
//            PATH_HOTSPOT -> hotSpotFragment = fragment
            PATH_CINEMA -> cinemaFragment = fragment
            PATH_PERSONAL -> mineFragment = fragment
            PATH_MOVIE -> movieFragment = fragment
        }
    }

//    override fun onNavClick(position: Int) {
//        val ts = supportFragmentManager.beginTransaction()
//        hideAllFragment(ts)
//        var fragment: BaseFragment? = null
//        var path = ""
//        when (position) {
//            0 -> {
//                fragment = cinemaFragment
//                path = PATH_CINEMA
//            }
//            2 -> {
//                fragment = movieFragment
//                path = PATH_MOVIE
//            }
//            1 -> {
//                fragment = hotSpotFragment
//                path = PATH_HOTSPOT
//            }
//            3 -> {
//                fragment = mineFragment
//                path = PATH_PERSONAL
//            }
//        }
//        showFragment(ts, fragment, path, position)
//        ts.commit()
//    }

    /**
     * 隐藏所有的fragment
     */
    private fun hideAllFragment(transaction: FragmentTransaction) {
        cinemaFragment?.let { transaction.hide(it) }
//        hotSpotFragment?.let { transaction.hide(it) }
        movieFragment?.let { transaction.hide(it) }
        mineFragment?.let { transaction.hide(it) }
    }

    override fun onBackPressed() {
        showAlertDialog()
    }

    private var dialogFragment: AlertDialogFragment? = null
    private fun showAlertDialog() {
        dialogFragment = AlertDialogFragment("确定要退出吗？", 250f, listener)
        dialogFragment?.let {
            it.showNow(supportFragmentManager, "alert")
            it.setTitleVisibility(View.GONE)
            it.setCancelText("退出")
            it.setSureText("再看看")
            it.setCanceledOnTouchOut(false)
            it.setCancel(false)
        }
    }

    private val listener = object : AlertDialogFragment.OnDialogSureClickListener {
        override fun onSureClick() {
            dialogFragment?.dismiss()
        }

        override fun onCancelClick() {
            dialogFragment?.dismiss()
            finish()
            exitProcess(0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        UIListenerManager.getInstance().onActivityResult(
            requestCode,
            resultCode,
            data,
            if (null != WeiChatTool.loginListener) WeiChatTool.loginListener else WeiChatTool.shareListener
        )
        WeiChatTool.loginListener = null
        WeiChatTool.shareListener = null
        super.onActivityResult(requestCode, resultCode, data)
    }
}