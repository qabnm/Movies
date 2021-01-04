package com.junliu.cinema.view

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.cinema.CinemaContext
import com.junliu.cinema.HistoryUtil
import com.junliu.cinema.R
import com.junliu.common.util.RouterPath
import com.junliu.common.util.SharedPreferencesHelper
import dc.android.bridge.util.OsUtils
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_search.*

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
        imgMore.setOnClickListener { onMoreClick() }
        imgClear.setOnClickListener {
            SharedPreferencesHelper.helper.remove(CinemaContext.local)
            layoutHistory.clear()
        }
        tvCancel.setOnClickListener {
            if (!TextUtils.isEmpty(etSearch.text)) HistoryUtil.save(etSearch.text.toString()) else finish()
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tvCancel.text = if (TextUtils.isEmpty(etSearch.text)) "取消" else "搜索"
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        setSearchHistory()
    }

    private fun setSearchHistory() {
        val history = HistoryUtil.getLocalHistory()
        Log.i("his", history.toString())
        layoutHistory.setData(history)
        if (history.isEmpty()) imgMore.visibility = View.GONE
        val layoutParams = layoutHistory.layoutParams as LinearLayout.LayoutParams
        if (layoutHistory.isSingLine) {
            layoutParams.height = OsUtils.dip2px(this, 38f)
            imgMore.visibility = View.GONE
        } else {
            layoutParams.height = OsUtils.dip2px(this, 76f)
            imgMore.visibility = View.VISIBLE
        }
    }

    /**
     * 展开所有的本地搜索记录
     */
    private fun onMoreClick() {
        val layoutParams = layoutHistory.layoutParams as LinearLayout.LayoutParams
        //展开所有的记录
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        imgMore.visibility = View.GONE
    }
}