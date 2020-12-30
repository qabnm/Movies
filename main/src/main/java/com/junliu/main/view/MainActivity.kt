package com.junliu.main.view

import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.launcher.ARouter
import com.junliu.common.util.RouterPath
import com.junliu.main.R
import dc.android.bridge.view.BaseFragment
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 首页activity
 */
class MainActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_main

    private var cinemaFragment: BaseFragment? = null
    private var hotSpotFragment: BaseFragment? = null
    private var movieFragment: BaseFragment? = null
    private var mineFragment: BaseFragment? = null

    override fun initData() {
        navigation.itemIconTintList = null
        navigation.setOnNavigationItemSelectedListener {
            val ts = supportFragmentManager.beginTransaction()
            hideAllFragment(ts)
            when (it.itemId) {
                R.id.action_main -> showFragment(ts, cinemaFragment, RouterPath.PATH_CINEMA)
                R.id.action_discover -> showFragment(ts, movieFragment, RouterPath.PATH_MOVIE)
                R.id.action_hot -> showFragment(ts, hotSpotFragment, RouterPath.PATH_HOTSPOT)
                R.id.action_mine -> showFragment(ts, mineFragment, RouterPath.PATH_PERSONAL)
            }
            ts.commitAllowingStateLoss()
            true
        }
        navigation.selectedItemId = R.id.action_main
    }

    private fun showFragment(
        transaction: FragmentTransaction,
        fragment: BaseFragment?,
        path: String
    ) {
        fragment.takeIf { null != fragment }?.also { transaction.show(it) } ?: run {
            (ARouter.getInstance().build(path).navigation() as? BaseFragment)?.let {
                getFragment(path, it)
                transaction.add(R.id.layoutContainer, it, path)
            }
        }
    }

    private fun getFragment(path: String, fragment: BaseFragment?) {
        when (path) {
            RouterPath.PATH_HOTSPOT -> hotSpotFragment = fragment
            RouterPath.PATH_CINEMA -> cinemaFragment = fragment
            RouterPath.PATH_PERSONAL -> mineFragment = fragment
            RouterPath.PATH_MOVIE -> movieFragment = fragment
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
}