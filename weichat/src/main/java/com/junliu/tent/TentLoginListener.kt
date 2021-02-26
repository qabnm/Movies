package com.junliu.tent

import android.content.Context
import android.widget.Toast
import com.junliu.weichat.WeiChatTool
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.UiError
import dc.android.tools.LiveDataBus
import org.json.JSONObject

/**
 * @author: jun.liu
 * @date: 2021/2/18 11:43
 * @des:
 */
class TentLoginListener(private val context: Context):IUiListener {
    override fun onComplete(response: Any?) {
        val jsonObject = response as? JSONObject
        if (null != jsonObject){
            val expiresIn = jsonObject.getString(Constants.PARAM_EXPIRES_IN)
            val openId = jsonObject.getString(Constants.PARAM_OPEN_ID)
            val accessToken = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN)
            //获取用户信息
            WeiChatTool.mTenCent?.let {
                it.openId = openId
                it.setAccessToken(accessToken, expiresIn)
                it.requestAsync("get_simple_userinfo",null, Constants.HTTP_GET,object :TentUserInfoListener(){
                    override fun onComplete(jsonObject: JSONObject?) {
                        jsonObject?.let { it ->
                            val ret = it.getInt("ret")
                            val nickName = it.getString("nickname")
                            val sex = it.getString("gender")
                            val msg = it.getString("msg")
                            val headerUrl = it.getString("figureurl_qq_1")
                            val userInfo = TentUserInfo(ret, msg, nickName, sex, headerUrl,openId)
                            LiveDataBus.get().with("tentUserInfo").value = userInfo
                        }
                    }
                })
            }
        }else{
            Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCancel() {
        Toast.makeText(context, "授权取消", Toast.LENGTH_SHORT).show()
    }

    override fun onWarning(var0: Int) {
    }

    override fun onError(error: UiError) {
        Toast.makeText(context, "授权失败${error.errorCode}:${error.errorMessage}", Toast.LENGTH_SHORT).show()
    }
}