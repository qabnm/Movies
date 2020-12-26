package com.jun.liu.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.jun.liu.common.BaseApplication
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import kotlin.jvm.Throws
import kotlin.reflect.KProperty

/**
 * @author: jun.liu
 * @date: 2020/12/26 10:09
 * @des:
 */
class Preference<T>(private val keyName: String="",private val defaultValue: T? = null) {
    private val spKey = "default"
    private val prefs :SharedPreferences by lazy {
        BaseApplication.baseCtx.getSharedPreferences(spKey , Context.MODE_PRIVATE)
    }

    /**
     * 删除全部数据
     */
    fun clear() {
        prefs.edit().clear().apply()
    }

    /**
     * 根据key删除存储数据
     */
    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T  {
        Log.d("info", "调用$this 的getValue()")
        return findSharedPreference(keyName, defaultValue!!)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        Log.d("info", "调用$this 的setValue() value参数值为：$value")
        putSharedPreferences(keyName, value)
    }

    @SuppressLint("CommitPrefEdits")
    private fun  putSharedPreferences(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> putString(name, serialize(value))
        }.apply()
    }

    /**
     * 反序列化对象
     */
    @Throws(Exception::class)
    private fun <T> deSerialization(str: String?): T? {
        val redStr = java.net.URLDecoder.decode(str, "UTF-8")
        val byteArrayInputStream = ByteArrayInputStream(
            redStr.toByteArray(charset("ISO-8859-1")))
        val objectInputStream = ObjectInputStream(
            byteArrayInputStream)
        val obj = objectInputStream.readObject() as? T
        objectInputStream.close()
        byteArrayInputStream.close()
        return obj
    }

    /**
     * 序列化对象
     */
    @Throws(Exception::class)
    private fun <T> serialize(obj: T): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(
            byteArrayOutputStream)
        objectOutputStream.writeObject(obj)
        var serStr = byteArrayOutputStream.toString("ISO-8859-1")
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8")
        objectOutputStream.close()
        byteArrayOutputStream.close()
        return serStr
    }
    /**
     * 查找数据 返回一个具体的对象
     * 没有查找到value 就返回默认的序列化对象，然后经过反序列化返回
     */
    @Suppress("UNCHECKED_CAST")
    private fun  findSharedPreference(name: String, default: T): T = with(prefs) {
        val res: Any? = when (default) {
            is Long -> getLong(name, 0L)
            is String -> getString(name, "")
            is Int -> getInt(name, 0)
            is Boolean -> getBoolean(name, false)
            is Float -> getFloat(name, 0f)
            else -> deSerialization(getString(name, serialize(default)))
        }
        res as T
    }
}