package com.duoduovv.mhhh.wxapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.duoduovv.weichat.WeiChatTool
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import dc.android.tools.LiveDataBus

/**
 * @author: jun.liu
 * @date: 2020/12/26 15:10
 * @des:接受微信响应结果类
 */
class WXEntryActivity : AppCompatActivity(), IWXAPIEventHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WeiChatTool.regToWx(applicationContext)
        WeiChatTool.weiChatApi?.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        WeiChatTool.weiChatApi?.handleIntent(intent, this)
    }

    /**
     * 接受微信返回的消息
     * @param resp BaseResp
     */
    override fun onResp(resp: BaseResp) {
        Log.d("weiChat","${resp.errCode}")
//        Log.d("weiChat","${resp.type}")
//        if (resp.type == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM){
//            val extraData = (resp as WXLaunchMiniProgram.Resp).extMsg
//            Log.d("weiChat","$extraData")
//            LiveDataBus.get().with("extraData").value = extraData
//        }
        var result = ""
        when (resp.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                if (resp.type == ConstantsAPI.COMMAND_SENDAUTH) {
                    result = "授权成功"
                    val code = (resp as SendAuth.Resp).code
                    //通过此code换取access_token参数
                    LiveDataBus.get().with("wxCode").value = code
                } else if (resp.type == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
                    result = "分享成功"
                }
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                if (resp.type == ConstantsAPI.COMMAND_SENDAUTH) {
                    result = "授权取消"
                } else if (resp.type == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
                    result = "分享取消"
                }
            }
            BaseResp.ErrCode.ERR_AUTH_DENIED -> result = "发送被拒绝"
            BaseResp.ErrCode.ERR_UNSUPPORT -> result = "不支持的错误"
            else -> result = "发送返回"
        }
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onReq(p0: BaseReq?) {}
}