package com.jun.liu.common

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.jun.liu.common.util.ClassUtils
import com.jun.liu.common.util.Utils
import dc.android.bridge.view.BaseFragment

open class BaseApplication : Application() {
    companion object{
        lateinit var baseCtx:Context
    }
    override fun onCreate() {
        super.onCreate()
        baseCtx = this.applicationContext
        initARouter()
    }

    /**
     * 初始化arouter
     */
    private fun initARouter(){
        if (Utils.isAppDebug()){
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