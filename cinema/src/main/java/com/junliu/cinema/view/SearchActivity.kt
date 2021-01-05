package com.junliu.cinema.view

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.cinema.CinemaContext
import com.junliu.cinema.HistoryUtil
import com.junliu.cinema.R
import com.junliu.common.adapter.NavigatorAdapter
import com.junliu.common.adapter.ViewPagerAdapter
import com.junliu.common.util.FlowLayout
import com.junliu.common.util.RouterPath
import com.junliu.common.util.SharedPreferencesHelper
import dc.android.bridge.util.OsUtils
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_search.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator


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
        etSearch.addTextChangedListener(textChangeWatcher)
        setSearchHistory()
        val data = listOf("热搜","电影","电视剧","美剧","韩剧","日剧")
        val fragmentList = ArrayList<Fragment>()
        for (i in data.indices){
            val fragment = HotSearchFragment()
            fragmentList.add(fragment)
            val bundle = Bundle()
            bundle.putString("type","type")
            fragment.arguments = bundle
        }
        vpContainer.adapter = ViewPagerAdapter(supportFragmentManager,fragmentList)
        CommonNavigator(this).apply {
            adapter = NavigatorAdapter(vpContainer , data)
            isAdjustMode = false
            indicator.navigator = this
        }
        ViewPagerHelper.bind(indicator ,vpContainer)
    }

    /**
     * 处理历史记录的显示
     */
    private fun setSearchHistory() {
        layoutHistory.setOnItemClickListener(listener)
        val history = HistoryUtil.getLocalHistory()
        layoutHistory.setData(history)
        if (history.isEmpty()) imgMore.visibility = View.GONE
        if (history.isNotEmpty()) {
            layoutHistoryContainer.visibility = View.VISIBLE
            layoutFlowLayout.visibility = View.VISIBLE
            val layoutParams = layoutHistory.layoutParams as LinearLayout.LayoutParams
            if (layoutHistory.isSingLine) {
                layoutParams.height = OsUtils.dip2px(this, 38f)
                imgMore.visibility = View.GONE
            } else {
                layoutParams.height = OsUtils.dip2px(this, 76f)
                imgMore.visibility = View.VISIBLE
            }
        }else{
            layoutHistoryContainer.visibility = View.GONE
            layoutFlowLayout.visibility = View.GONE
        }
    }

    private val textChangeWatcher:TextWatcher = object :TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            tvCancel.text = if (TextUtils.isEmpty(etSearch.text)) "取消" else "搜索"
        }
        override fun afterTextChanged(s: Editable?) {}
    }

    private var isFirstSet = true
    private val listener: FlowLayout.OnItemClickListener = object : FlowLayout.OnItemClickListener {
        override fun OnItemClick(result: String) {
            //跳转到搜索的结果页面
        }

        override fun isSingLine(isSingLine: Boolean) {
            if (isSingLine && isFirstSet){
                isFirstSet = false
                val layoutParams = layoutHistory.layoutParams as LinearLayout.LayoutParams
                layoutParams.height = OsUtils.dip2px(this@SearchActivity, 38f)
                imgMore.visibility = View.GONE
            }
        }

        override fun currentHeight(height: Int) {
            imgMore.visibility = if (height>76) View.VISIBLE else View.GONE
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