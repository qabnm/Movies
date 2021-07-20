package com.duoduovv.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.movie.bean.SubjectListBean
import com.duoduovv.movie.repository.MovieRepository
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/6/30 15:22
 * @des:专题 列表页
 */
class SubjectListViewModel : BaseViewModel() {
    private val subjectList = MutableLiveData<List<SubjectListBean>>()
    fun getSubjectList() = subjectList
    private var noMoreData: MutableLiveData<String> = MutableLiveData()
    fun getNoMoreData() = noMoreData
    private val repository = MovieRepository()
    private val dataList = ArrayList<SubjectListBean>()

    /**
     * 专题列表
     * @return Job
     */
    fun subjectList(page: Int) = request {
        val result = repository.subjectList(page)
        if (isSuccess(result.code)){
            if (page == 1) dataList.clear()
            val list = result.data.subject
            if (list?.isNotEmpty() == true){
                dataList.addAll(list)
                subjectList.postValue(dataList)
            }else{
                if (page !=1){
                    noMoreData.postValue(BridgeContext.NO_MORE_DATA)
                }else{
                    subjectList.postValue(dataList)
                }
            }
        }
    }
}