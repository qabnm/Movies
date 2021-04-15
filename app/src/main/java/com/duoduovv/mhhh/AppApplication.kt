package com.duoduovv.mhhh

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI
import com.duoduovv.common.BaseApplication

/**
 * @author: jun.liu
 * @date: 2020/12/24 15:58
 * @des:
 */
class AppApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        FeedbackAPI.init(this)
    }

}