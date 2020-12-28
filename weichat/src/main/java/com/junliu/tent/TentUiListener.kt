package com.junliu.tent

import com.tencent.tauth.IUiListener
import com.tencent.tauth.UiError

/**
 * @author: jun.liu
 * @date: 2020/12/28 14:28
 * @des: 接受QQ返回的回调信息
 */
class TentUiListener:IUiListener{
    override fun onComplete(response: Any?) {
    }

    override fun onCancel() {
    }

    override fun onWarning(p0: Int) {
    }

    override fun onError(error: UiError?) {
    }
}