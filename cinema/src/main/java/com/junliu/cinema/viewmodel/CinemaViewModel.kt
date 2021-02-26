package com.junliu.cinema.viewmodel

import androidx.lifecycle.MutableLiveData
import com.junliu.cinema.bean.ConfigureBean
import com.junliu.cinema.repository.CinemaRepository
import dc.android.bridge.BridgeContext
import dc.android.bridge.net.BaseResponseData
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/1/19 17:41
 * @des:
 */
class CinemaViewModel : BaseViewModel() {
    private var configure: MutableLiveData<BaseResponseData<ConfigureBean>> = MutableLiveData()
    fun getConfigure() = configure
    private val repository = CinemaRepository()

    /**
     * 首页配置
     * @return Job
     */
    fun configure() = request {
        val result = repository.configure()
        if (result.code == BridgeContext.SUCCESS) configure.postValue(result)
    }
}