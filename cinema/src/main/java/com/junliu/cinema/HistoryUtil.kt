package com.junliu.cinema

import android.util.Log
import com.google.gson.Gson
import com.junliu.common.util.SharedPreferencesHelper
import com.junliu.common.util.StringUtils
import org.json.JSONArray

/**
 * @author: jun.liu
 * @date: 2021/1/4 14:15
 * @des:搜索历史
 */
class HistoryUtil {
    companion object {
        private const val local = "history"

        /**
         * 获取本地历史记录
         * @return List<String>
         */
        fun getLocalHistory(): List<String> {
            val json = SharedPreferencesHelper.helper.getValue(local, "") as String
            Log.i("his",json)
            val data = ArrayList<String>()
            if (!StringUtils.isEmpty(json)) {
                val array = JSONArray(json)
                if (array.length() > 0) {
                    for (i in 0 until array.length()){
                        data.add(array[i].toString())
                    }
                }
            }
            return data
        }


        fun save(keyWord: String) {
            Log.i("his","方法执行了")
            val history: String = SharedPreferencesHelper.helper.getValue(local, "") as String
            Log.i("his",history)
            val data = ArrayList<String>()
            //本地还没有保存
            if (StringUtils.isEmpty(history)) {
                data.add(0, keyWord)
                val jsonResult = Gson().toJson(data)
                //保存下来
                SharedPreferencesHelper.helper.setValue(local, jsonResult)
            } else {
                //有数据 先把保存的数据取出来
                val jsonArray = JSONArray(history)
                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        data.add(jsonArray[i].toString())
                    }
                }
                //移除相同的数据
                for (i in 0 until data.size) {
                    if (keyWord == data[i]) data.removeAt(i)
                }
                //最多只保存20条记录
                if (data.size == 20) data.removeAt(19)
                //再保存数据
                data.add(0, keyWord)
                val result = Gson().toJson(data)
                SharedPreferencesHelper.helper.setValue(local, result)
            }
        }
    }
}