package com.duoduovv.common.viewmodel

/**
 * @author: jun.liu
 * @date: 2021/6/21 9:42
 * @des:
 */
class PubRepository: PubApiRepository() {
    /**
     * 首页配置信息
     * @return BaseResponseData<ConfigureBean>
     */
    suspend fun configure() = request {
        apiService.configure()
    }
}