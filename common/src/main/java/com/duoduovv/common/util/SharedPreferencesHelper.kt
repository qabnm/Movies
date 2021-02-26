package com.duoduovv.common.util

import android.content.Context
import android.content.SharedPreferences
import com.duoduovv.common.BaseApplication

/**
 * @author: jun.liu
 * @date: 2020/12/26 10:42
 * @des: 本地数据存储工具类
 */
class SharedPreferencesHelper private constructor() {
    private val preference: SharedPreferences
    private val helperKey = "default"

    companion object {
        val helper by lazy { SharedPreferencesHelper() }
    }

    init {
        preference = BaseApplication.baseCtx.getSharedPreferences(helperKey, Context.MODE_PRIVATE)
    }

    /**
     *保存数据
     */
    fun <T> setValue(key: String, value: T) {
        preference.edit().apply {
            when (value) {
                is Int -> putInt(key, value)
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                else -> throw TypeCastException("Type not supported")
            }
            apply()
        }
    }

    /**
     * 获取保存的值
     */
    fun <T> getValue(key: String, default: T) = when (default) {
        is Int -> preference.getInt(key, default)
        is String -> preference.getString(key, default)
        is Boolean -> preference.getBoolean(key, default)
        is Float -> preference.getFloat(key, default)
        else -> throw TypeCastException("Type not supported")
    }

    /**
     * 根据Key移除数据
     */
    fun remove(key: String) {
        preference.edit().remove(key).apply()
    }

    /**
     * 清除所有保存的数据
     */
    fun clear() {
        preference.edit().clear().apply()
    }
}