package com.junliu.main.view

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.launcher.ARouter
import com.junliu.common.util.RouterPath.Companion.PATH_CINEMA
import com.junliu.common.util.RouterPath.Companion.PATH_HOTSPOT
import com.junliu.common.util.RouterPath.Companion.PATH_MOVIE
import com.junliu.common.util.RouterPath.Companion.PATH_PERSONAL
import com.junliu.main.R
import dc.android.bridge.util.LoggerSnack
import dc.android.bridge.view.BaseFragment
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

/**
 * 首页activity
 */
class MainActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_main
    private var exitTime = 0L
    private var currentPosition = 0
    private val position = "position"

    private var cinemaFragment: BaseFragment? = null
    private var hotSpotFragment: BaseFragment? = null
    private var movieFragment: BaseFragment? = null
    private var mineFragment: BaseFragment? = null

    override fun initData(savedInstanceState: Bundle?) {
        navigation.itemIconTintList = null
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(position)
            cinemaFragment = supportFragmentManager.getFragment(
                savedInstanceState,
                PATH_CINEMA
            ) as? BaseFragment?
            hotSpotFragment = supportFragmentManager.getFragment(
                savedInstanceState,
                PATH_HOTSPOT
            ) as? BaseFragment?
            movieFragment =
                supportFragmentManager.getFragment(savedInstanceState, PATH_MOVIE) as? BaseFragment?
            mineFragment = supportFragmentManager.getFragment(
                savedInstanceState,
                PATH_PERSONAL
            ) as? BaseFragment?
        }
        navigation.setOnNavigationItemSelectedListener {
            val ts = supportFragmentManager.beginTransaction()
            hideAllFragment(ts)
            when (it.itemId) {
                R.id.action_main -> showFragment(ts, cinemaFragment, PATH_CINEMA, 0)
                R.id.action_discover -> showFragment(ts, movieFragment, PATH_MOVIE, 1)
                R.id.action_hot -> showFragment(ts, hotSpotFragment, PATH_HOTSPOT, 2)
                R.id.action_mine -> showFragment(ts, mineFragment, PATH_PERSONAL, 3)
            }
            ts.commitAllowingStateLoss()
            true
        }
        navigation.selectedItemId = checkPage(currentPosition)
    }

    private fun checkPage(position: Int) = when (position) {
        0 -> R.id.action_main
        1 -> R.id.action_hot
        2 -> R.id.action_discover
        3 -> R.id.action_mine
        else -> R.id.action_main
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(position, currentPosition)
        cinemaFragment?.let { supportFragmentManager.putFragment(outState, PATH_CINEMA, it) }
        hotSpotFragment?.let { supportFragmentManager.putFragment(outState, PATH_HOTSPOT, it) }
        movieFragment?.let { supportFragmentManager.putFragment(outState, PATH_MOVIE, it) }
        mineFragment?.let { supportFragmentManager.putFragment(outState, PATH_PERSONAL, it) }
        super.onSaveInstanceState(outState)
    }

    private fun showFragment(
        transaction: FragmentTransaction,
        fragment: BaseFragment?,
        path: String,
        position: Int
    ) {
        currentPosition = position
        fragment.takeIf { null != fragment }?.also { transaction.show(it) } ?: run {
            (ARouter.getInstance().build(path).navigation() as? BaseFragment)?.let {
                getFragment(path, it)
                transaction.add(R.id.layoutContainer, it, path)
            }
        }
    }

    private fun getFragment(path: String, fragment: BaseFragment?) {
        when (path) {
            PATH_HOTSPOT -> hotSpotFragment = fragment
            PATH_CINEMA -> cinemaFragment = fragment
            PATH_PERSONAL -> mineFragment = fragment
            PATH_MOVIE -> movieFragment = fragment
        }
    }

    /**
     * 隐藏所有的fragment
     */
    private fun hideAllFragment(transaction: FragmentTransaction) {
        cinemaFragment?.let { transaction.hide(it) }
        hotSpotFragment?.let { transaction.hide(it) }
        movieFragment?.let { transaction.hide(it) }
        mineFragment?.let { transaction.hide(it) }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            LoggerSnack.show(this, "再按一次退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            finish()
            exitProcess(0)
        }
    }
}