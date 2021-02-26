package com.duoduovv.download.tool

import androidx.room.TypeConverter
import java.io.*

/**
 * @author: jun.liu
 * @date: 2020/12/19 18:18
 * @des:
 */
class Converters {
    @TypeConverter
    fun toByteArray(serializable: Serializable?): ByteArray? {
        serializable ?: return null
        var byteArrayOutputStream: ByteArrayOutputStream? = null
        var objectOutputStream: ObjectOutputStream? = null
        try {
            byteArrayOutputStream = ByteArrayOutputStream()
            objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
            objectOutputStream.writeObject(serializable)
            objectOutputStream.flush()
            return byteArrayOutputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            byteArrayOutputStream?.close()
            objectOutputStream?.close()
        }
        return null
    }

    @TypeConverter
    fun toSerializable(byteArray: ByteArray?): Serializable? {
        byteArray ?: return null
        var byteArrayOutputStream: ByteArrayInputStream? = null
        var objectInputStream: ObjectInputStream? = null
        try {
            byteArrayOutputStream = ByteArrayInputStream(byteArray)
            objectInputStream = ObjectInputStream(byteArrayOutputStream)
            return objectInputStream.readObject() as Serializable
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            byteArrayOutputStream?.close()
            objectInputStream?.close()
        }
        return null
    }
}