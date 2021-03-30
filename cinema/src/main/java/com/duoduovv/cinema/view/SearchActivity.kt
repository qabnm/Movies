package com.duoduovv.cinema.view

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.cinema.component.HistoryUtil
import com.duoduovv.cinema.R
import com.duoduovv.cinema.listener.IHistoryClickCallback
import com.duoduovv.common.util.RouterPath
import dc.android.bridge.BridgeContext
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.util.OsUtils
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_search.*


/**
 * @author: jun.liu
 * @date: 2021/1/4 10:13
 * @des: 搜索页面
 */
@Route(path = RouterPath.PATH_SEARCH_ACTIVITY)
class SearchActivity : BridgeActivity(), IHistoryClickCallback {
    override fun getLayoutId() = R.layout.activity_search
    private var searchFragment: SearchFragment? = null
    private var searchResultFragment: SearchResultFragment? = null
    private var isSearchClick = false
    private var hotList: ArrayList<String>? = null

    override fun initView() {
        ARouter.getInstance().inject(this)
        hotList = intent.getStringArrayListExtra(BridgeContext.LIST)
        imgBack.setOnClickListener { finish() }
        tvCancel.setOnClickListener { onCancelClick() }
        //添加显示搜索记录的fragment
        showSearchFragment()
        etSearch.addTextChangedListener(textChangeWatcher)
        etSearch.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN){
                if (TextUtils.isEmpty(etSearch.text)){
                    AndroidUtils.toast("请输入搜索内容",this)
                }else{
                    onCancelClick()
                }
            }
            false
        }
    }

    private fun onCancelClick(){
        if (!TextUtils.isEmpty(etSearch.text)) {
            if (searchResultFragment?.isVisible == true) return
            toResultFragment(etSearch.text.toString())
        } else{
            finish()
        }
    }

    override fun initData() {

    }

    private fun toResultFragment(result: String) {
        etSearch.clearFocus()
        OsUtils.hideKeyboard(this)
        isSearchClick = true
        if (searchFragment?.isVisible == true) searchFragment?.let {
            supportFragmentManager.beginTransaction().hide(it).commit()
        }
        showSearchResultFragment(result)
        HistoryUtil.save(result)
    }

    private fun showSearchResultFragment(result: String) {
        val ts = supportFragmentManager.beginTransaction()
//        searchResultFragment?.takeIf { null != searchResultFragment }?.also {
//            it.initData()
//            ts.show(it)
//        } ?: run {
            searchResultFragment = SearchResultFragment()
            searchResultFragment?.setKeyWord(result)
            ts.add(R.id.layoutContainer, searchResultFragment!!)
//        }
        ts.commit()
    }

    private fun showSearchFragment() {
        val ts = supportFragmentManager.beginTransaction()
        searchFragment?.takeIf { null != searchFragment }?.also { ts.show(it) } ?: run {
            searchFragment = SearchFragment()
            val bundle = Bundle()
            bundle.putStringArrayList(BridgeContext.LIST, hotList)
            searchFragment?.arguments = bundle
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
                if (searchResultFragment?.isVisible == true) searchResultFragment?.let {
                    supportFragmentManager.beginTransaction().remove(it).commit()
                }
                if (searchFragment?.isVisible == false) {
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