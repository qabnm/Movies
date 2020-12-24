package com.jun.liu.common

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.jun.liu.common.util.ClassUtils
import com.jun.liu.common.util.Utils
import com.umeng.commonsdk.UMConfigure
import dc.android.bridge.view.BaseFragment

open class BaseApplication : Application() {
    companion object {
        lateinit var baseCtx: Context
    }

    override fun onCreate() {
        super.onCreate()
        baseCtx = this.applicationContext
        initARouter()
        initUmeng()
    }

    /**
     * 初始化友盟统计
     * UMConfigure.init(Context context, String appkey, String channel, int deviceType, String pushSecret);
     */
    private fun initUmeng() {
        UMConfigure.init(applicationContext, "","",0, "")
        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(false)
    }

    /**
     * 初始化arouter
     */
    private fun initARouter() {
        if (Utils.isAppDebug()) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        //初始化ARouter
        ARouter.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        ARouter.getInstance().destroy()
    }
}