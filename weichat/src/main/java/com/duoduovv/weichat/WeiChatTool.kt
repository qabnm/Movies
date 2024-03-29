package com.duoduovv.weichat

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Bundle
import android.widget.Toast
import com.duoduovv.tent.TentLoginListener
import com.duoduovv.tent.TentShareListener
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.qqAppId
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.weiChatAppId
import com.tencent.connect.share.QQShare
import com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_DEFAULT
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.Tencent
import java.io.ByteArrayOutputStream


/**
 * @author: jun.liu
 * @date: 2020/12/26 14:32
 * @des:
 */
class WeiChatTool {
    companion object {
        var weiChatApi: IWXAPI? = null
        var mTenCent: Tencent? = null
        var loginListener: TentLoginListener? = null
        var shareListener: TentShareListener? = null

        /**
         * 注册APP到微信
         * 一般在程序入口注册
         */
        fun regToWx(context: Context) {
            weiChatApi = WXAPIFactory.createWXAPI(context, weiChatAppId, true)
            weiChatApi?.registerApp(weiChatAppId)
        }

        /**
         * 微信登录
         * @param context Context
         */
        fun weiChatLogin(context: Context) {
            weiChatApi?.let {
                if (!it.isWXAppInstalled) {
                    Toast.makeText(context, "您的设备未安装微信客户端", Toast.LENGTH_SHORT).show()
                } else {
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

        private fun buildTransaction(type: String?): String {
            return if (type == null) System.currentTimeMillis()
                .toString() else "$type${System.currentTimeMillis()}"
        }

        /**
         * 注册到QQ
         * Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI
         * 其中Authorities为 Manifest文件中注册FileProvider时设置的authorities属性值
         */
        fun regToQQ(context: Context) {
            mTenCent = Tencent.createInstance(qqAppId, context, "com.tencent.duoduovv.fileProvider")
        }

        /**
         * QQ登录
         * @param context Context
         * @param listener TentUiListener
         */
        fun qqLogin(context: Activity, listener: TentLoginListener) {
            if (mTenCent?.isSessionValid == false) {
                mTenCent?.login(context, "all", listener)
            }
        }


        /**
         * @param context Context
         * @param title String 分享的标题 最长30个字符
         * @param summary String 分享的消息摘要 最长40个字符
         * @param url String 分享消息点击之后跳转url
         * @param appName String app名称
         * @param type Int 分享到QQ或者QQ空间
         */
        fun shareToQQ(
            context: Context,
            title: String,
            summary: String,
            url: String,
            appName: String,
            type: Int
        ) {
            shareListener = TentShareListener(context)
            Bundle().apply {
                putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, SHARE_TO_QQ_TYPE_DEFAULT)
                putString(QQShare.SHARE_TO_QQ_TITLE, title)
                putString(QQShare.SHARE_TO_QQ_SUMMARY, summary)
                putString(QQShare.SHARE_TO_QQ_TARGET_URL, url)
                putString(QQShare.SHARE_TO_QQ_APP_NAME, appName)
                if (type == WeiChatBridgeContext.shareToQQ) mTenCent?.shareToQQ(
                    context as Activity?,
                    this,
                    shareListener
                ) else mTenCent?.shareToQzone(
                    context as Activity?, this, shareListener
                )
            }
        }

//        fun toxiao(context: Context){
//            val api = WXAPIFactory.createWXAPI(context, weiChatAppId)
//            val req = WXLaunchMiniProgram.Req()
//            req.apply {
//                userName = "gh_b5747801fc59"
//                path = "pages/pay/index?order_sn=luge_202106162135571009753&source=app"
//                miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW
//                api.sendReq(this)
//            }
//        }
    }
}