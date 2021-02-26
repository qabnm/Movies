package com.junliu.personal

import com.junliu.personal.bean.*
import dc.android.bridge.net.BaseResponseData
import retrofit2.http.*

/**
 * @author: jun.liu
 * @date: 2021/2/24 18:03
 * @des:
 */
interface IPersonApiService {

    /**
     * 用户信息
     * @return BaseResponseData<UserInfoBean>
     */
    @GET("api/user/info")
    suspend fun userInfo(): BaseResponseData<UserInfoBean>

    /**
     * 获取微信access_token
     */
    @GET
    suspend fun weiChatAccessToken(
        @Url url: String,
        @Query("appid") appId: String,
        @Query("secret") secret: String,
        @Query("code") code: String,
        @Query("grant_type") grantType: String = "authorization_code"
    ): AccessTokenBean

    /**
     * 刷新微信token
     * @param url String
     * @param appId String
     * @param refreshToken String
     * @param grantType String
     */
    @GET
    suspend fun weiChatRefreshToken(
        @Url url: String,
        @Query("appid") appId: String,
        @Query("refresh_token") refreshToken: String,
        @Query("grant_type") grantType: String = "refresh_token"
    ): RefreshTokenBean

    /**
     * 检查accessToken是否有效
     * @param url String
     * @param accessToken String
     * @param openId String
     * @return AccessTokenValidBean
     */
    @GET
    suspend fun accessTokenValid(
        @Url url: String,
        @Query("access_token") accessToken: String,
        @Query("openid") openId: String
    ): AccessTokenValidBean

    /**
     * 获取微信用户信息
     * @param url String
     * @param accessToken String
     * @param openId String
     * @return WeiChatUserInfoBean
     */
    @GET
    suspend fun weiChatUserInfo(
        @Url url: String,
        @Query("access_token") accessToken: String,
        @Query("openid") openId: String
    ): WeiChatUserInfoBean

    /**
     * 微信 QQ登录
     * @param openType Int
     * @param openId String
     * @param nickName String
     * @param sex Int
     * @param img String
     * @param unionId String
     */
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("open_type") openType: Int,
        @Field("open_id") openId: String,
        @Field("nick") nickName: String,
        @Field("sex") sex: Int,
        @Field("img") img: String,
        @Field("union_id") unionId: String = ""
    ): BaseResponseData<LoginBean>

    /**
     * 收藏列表
     * @return BaseResponseData<MyCollectionBean>
     */
    @GET("api/user/favorites")
    suspend fun collectionList(@Query("page") page: Int): BaseResponseData<MyCollectionBean>

    /**
     * 删除收藏
     * @param movieId String
     * @return BaseResponseData<Any>
     */
    @DELETE("api/user/favorite_1614157043983")
    suspend fun deleteCollection(@Query("movie_id") movieId: String): BaseResponseData<DeleteCollectionBean>
}