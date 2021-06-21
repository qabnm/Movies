package com.duoduovv.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.movie.repository.MovieRepository
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/3/31 14:23
 * @des:举报
 */
class ReportViewModel : BaseViewModel() {
    private var report: MutableLiveData<Int> = MutableLiveData()
    fun getReport() = report
    private val repository = MovieRepository()

    /**
     * 举报
     * @param content String
     * @param movieId String
     * @return Job
     */
    fun report(content: String, movieId: String) = request {
        val result = repository.report(content, movieId)
        if (isSuccess(result.code)) report.postValue(SUCCESS)
    }
}