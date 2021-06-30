package com.duoduovv.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.movie.bean.SubjectDetailListBean
import com.duoduovv.movie.repository.MovieRepository
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/6/30 15:32
 * @des:专题详情页
 */
class SubjectDetailViewModel : BaseViewModel() {
    private val subjectDetail = MutableLiveData<List<SubjectDetailListBean>>()
    fun getSubjectDetail() = subjectDetail
    private val repository = MovieRepository()

    /**
     * 专题详情页
     * @param subjectId String
     * @return Job
     */
    fun subjectDetail(subjectId: String) = request {
        val result = repository.subjectDetail(subjectId)
        if (result.code == SUCCESS) subjectDetail.postValue(result.data.list)
    }
}