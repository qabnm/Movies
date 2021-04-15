package com.duoduovv.weichat

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
        //微信秘钥
        const val weiChatSecret = ""
        //分享到微信好友
        const val weiChatFriend = SendMessageToWX.Req.WXSceneSession
        //分享到微信朋友圈
        const val weiChatCircle = SendMessageToWX.Req.WXSceneTimeline
        //分享到微信收藏
        const val weiChatFavorite = SendMessageToWX.Req.WXSceneFavorite
        //QQ的appId
        const val qqAppId = "101936595"
        //分享到QQ
        const val shareToQQ = 1
        //分享到QQ空间
        const val shareToQQZone = 2

        /**
         * 获取微信accessToken
         */
        const val accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token"

        /**
         * 刷新Token
         */
        const val refreshTokenUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token"

        /**
         * 检查accessToken是否有效
         */
        const val accessTokenValidUrl = "https://api.weixin.qq.com/sns/auth"

        /**
         * 获取用户信息
         */
        const val weiChatUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo"
    }
}