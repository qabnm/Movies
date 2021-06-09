package com.duoduovv.movie.component

import com.duoduovv.movie.bean.MovieDetail
import com.duoduovv.movie.bean.MovieItem
import com.duoduovv.room.domain.CollectionBean

/**
 * @author: jun.liu
 * @date: 2021/5/26 20:05
 * @des:
 */
interface MovieDetailCallback {
    fun onShareClick()
    fun onDownLoadClick()
    fun onCollectClick(collectionBean: CollectionBean?)
    fun onDetailClick(bean: MovieDetail)
    fun onSelectClick(dataList: List<MovieItem>)
    fun onSelectClick(vid: String, movieId: String, vidTitle: String)
    fun onArtSelectClick(dataList: List<MovieItem>)
}