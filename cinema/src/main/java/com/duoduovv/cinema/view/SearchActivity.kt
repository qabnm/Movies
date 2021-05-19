package com.duoduovv.cinema.view

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.advert.gdtad.GDTInfoAd
import com.duoduovv.advert.ttad.TTInfoAd
import com.duoduovv.cinema.R
import com.duoduovv.cinema.component.HistoryUtil
import com.duoduovv.cinema.databinding.ActivitySearchBinding
import com.duoduovv.cinema.listener.IHistoryClickCallback
import com.duoduovv.common.util.RouterPath
import dc.android.bridge.BridgeContext
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.util.OsUtils
import dc.android.bridge.view.BridgeActivity
import dc.android.tools.LiveDataBus


/**
 * @author: jun.liu
 * @date: 2021/1/4 10:13
 * @des: 搜索页面
 */
@Route(path = RouterPath.PATH_SEARCH_ACTIVITY)
class SearchActivity : BridgeActivity(), IHistoryClickCallback {
    override fun getLayoutId() = R.layout.activity_search
    private var historySearchFragment: HistorySearchFragment? = null
    private var searchResultFragment: SearchResultFragment? = null
    private var isSearchClick = false
    private var hotList: ArrayList<String>? = null
    private lateinit var mBind: ActivitySearchBinding
    private var gdtInfoAd: GDTInfoAd? = null
    private var ttInfoAd: TTInfoAd? = null

    override fun initView() {
        mBind = ActivitySearchBinding.bind(layoutView)
        ARouter.getInstance().inject(this)
        hotList = intent.getStringArrayListExtra(BridgeContext.LIST)
        mBind.imgBack.setOnClickListener { onBackClick() }
        mBind.tvCancel.setOnClickListener { onCancelClick() }
        //添加显示搜索记录的fragment
        showSearchFragment()
        mBind.etSearch.addTextChangedListener(textChangeWatcher)
        mBind.etSearch.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                if (TextUtils.isEmpty(mBind.etSearch.text)) {
                    AndroidUtils.toast("请输入搜索内容", this)
                } else {
                    onCancelClick()
                }
            }
            false
        }
    }

    override fun initData() {
        LiveDataBus.get().with("render", String::class.java).observe(
            this,
            { if (it == "render") mBind.rlTop.setBackgroundResource(R.color.colorFFFFFF) })
        //请求广告
        initGDTAd()
//        initTTAd()
    }

    /**
     * 请求穿山甲广告
     */
    private fun initTTAd() {
        ttInfoAd = TTInfoAd()
        ttInfoAd?.initTTInfoAd(this, "946107576", 0f, 170f, mBind.container)
    }

    /**
     * 请求广点通的信息流广告
     */
    private fun initGDTAd() {
        gdtInfoAd = GDTInfoAd()
        gdtInfoAd?.initInfoAd(
            this,
            "5051684812707537",
            mBind.container,
            390,
            170
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        ttInfoAd?.destroyInfoAd()
        gdtInfoAd?.destroyInfoAd()
    }

    /**
     * 返回键监听
     */
    private fun onBackClick() {
        if (!TextUtils.isEmpty(mBind.etSearch.text)) {
            mBind.etSearch.setText("")
        } else {
            finish()
        }
    }

    /**
     * 取消或者搜索键的点击
     */
    private fun onCancelClick() {
        if (!TextUtils.isEmpty(mBind.etSearch.text)) {
            if (searchResultFragment?.isVisible == true) return
            toResultFragment(mBind.etSearch.text.toString())
        } else {
            finish()
        }
    }

    /**
     * 显示搜索结果页面
     * @param result String
     */
    private fun toResultFragment(result: String) {
        mBind.etSearch.clearFocus()
        OsUtils.hideKeyboard(this)
        isSearchClick = true
        if (historySearchFragment?.isVisible == true) historySearchFragment?.let {
            supportFragmentManager.beginTransaction().hide(it).commit()
        }
        showSearchResultFragment(result)
        HistoryUtil.save(result)
    }

    /**
     * 显示搜索记录页面
     * @param result String
     */
    private fun showSearchResultFragment(result: String) {
        val ts = supportFragmentManager.beginTransaction()
        searchResultFragment = SearchResultFragment()
        searchResultFragment?.setKeyWord(result)
        ts.add(R.id.layoutContainer, searchResultFragment!!)
        ts.commit()
    }

    /**
     * 显示历史搜索 热门搜索的fragment
     */
    private fun showSearchFragment() {
        val ts = supportFragmentManager.beginTransaction()
        historySearchFragment?.takeIf { null != historySearchFragment }?.also { ts.show(it) }
            ?: run {
                historySearchFragment = HistorySearchFragment()
                val bundle = Bundle()
                bundle.putStringArrayList(BridgeContext.LIST, hotList)
                historySearchFragment?.arguments = bundle
                historySearchFragment?.setCallback(this)
                ts.add(R.id.layoutContainer, historySearchFragment!!)
            }
        ts.commit()
    }

    private val textChangeWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            mBind.tvCancel.text = if (TextUtils.isEmpty(mBind.etSearch.text)) "取消" else "搜索"
        }

        override fun afterTextChanged(s: Editable?) {
            if (TextUtils.isEmpty(mBind.etSearch.text) && isSearchClick) {
                if (searchResultFragment?.isVisible == true) searchResultFragment?.let {
                    supportFragmentManager.beginTransaction().remove(it).commit()
                }
                if (historySearchFragment?.isVisible == false) {
                    showSearchFragment()
                    historySearchFragment?.setSearchHistory()
                }
            }
        }
    }

    /**
     * 历史搜索记录的按钮点击
     * @param result String
     */
    override fun onHistoryClick(result: String) {
        mBind.etSearch.setText(result)
        toResultFragment(result = result)
    }
}