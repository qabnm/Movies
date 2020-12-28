package com.junliu.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.junliu.common.util.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //添加个注释

        btnSet.setOnClickListener {
            SharedPreferencesHelper.helper.setValue("age","小明")
//            WeiChatTool.regToQQ(applicationContext)
        }

        btnGet.setOnClickListener {
            val result = SharedPreferencesHelper.helper.getValue("age","")
            Log.i("age" , result as String)
        }

        btnRemove.setOnClickListener {
            SharedPreferencesHelper.helper.remove("age")
        }
    }
}