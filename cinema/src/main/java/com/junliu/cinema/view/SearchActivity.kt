package com.junliu.cinema.view

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.cinema.HistoryUtil
import com.junliu.cinema.R
import com.junliu.common.util.RouterPath
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_search.*
import java.util.logging.Logger

/**
 * @author: jun.liu
 * @date: 2021/1/4 10:13
 * @des: 搜索页面
 */
@Route(path = RouterPath.PATH_SEARCH_ACTIVITY)
class SearchActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_search

    override fun initData() {
        imgBack.setOnClickListener { finish() }
        tvCancel.setOnClickListener {
            if (!TextUtils.isEmpty(etSearch.text)){
                //保存搜索的记录到本地
                HistoryUtil.save(etSearch.text.toString())
            }
        }

        etSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tvCancel.text = if (TextUtils.isEmpty(etSearch.text)) "取消" else "搜索"
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        val history = HistoryUtil.getLocalHistory()
        Log.i("his",history.toString())
        layoutHistory.setData(history)
    }
}