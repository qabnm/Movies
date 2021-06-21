package com.duoduovv.personal.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author: jun.liu
 * @date: 2021/2/24 18:02
 * @des:用户中心
 */
class PersonRepository : PersonApiRepository() {

    /**
     * 获取用户信息
     * @return BaseResponseData<UserInfoBean>
     */
    suspend fun userInfo() = request {
        apiService.userInfo()
    }

    /**
     * 获取微信的accessToken
     * @param url String
     * @param appId String
     * @param secret String
     * @param code String
     * @return AccessTokenBean
     */
    suspend fun accessToken(url: String, appId: String, secret: String, code: String) =
        withContext(Dispatchers.IO) {
            apiService.weiChatAccessToken(url, appId, secret, code)
        }

    /**
     * 刷新用户token
     * @param url String
     * @param appId String
     * @param refreshToken String
     */
    suspend fun refreshToken(url: String, appId: String, refreshToken: String) =
        withContext(Dispatchers.IO) {
            apiService.weiChatRefreshToken(url, appId, refreshToken)
        }

    /**
     * 校验accessToken是否有效
     * @param url String
     * @param accessToken String
     * @param openId String
     */
    suspend fun accessTokenValid(url: String, accessToken: String, openId: String) =
        withContext(Dispatchers.IO) {
            apiService.accessTokenValid(url, accessToken, openId)
        }

    /**
     * 获取微信用户信息
     * @param url String
     * @param accessToken String
     * @param openId String
     */
    suspend fun weiChatUserInfo(url: String, accessToken: String, openId: String) =
        withContext(Dispatchers.IO) {
            apiService.weiChatUserInfo(url, accessToken, openId)
        }

    /**
     * 微信 QQ登录
     * @param openType Int
     * @param openId String
     * @param nickName String
     * @param sex Int
     * @param img String
     * @param unionId String
     * @return BaseResponseData<LoginBean>
     */
    suspend fun login(
        openType: Int,
        openId: String,
        nickName: String,
        sex: String,
        img: String,
        unionId: String = ""
    ) = request {
        apiService.login(openType, openId, nickName, sex, img, unionId)
    }

    /**
     * apk升级下载
     * @param url String
     * @return ResponseBody
     */
    suspend fun downloadFile(url: String) = withContext(Dispatchers.IO){
        apiService.downloadFile(url)
    }

}