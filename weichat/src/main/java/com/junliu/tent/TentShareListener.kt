package com.junliu.tent

import android.content.Context
import android.widget.Toast
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.json.JSONObject

/**
 * @author: jun.liu
 * @date: 2020/12/28 14:28
 * @des: 接受QQ返回的回调信息
 */
class TentShareListener(private val context: Context):IUiListener{
    override fun onComplete(response: Any?) {
        Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show()
    }

    override fun onCancel() {
        Toast.makeText(context, "分享取消", Toast.LENGTH_SHORT).show()
    }

    override fun onWarning(var0: Int) {
    }

    override fun onError(error: UiError) {
        Toast.makeText(context, "分享失败${error.errorCode}:${error.errorMessage}", Toast.LENGTH_SHORT).show()
    }
}