package com.junliu.cinema.view

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.cinema.HistoryUtil
import com.junliu.cinema.R
import com.junliu.cinema.listener.HistoryClickCallback
import com.junliu.common.util.RouterPath
import dc.android.bridge.util.OsUtils
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_search.*


/**
 * @author: jun.liu
 * @date: 2021/1/4 10:13
 * @des: 搜索页面
 */
@Route(path = RouterPath.PATH_SEARCH_ACTIVITY)
class SearchActivity : BridgeActivity(),HistoryClickCallback {
    override fun getLayoutId() = R.layout.activity_search
    private var searchFragment: SearchFragment? = null
    private var searchResultFragment: SearchResultFragment? = null
    private var isSearchClick = false

    override fun initData() {
        imgBack.setOnClickListener { finish() }
        tvCancel.setOnClickListener {
            if (!TextUtils.isEmpty(etSearch.text)) {
                toResultFragment(etSearch.text.toString())
            } else finish()
        }
        //添加显示搜索记录的fragment
        showSearchFragment()
        etSearch.addTextChangedListener(textChangeWatcher)
    }

    private fun toResultFragment(result: String){
        etSearch.clearFocus()
        OsUtils.hideKeyboard(this)
        isSearchClick = true
        if (searchFragment?.isVisible == true) searchFragment?.let { supportFragmentManager.beginTransaction().hide(it).commit() }
        showSearchResultFragment()
        HistoryUtil.save(result)
    }

    private fun showSearchResultFragment() {
        val ts = supportFragmentManager.beginTransaction()
        searchResultFragment?.takeIf { null != searchResultFragment }?.also { ts.show(it) } ?: run {
            searchResultFragment = SearchResultFragment()
            ts.add(R.id.layoutContainer, searchResultFragment!!)
        }
        ts.commit()
    }

    private fun showSearchFragment() {
        val ts = supportFragmentManager.beginTransaction()
        searchFragment?.takeIf { null != searchFragment }?.also { ts.show(it) } ?: run {
            searchFragment = SearchFragment()
            searchFragment?.setCallback(this)
            ts.add(R.id.layoutContainer, searchFragment!!)
        }
        ts.commit()
    }

    private val textChangeWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            tvCancel.text = if (TextUtils.isEmpty(etSearch.text)) "取消" else "搜索"
        }

        override fun afterTextChanged(s: Editable?) {
            if (TextUtils.isEmpty(etSearch.text) && isSearchClick) {
                if (searchResultFragment?.isVisible == true) searchResultFragment?.let { supportFragmentManager.beginTransaction().hide(it).commit() }
                if (searchFragment?.isVisible == false){
                    showSearchFragment()
                    searchFragment?.setSearchHistory()
                }
            }
        }
    }

    override fun onHistoryClick(result: String) {
        etSearch.setText(result)
        toResultFragment(result = result)
    }
}