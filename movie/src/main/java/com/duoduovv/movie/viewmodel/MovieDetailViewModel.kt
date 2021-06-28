package com.duoduovv.movie.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.duoduovv.common.BaseApplication
import com.duoduovv.movie.bean.JxPlayUrlBean
import com.duoduovv.movie.bean.MovieDetailBean
import com.duoduovv.movie.bean.MoviePlayInfoBean
import com.duoduovv.movie.repository.MovieRepository
import com.duoduovv.room.database.CollectionDatabase
import com.duoduovv.room.database.WatchHistoryDatabase
import com.duoduovv.room.domain.CollectionBean
import com.duoduovv.room.domain.VideoWatchHistoryBean
import dc.android.bridge.net.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: jun.liu
 * @date: 2021/1/26 17:24
 * @des: 视频详情
 */
class MovieDetailViewModel : BaseViewModel() {
    private var movieDetail: MutableLiveData<MovieDetailBean> = MutableLiveData()
    fun getMovieDetail() = movieDetail
    private var moviePlayInfo: MutableLiveData<MoviePlayInfoBean> = MutableLiveData()
    fun getMoviePlayInfo() = moviePlayInfo

    //以下是点击播放  直接可以播放
    private var movieClickInfo: MutableLiveData<MoviePlayInfoBean> = MutableLiveData()
    fun getMovieClickInfo() = movieClickInfo

    private var analysisPlayUrl: MutableLiveData<JxPlayUrlBean> = MutableLiveData()
    fun getPlayUrl() = analysisPlayUrl
    private var jxUrl: MutableLiveData<String> = MutableLiveData()
    fun getJxUrl() = jxUrl

    private val repository = MovieRepository()

    /**
     * 视频详情
     * @param id String  视频ID
     * @param num String  播放级数
     * @return Job
     */
    fun movieDetail(id: String, vid: String = "") = request {
        val result = repository.movieDetail(id = id, vid = vid)
        if (isSuccess(result.code)) movieDetail.postValue(result.data)
    }

    /**
     * 获取播放信息
     * @param vid String
     * @param id String
     * @return Job
     */
    fun moviePlayInfo(vid: String, id: String, line: String, js: String, flag: Int = 0) = request(false) {
        val result = repository.moviePlayInfo(vid, id, line, js)
        if (isSuccess(result.code)) {
            if (flag == 0) {
                moviePlayInfo.postValue(result.data)
            } else {
                movieClickInfo.postValue(result.data)
            }
        }
    }

    /**
     * 更新历史观看记录
     */
    fun updateHistoryDB(
        progress: Int,
        detailBean: MovieDetailBean,
        movieId: String,
        vid: String,
        vidTitle: String,
        duration: Int
    ) {
        //保存下当前播放的视频信息
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("videoPlayer", "当前播放的进度是：$progress")
            if (progress > 0) {
                //首先查询数据库是否有当前影片 如果有了就执行update操作
                val dataList =
                    WatchHistoryDatabase.getInstance(BaseApplication.baseCtx).history().queryAllByDate()
                if (dataList?.isNotEmpty() == true) {
                    var updateBean: VideoWatchHistoryBean? = null
                    for (i in dataList.indices) {
                        if (movieId == dataList[i].movieId) {
                            //如果已经存在数据库中 直接执行更新操作
                            updateBean = dataList[i]
                        }
                    }
                    updateBean?.let {
                        it.vid = vid
                        it.vidTitle = vidTitle
                        it.currentLength = progress
                        it.currentTime = System.currentTimeMillis()
                        WatchHistoryDatabase.getInstance(BaseApplication.baseCtx).history()
                            .update(it)
                    } ?: also {
                        //最多保存100条观看记录
                        if (dataList.size == 100) {
                            WatchHistoryDatabase.getInstance(BaseApplication.baseCtx).history()
                                .delete(dataList[0])
                        }
                        insertHistory(detailBean, progress, movieId, vid, vidTitle, duration)
                    }
                } else {
                    insertHistory(detailBean, progress, movieId, vid, vidTitle, duration)
                }
            }
        }
    }

    /**
     * 通过id查询影片
     * @param movieId String
     * @return VideoWatchHistoryBean
     */
    suspend fun queryMovieById(movieId: String) = withContext(Dispatchers.IO) {
        WatchHistoryDatabase.getInstance(BaseApplication.baseCtx).history().queryById(movieId)
    }

    /**
     * 通过id查询收藏状态
     * @param id String
     * @return CollectionBean
     */
    suspend fun queryCollectionById(id: String) = withContext(Dispatchers.IO) {
        CollectionDatabase.getInstance(BaseApplication.baseCtx).collection().queryById(id)
    }

    /**
     * 删除收藏
     * @param collectionBean CollectionBean
     */
    suspend fun deleteCollection(collectionBean: CollectionBean) = withContext(Dispatchers.IO) {
        CollectionDatabase.getInstance(BaseApplication.baseCtx).collection().delete(collectionBean)
    }

    /**
     * 添加收藏
     * @param collectionBean CollectionBean
     */
    suspend fun addCollection(collectionBean: CollectionBean) = withContext(Dispatchers.IO) {
        CollectionDatabase.getInstance(BaseApplication.baseCtx).collection().insert(collectionBean)
    }

    /**
     * 插入数据
     * @param bean MovieDetailBean
     * @param progress Int
     */
    private fun insertHistory(
        bean: MovieDetailBean,
        progress: Int,
        movieId: String,
        vid: String,
        vidTitle: String,
        duration: Int
    ) {
        //当前有视频播放 将播放的视频信息添加或者更新到数据库
        val flag = bean.movie.movieFlag
        val videoBean = VideoWatchHistoryBean(
            coverUrl = bean.movie.coverUrl,
            title = bean.movie.vodName,
            type = flag,
            movieId = movieId,
            vid = vid,
            currentLength = progress,
            vidTitle = vidTitle,
            currentTime = System.currentTimeMillis(),
            totalLength = duration
        )
        WatchHistoryDatabase.getInstance(BaseApplication.baseCtx).history().insert(videoBean)
    }

    /**
     * 解析播放地址
     * @param vid String
     * @param movieId String
     * @param line String
     */
    fun analysisPlayUrl(vid: String, movieId: String, line: String, content: String) = request(false) {
        val result = repository.analysisPlayUrl(vid, movieId, line, content)
        if (isSuccess(result.code)) {
            analysisPlayUrl.postValue(result.data)
        }
    }

    /**
     * 解析三方的地址
     * @param url String
     * @param headers Map<String, String>
     * @return Job
     */
    fun jxUrlForGEet(url: String, headers: Map<String, String>) = request(false) {
        val result = repository.jxUrlForGEet(url, headers)
        jxUrl.postValue(result.string())
    }

    /**
     * 解析三方地址  post请求
     * @param url String
     * @param headers Map<String, String>
     * @param map Map<String, String>
     * @return Job
     */
    fun jxUrlForPost(url: String, headers: Map<String, String>, map: Map<String, String>) =
        request(false) {
            val result = repository.jxUrlForPost(url, headers, map)
            jxUrl.postValue(result.string())
        }

    /**
     * 视频播放失败
     * @param vid String
     * @param url String
     * @param message String
     * @return Job
     */
    fun playError(vid: String, url: String, message: String) = request(false) {
        repository.playError(vid, url, message)
    }
}