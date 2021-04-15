package com.duoduovv.common

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.util.CrashHandler
import com.duoduovv.weichat.WeiChatTool
import com.tencent.bugly.crashreport.CrashReport
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import dc.android.bridge.util.AndroidUtils
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
        }
        //初始化ARouter
        ARouter.init(this)
        //初始化友盟统计
        UMConfigure.init(applicationContext, "605c45146ee47d382b961c13", AndroidUtils.getAppMetaData(), UMConfigure.DEVICE_TYPE_PHONE, null)
        //统计SDK基础统计指标自动采集
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
        if (OsUtils.isAppDebug()) UMConfigure.setLogEnabled(true)  //参数: boolean 默认为false，如需查看LOG设置为true
    }

    override fun onTerminate() {
        super.onTerminate()
        ARouter.getInstance().destroy()
    }
}