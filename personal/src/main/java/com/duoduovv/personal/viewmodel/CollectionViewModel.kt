package com.duoduovv.personal.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.personal.bean.DeleteCollectionBean
import com.duoduovv.personal.bean.MyCollectionBean
import com.duoduovv.personal.repository.PersonRepository
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/2/25 16:27
 * @des:收藏
 */
class CollectionViewModel : BaseViewModel() {
    private val collection: MutableLiveData<MyCollectionBean> = MutableLiveData()
    fun getCollection() = collection

    private val delete: MutableLiveData<DeleteCollectionBean> = MutableLiveData()
    fun deleteState() = delete
    private val repository = PersonRepository()

    /**
     * 收藏列表
     * @param page Int
     * @return Job
     */
    fun collectionList(page: Int) = request {
        val result = repository.collectionList(page)
        if (result.code == SUCCESS) collection.postValue(result.data)
    }

    /**
     * 删除收藏
     * @param movieId String
     * @return Job
     */
    fun deleteCollection(movieId: String) = request {
        val result = repository.deleteCollection(movieId)
        if (result.code == SUCCESS) delete.postValue(result.data)
    }
}