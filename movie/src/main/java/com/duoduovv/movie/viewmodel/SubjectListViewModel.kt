package com.duoduovv.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.movie.bean.SubjectListBean
import com.duoduovv.movie.repository.MovieRepository
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/6/30 15:22
 * @des:专题 列表页
 */
class SubjectListViewModel :BaseViewModel() {
    private val subjectList = MutableLiveData<List<SubjectListBean>>()
    fun getSubjectList() = subjectList
    private val repository = MovieRepository()

    /**
     * 专题列表
     * @return Job
     */
    fun subjectList() = request {
        val result = repository.subjectList()
        if (result.code == SUCCESS)subjectList.postValue(result.data.subject)
    }
}