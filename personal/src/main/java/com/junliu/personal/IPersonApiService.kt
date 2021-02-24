package com.junliu.personal

import com.junliu.personal.bean.UserInfoBean
import dc.android.bridge.net.BaseResponseData
import retrofit2.http.GET

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
    suspend fun userInfo():BaseResponseData<UserInfoBean>
}