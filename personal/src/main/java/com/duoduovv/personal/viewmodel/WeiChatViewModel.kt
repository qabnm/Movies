package com.duoduovv.personal.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.personal.bean.*
import com.duoduovv.personal.repository.PersonRepository
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/2/25 13:53
 * @des:
 */
class WeiChatViewModel : BaseViewModel() {
    private val repository = PersonRepository()

    private var accessToken: MutableLiveData<AccessTokenBean> = MutableLiveData()
    fun getAccessToken() = accessToken
    private var refreshToken: MutableLiveData<RefreshTokenBean> = MutableLiveData()
    fun getRefreshToken() = refreshToken
    private var accessTokenValid: MutableLiveData<AccessTokenValidBean> = MutableLiveData()
    fun getAccessTokenValid() = accessTokenValid
    private var weiChartUserInfo: MutableLiveData<WeiChatUserInfoBean> = MutableLiveData()
    fun getWeiChatUseInfo() = weiChartUserInfo
    private var token: MutableLiveData<LoginBean> = MutableLiveData()
    fun getToken() = token
    private var userInfo: MutableLiveData<User> = MutableLiveData()
    fun getUserInfo() = userInfo

    /**
     * 获取微信accessToken
     * @param url String
     * @param appId String
     * @param secret String
     * @param code String
     * @return Job
     */
    fun accessToken(url: String, appId: String, secret: String, code: String) = request {
        val result = repository.accessToken(url, appId, secret, code)
        accessToken.postValue(result)
    }

    /**
     * 刷新用户token
     * @param url String
     * @param appId String
     * @param token String
     * @return Job
     */
    fun refreshToken(url: String, appId: String, token: String) = request {
        val result = repository.refreshToken(url = url, appId = appId, refreshToken = token)
        refreshToken.postValue(result)
    }

    /**
     * 校验accessToken是否有效
     * @param url String
     * @param accessToken String
     * @param openId String
     * @return Job
     */
    fun accessTokenValid(url: String, accessToken: String, openId: String) = request {
        val result =
            repository.accessTokenValid(url = url, accessToken = accessToken, openId = openId)
        result.accessToken = accessToken
        result.openId = openId
        accessTokenValid.postValue(result)
    }

    /**
     * 获取微信用户信息
     * @param url String
     * @param accessToken String
     * @param openId String
     * @return Job
     */
    fun weiCharUserInfo(url: String, accessToken: String, openId: String) = request {
        val result = repository.weiChatUserInfo(url, accessToken, openId)
        weiChartUserInfo.postValue(result)
    }

    /**
     * 微信 QQ登录
     * @param openType Int
     * @param openId String
     * @param nickName String
     * @param sex Int
     * @param img String
     * @param unionId String
     * @return Job
     */
    fun login(
        openType: Int,
        openId: String,
        nickName: String,
        sex: Int,
        img: String,
        unionId: String = ""
    ) = request {
        val result = repository.login(openType, openId, nickName, sex, img, unionId)
        val bean = result.data
        bean.img = img
        bean.nickName = nickName
        if (result.code == SUCCESS) token.postValue(result.data)
    }

    /**
     * 获取用户信息
     * @return Job
     */
    fun userInfo() = request {
        val result = repository.userInfo()
        if (result.code == SUCCESS) userInfo.postValue(result.data.user)
    }
}