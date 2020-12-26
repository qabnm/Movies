package com.junliu.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jun.liu.common.util.Preference
import com.jun.liu.common.util.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //添加个注释

        btnSet.setOnClickListener {
            SharedPreferencesHelper.helper.setValue("age","小明")
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