package com.duoduovv.tent

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.duoduovv.weichat.WeiChatTool
import com.tencent.connect.UserInfo
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
        Log.i("tent",jsonObject?.toString()?:"jsonObject是空的")
        if (null != jsonObject){
            val expiresIn = jsonObject.getString(Constants.PARAM_EXPIRES_IN)
            val openId = jsonObject.getString(Constants.PARAM_OPEN_ID)
            val accessToken = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN)
            //获取用户信息
            WeiChatTool.mTenCent?.let { it ->
                it.openId = openId
                it.setAccessToken(accessToken, expiresIn)
                val userInfo = UserInfo(context, it.qqToken)
                val listener = object :IUiListener{
                    override fun onComplete(p0: Any?) {
                        val js = p0 as? JSONObject
                        js?.let {
                            Log.i("tent",it.toString())
                            val ret = it.getInt("ret")
                            val nickName = it.getString("nickname")
                            val sex = it.getInt("gender_type")
                            val msg = it.getString("msg")
                            var headerUrl = it.getString("figureurl_qq_1")
                            if (it.has("figureurl_qq_2")){
                                val url = it.getString("figureurl_qq_2")
                                if (!TextUtils.isEmpty(url)) headerUrl = url
                            }
                            val info = TentUserInfo(ret, msg, nickName, sex, headerUrl,openId)
                            LiveDataBus.get().with("tentUserInfo").value = info
                        }
                    }

                    override fun onError(p0: UiError?) {
                        Log.i("tent","onError${p0?.toString()}")
                    }

                    override fun onCancel() {
                        Log.i("tent","onCancel")
                    }

                    override fun onWarning(p0: Int) {
                        Log.i("tent","onWarning${p0}")
                    }
                }
                userInfo.getUserInfo(listener)
//                it.requestAsync("get_simple_userinfo",null, Constants.HTTP_GET,object :TentUserInfoListener(){
//                    override fun onComplete(jsonObject: JSONObject?) {
//                        Log.i("tent","onComplete")
//                        jsonObject?.let { it ->
//                            Log.i("tent",it.toString())
//                            val ret = it.getInt("ret")
//                            val nickName = it.getString("nickname")
//                            val sex = if (it.getString("gender") == "男") 1 else 2
//                            val msg = it.getString("msg")
//                            val headerUrl = it.getString("figureurl_qq_1")
//                            val userInfo = TentUserInfo(ret, msg, nickName, sex, headerUrl,openId)
//                            LiveDataBus.get().with("tentUserInfo").value = userInfo
//                        }
//                    }
//                })
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