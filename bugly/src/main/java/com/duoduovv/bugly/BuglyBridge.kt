package com.duoduovv.bugly

import android.content.Context
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport

/**
 * @author: jun.liu
 * @date: 2021/6/7 13:32
 * @des:
 */
class BuglyBridge {
    companion object{
        fun initBugly(context: Context,isDebug:Boolean){
            if (!isDebug){
                //正式环境开启异常上报
                CrashReport.initCrashReport(context,"857247490b",false)
            }
        }
    }
}