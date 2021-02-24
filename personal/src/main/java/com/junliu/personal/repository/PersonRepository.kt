package com.junliu.personal.repository

/**
 * @author: jun.liu
 * @date: 2021/2/24 18:02
 * @des:用户中心
 */
class PersonRepository :PersonApiRepository() {

    /**
     * 获取用户信息
     * @return BaseResponseData<UserInfoBean>
     */
    suspend fun userInfo() = request {
        apiService.userInfo()
    }

}