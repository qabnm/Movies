package com.junliu.room

import androidx.room.*
import com.junliu.room.domain.VideoWatchHistoryBean

/**
 * @author: jun.liu
 * @date: 2021/1/21 17:10
 * @des:操作数据的接口
 */
@Dao
interface HistoryDao {
    /**
     * 查询所有数据
     * @return List<VideoWatchHistoryBean>
     */
    @Query("select * from VideoWatchHistoryBean")
    fun queryAll(): List<VideoWatchHistoryBean>

    /**
     * 插入数据
     * @param bean VideoWatchHistoryBean
     * @return List<VideoWatchHistoryBean>
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bean: VideoWatchHistoryBean): List<VideoWatchHistoryBean>

    /**
     * 删除数据
     * @param bean VideoWatchHistoryBean
     * @return List<VideoWatchHistoryBean>
     */
    @Delete
    fun delete(bean: VideoWatchHistoryBean): List<VideoWatchHistoryBean>

    /**
     * 更新数据
     * @param bean VideoWatchHistoryBean
     * @return List<VideoWatchHistoryBean>
     */
    @Update
    fun update(bean: VideoWatchHistoryBean): List<VideoWatchHistoryBean>
}