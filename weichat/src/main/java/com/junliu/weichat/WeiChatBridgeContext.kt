package com.junliu.weichat

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX

/**
 * @author: jun.liu
 * @date: 2020/12/26 14:31
 * @des:
 */
class WeiChatBridgeContext {
    companion object{
        //微信appId
        const val weiChatAppId = "000000"
        //分享到微信好友
        const val weiChatFriend = SendMessageToWX.Req.WXSceneSession
        //分享到微信朋友圈
        const val weiChatCircle = SendMessageToWX.Req.WXSceneTimeline
        //分享到微信收藏
        const val weiChatFavorite = SendMessageToWX.Req.WXSceneFavorite
        //QQ的appId
        const val qqAppId = "00000"
        //分享到QQ
        const val shareToQQ = 1
        //分享到QQ空间
        const val shareToQQZone = 2
    }
}