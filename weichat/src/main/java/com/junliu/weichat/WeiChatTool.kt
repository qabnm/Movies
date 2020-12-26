package com.junliu.weichat

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.widget.Toast
import com.junliu.weichat.WeiChatBridgeContext.Companion.weiChatAppId
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.io.ByteArrayOutputStream


/**
 * @author: jun.liu
 * @date: 2020/12/26 14:32
 * @des:
 */
class WeiChatTool {
    companion object {
        var weiChatApi: IWXAPI? = null

        /**
         * 注册APP到微信
         * 一般在程序入口哦注册
         */
        fun regToWx(context: Context) {
            weiChatApi = WXAPIFactory.createWXAPI(context, weiChatAppId, true)
            weiChatApi?.registerApp(weiChatAppId)
        }

        /**
         * 微信登录
         * @param context Context
         */
        fun weiChatLogin(context: Context){
            weiChatApi?.let {
                if (!it.isWXAppInstalled){
                    Toast.makeText(context,"您的设备未安装微信客户端",Toast.LENGTH_SHORT).show()
                }else{
                    SendAuth.Req().apply {
                        scope = "snsapi_userinfo"
                        state = "weiChatSuccess"
                        it.sendReq(this)
                    }
                }
            }
        }

        /**
         *
         * @param webPageUrl 网页url
         * @param title 网页标题
         * @param description 网页描述
         * @param target 分享到哪里 微信好友 朋友圈 微信收藏
         */
        fun weiChatShareAsWeb(
            webPageUrl: String,
            title: String,
            description: String,
            bitmap: Bitmap,
            target: Int
        ) {
            val webPage = WXWebpageObject()
            webPage.webpageUrl = webPageUrl
            val msg = WXMediaMessage(webPage)
            msg.title = title
            msg.description = description
            msg.thumbData = bmpToByteArray(bitmap)
            //构造一个req
            SendMessageToWX.Req().apply {
                this.transaction = buildTransaction("webPage")
                message = msg
                scene = target
                weiChatApi?.sendReq(this)
            }
        }

        private fun bmpToByteArray(bmp: Bitmap): ByteArray? {
            val output = ByteArrayOutputStream()
            bmp.compress(CompressFormat.PNG, 100, output)
            bmp.recycle()
            val result: ByteArray = output.toByteArray()
            try {
                output.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        private fun buildTransaction(type: String?): String? {
            return if (type == null) System.currentTimeMillis()
                .toString() else "$type${System.currentTimeMillis()}"
        }

    }


}