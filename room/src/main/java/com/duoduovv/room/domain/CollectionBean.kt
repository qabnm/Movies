package com.duoduovv.room.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author: jun.liu
 * @date: 2021/4/6 18:58
 * @des:收藏
 * @Entity：注解的类对应数据库中的一张表，我们可以指定表名，如果不指定的话，默认是类的名字
 * @PrimaryKey：每一个表都需要一个主键，这点需要注意，Room就是根据主键是否相同来判断是否是同一个对象。
 * @ColumnInfo：指定类的属性在表中列的名字，如果不指定，默认就是属性名
 */
@Entity
data class CollectionBean(
    @PrimaryKey(autoGenerate = true)
    var number: Long = 0,
    var coverUrl:String,
    var strId:String,
    var movieId:String,
    var lastRemark:String,
    var actor:String,
    var direcotor:String,
    var movieName:String,
    var lang:String,
    var isCollect:Boolean,
    var collectionTime:Long
)