package com.junliu.main.view

import com.alibaba.android.arouter.launcher.ARouter
import com.junliu.common.util.RouterPath
import com.junliu.main.R
import dc.android.bridge.view.BridgeActivity

/**
 * @author: jun.liu
 * @date: 2021/1/22 11:48
 * @des:启动页
 */
class SplashActivity :BridgeActivity(){
    override fun getLayoutId() = R.layout.activity_splash
    override fun showStatusBarView() = false

    override fun initView() {
        ARouter.getInstance().build(RouterPath.PATH_MAIN).navigation()
        this.finish()
    }
}