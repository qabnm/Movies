package com.duoduovv.room.dao

import androidx.room.*
import com.duoduovv.room.domain.CollectionBean

/**
 * @author: jun.liu
 * @date: 2021/4/6 19:22
 * @des:收藏记录
 */
@Dao
interface CollectionDao {

    /**
     * 查询所有数据
     * @return List<CollectionBean>?
     */
    @Query("select * from CollectionBean order by collectionTime desc")
    fun queryAllByDate():List<CollectionBean>?

    /**
     * 通过id查询数据
     * @param id String
     * @return CollectionBean
     */
    @Query("select * from CollectionBean where movieId=(:id)")
    fun queryById(id: String): CollectionBean

    /**
     * 插入数据
     * @param bean CollectionBean
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bean: CollectionBean)

    /**
     * 删除数据
     * @param bean CollectionBean
     */
    @Delete
    fun delete(bean: CollectionBean)

    /**
     * 更新数据
     * @param bean CollectionBean
     */
    @Update
    fun update(bean: CollectionBean)
}