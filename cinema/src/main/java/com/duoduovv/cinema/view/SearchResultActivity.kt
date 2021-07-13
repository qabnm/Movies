package com.duoduovv.cinema.view

import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import com.alibaba.android.arouter.facade.annotation.Route
import com.duoduovv.cinema.CinemaContext
import com.duoduovv.cinema.R
import com.duoduovv.cinema.component.HistoryUtil
import com.duoduovv.cinema.component.SearchResultFragmentPagerAdapter
import com.duoduovv.cinema.databinding.ActivitySearchResultBinding
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.adapter.NoLineIndicatorAdapter
import com.duoduovv.common.domain.Column
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.viewmodel.ConfigureViewModel
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.view.BaseViewModelActivity
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * @author: jun.liu
 * @date: 2021/7/12 16:30
 * @des:搜索结果页面
 */
@Route(path = RouterPath.PATH_SEARCH_RESULT)
class SearchResultActivity : BaseViewModelActivity<ConfigureViewModel>() {
    override fun getLayoutId() = R.layout.activity_search_result
    override fun providerVMClass() = ConfigureViewModel::class.java
    private lateinit var mBind: ActivitySearchResultBinding
    private var keyWord = ""

    override fun initView() {
        mBind = ActivitySearchResultBinding.bind(layoutView)
        viewModel.getConfigure().observe(this, {
            val result = viewModel.getConfigure().value
            initFragment(result?.columns)
        })
        mBind.tvCancel.setOnClickListener {
            onCancelClick()
        }
        mBind.etSearch.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                if (TextUtils.isEmpty(mBind.etSearch.text)) {
                    AndroidUtils.toast("请输入搜索内容", this)
                } else {
                    //更新搜索的内容
                    keyWord = mBind.etSearch.text.toString().trim()
//                    SearchResultListFragment.keyWords = keyWord
//                    SearchResultListFragment.instance?.setKeyWord(keyWord)
                    HistoryUtil.save(keyWord)
                }
            }
            false
        }
    }

    private fun onCancelClick() {
        val intent = Intent()
        intent.putExtra("from", "from")
        this.setResult(200, intent)
        finish()
    }

    override fun onBackPressed() {
        onCancelClick()
    }

    private fun initFragment(dataList: List<Column>?) {
        if (dataList?.isNotEmpty() == true) {
            val titleList = ArrayList<String>()
            for (i in dataList.indices) {
                if (dataList[i].name == "精选") dataList[i].name = "全部"
                titleList.add(dataList[i].name)
            }
            mBind.vpContainer.adapter = SearchResultFragmentPagerAdapter(
                supportFragmentManager,
                titleList.size,
                dataList,
                keyWord
            )
            CommonNavigator(this).apply {
                adapter = NoLineIndicatorAdapter(viewPager = mBind.vpContainer, data = titleList)
                isAdjustMode = false
                mBind.indicator.navigator = this
            }
            ViewPagerHelper.bind(mBind.indicator, mBind.vpContainer)
        }
    }

    override fun initData() {
        keyWord = intent.getStringExtra(CinemaContext.KEY_WORD) ?: ""
        mBind.etSearch.setText(keyWord)
        mBind.etSearch.setSelection(keyWord.length)
        BaseApplication.configBean?.let {
            initFragment(it.columns)
        } ?: also {
            viewModel.configure()
        }
    }
}