package com.junliu.cinema.view

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.junliu.cinema.CinemaContext
import com.junliu.cinema.HistoryUtil
import com.junliu.cinema.R
import com.junliu.cinema.listener.HistoryClickCallback
import com.junliu.common.adapter.NavigatorAdapter
import com.junliu.common.adapter.ViewPagerAdapter
import com.junliu.common.util.FlowLayout
import com.junliu.common.util.SharedPreferencesHelper
import dc.android.bridge.util.OsUtils
import dc.android.bridge.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_search.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * @author: jun.liu
 * @date: 2021/1/8 14:19
 * @des:历史搜索 搜索热词
 */
class SearchFragment : BaseFragment(), HistoryClickCallback {
    override fun getLayoutId() = R.layout.fragment_search
    private var cb: HistoryClickCallback? = null

    fun setCallback(cb: HistoryClickCallback) {
        this.cb = cb
    }

    override fun initView() {
        imgMore.setOnClickListener { onMoreClick() }
        layoutHistory.setOnItemClickListener(listener)
        imgClear.setOnClickListener {
            SharedPreferencesHelper.helper.remove(CinemaContext.local)
            layoutHistory.clear()
        }
    }

    override fun initData() {
        val data = listOf("热搜", "电影", "电视剧", "美剧", "韩剧", "日剧")
        val fragmentList = ArrayList<Fragment>()
        for (i in data.indices) {
            val fragment = HotSearchFragment()
            fragment.setCallback(this)
            fragmentList.add(fragment)
            val bundle = Bundle()
            bundle.putString("type", "type")
            fragment.arguments = bundle
        }
        vpContainer.adapter = ViewPagerAdapter(childFragmentManager, fragmentList)
        CommonNavigator(requireActivity()).apply {
            adapter = NavigatorAdapter(viewPager = vpContainer, data = data)
            isAdjustMode = false
            indicator.navigator = this
        }
        ViewPagerHelper.bind(indicator, vpContainer)
        setSearchHistory()
    }

    /**
     * 处理历史记录的显示
     */
    fun setSearchHistory() {
        val history = HistoryUtil.getLocalHistory()
        layoutHistory.setData(history)
        if (history.isEmpty()) imgMore.visibility = View.GONE
        if (history.isNotEmpty()) {
            layoutHistoryContainer.visibility = View.VISIBLE
            layoutFlowLayout.visibility = View.VISIBLE
            val layoutParams = layoutHistory.layoutParams as LinearLayout.LayoutParams
            if (layoutHistory.isSingLine) {
                layoutParams.height = OsUtils.dip2px(requireActivity(), 38f)
                imgMore.visibility = View.GONE
            } else {
                layoutParams.height = OsUtils.dip2px(requireActivity(), 76f)
                imgMore.visibility = View.VISIBLE
            }
        } else {
            layoutHistoryContainer.visibility = View.GONE
            layoutFlowLayout.visibility = View.GONE
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

    private var isFirstSet = true
    private val listener: FlowLayout.OnItemClickListener = object : FlowLayout.OnItemClickListener {
        override fun OnItemClick(result: String) {
            //跳转到搜索的结果页面
            cb?.onHistoryClick(result)
        }

        override fun isSingLine(isSingLine: Boolean) {
            if (isSingLine && isFirstSet) {
                isFirstSet = false
                val layoutParams = layoutHistory.layoutParams as LinearLayout.LayoutParams
                layoutParams.height = OsUtils.dip2px(requireActivity(), 38f)
                imgMore.visibility = View.GONE
            }
        }

        override fun currentHeight(height: Int) {
            imgMore.visibility = if (height > 76) View.VISIBLE else View.GONE
        }
    }

    override fun onHistoryClick(result: String) {
        cb?.onHistoryClick(result)
    }
}