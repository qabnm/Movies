package com.junliu.common

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.junliu.common.util.Utils
import com.junliu.weichat.WeiChatTool
import com.umeng.commonsdk.UMConfigure

open class BaseApplication : Application() {
    companion object {
        lateinit var baseCtx: Context
    }

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        baseCtx = this.applicationContext
        initOthers()
    }

    /**
     * 初始化一些三方库
     */
    private fun initOthers() {
        if (Utils.isAppDebug()) {
            ARouter.openLog()
            ARouter.openDebug()
            //参数: boolean 默认为false，如需查看LOG设置为true
            UMConfigure.setLogEnabled(true)
        }
        //初始化ARouter
        ARouter.init(this)
        //初始化友盟统计
        UMConfigure.init(applicationContext, "", "", 0, "")
        //注册app到微信 实现分享 三方登录等
//        WeiChatTool.regToWx(applicationContext)
    }

    override fun onTerminate() {
        super.onTerminate()
        ARouter.getInstance().destroy()
    }
}