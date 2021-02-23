package com.junliu.common.view

import android.text.Editable
import android.text.TextWatcher

/**
 * @author: jun.liu
 * @date: 2021/2/23 11:00
 * @des:
 */
open class MyTextWatcher :TextWatcher{
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }
}