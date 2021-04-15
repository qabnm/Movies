package com.duoduovv.personal.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.personal.bean.User
import com.duoduovv.personal.repository.PersonRepository
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/2/24 18:02
 * @des:用户中心
 */
class PersonViewModel : BaseViewModel() {
    private var userInfo: MutableLiveData<User> = MutableLiveData()
    fun getUserInfo() = userInfo
    private val repository = PersonRepository()

    /**
     * 获取用户信息
     * @return Job
     */
    fun userInfo() = request {
        val result = repository.userInfo()
        if (result.code == SUCCESS) userInfo.postValue(result.data.user)
    }

}