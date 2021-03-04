package com.duoduovv.common

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.util.CrashHandler
import com.tencent.bugly.crashreport.CrashReport
import com.umeng.commonsdk.UMConfigure
import dc.android.bridge.util.OsUtils

open class BaseApplication : Application() {
    companion object {
        lateinit var baseCtx: Context
        var hotList:List<String>?=null
    }

    override fun onCreate() {
        super.onCreate()
        baseCtx = this.applicationContext
        initOthers()
        if (!OsUtils.isAppDebug()){
            //正式环境开启异常上报
            CrashReport.initCrashReport(applicationContext,"857247490b",false)
        }
    }

    /**
     * 初始化一些三方库
     */
    private fun initOthers() {
        if (OsUtils.isAppDebug()) {
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