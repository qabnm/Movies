package com.duoduovv.main.repository

/**
 * @author: jun.liu
 * @date: 2021/5/28 18:25
 * @des:
 */
class MainRepository :MainApiRepository() {
    /**
     * 首页配置信息
     * @return BaseResponseData<ConfigureBean>
     */
    suspend fun configure() = request {
        apiService.configure()
    }
}