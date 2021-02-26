package com.duoduovv.common.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * @author: jun.liu
 * @date: 2020/12/25 17:47
 * @des:
 */
class DataStoreUtils private constructor() {
    private val name = "DataStore"
    lateinit var dataStore: DataStore<Preferences>

    fun init(context: Context) {
        dataStore = context.createDataStore(name)
    }

    /**
     * 从dataStore读取数据
     */
    suspend inline fun <reified T : Any> read(key: String) = when (T::class) {
        String::class -> dataStore.data.map { it[preferencesKey<T>(key)] ?: "" }.first() as T
        Int::class -> dataStore.data.map { it[preferencesKey<T>(key)] ?: 0 }.first() as T
        Boolean::class -> dataStore.data.map { it[preferencesKey<T>(key)]?:false }.first() as T
        Float::class -> dataStore.data.map { it[preferencesKey<T>(key)]?:0f }.first() as T
        Long::class -> dataStore.data.map { it[preferencesKey<T>(key)]?:0L }.first() as T
        else -> throw IllegalArgumentException("Type not supported: ${T::class.java}")
    }

    /**
     * dataStore 只支持以下的几种数据类型
     */
    suspend inline fun <reified T : Any> save(key: String, value: T) = when (T::class) {
        String::class -> dataStore.edit { it[preferencesKey<T>(key)] = value }
        Int::class -> dataStore.edit { it[preferencesKey<T>(key)] = value }
        Boolean::class -> dataStore.edit { it[preferencesKey<T>(key)] = value }
        Float::class -> dataStore.edit { it[preferencesKey<T>(key)] = value }
        Long::class -> dataStore.edit { it[preferencesKey<T>(key)] = value }
        else -> throw IllegalArgumentException("Type not supported: ${T::class.java}")
    }

    companion object {
        val instance: DataStoreUtils by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DataStoreUtils() }
    }
}