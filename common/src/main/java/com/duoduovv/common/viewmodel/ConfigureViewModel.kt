package com.duoduovv.common.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.common.domain.ConfigureBean
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/6/21 9:43
 * @des:
 */
open class ConfigureViewModel:BaseViewModel() {
    private var configure: MutableLiveData<ConfigureBean> = MutableLiveData()
    fun getConfigure() = configure
    private val repository = PubRepository()

    /**
     * 首页配置
     * @return Job
     */
    fun configure() = request {
        val result = repository.configure()
        if (isSuccess(result.code)) configure.postValue(result.data)
    }
}