package com.junliu.cinema.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.junliu.cinema.R
import com.junliu.common.adapter.NavigatorAdapter
import com.junliu.common.adapter.NoLineIndicatorAdapter
import com.junliu.common.adapter.ViewPagerAdapter
import dc.android.bridge.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_search_result.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * @author: jun.liu
 * @date: 2021/1/8 14:54
 * @des:搜索结果
 */
class SearchResultFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_search_result

    override fun initData() {
        val data = listOf("全部", "电影", "电视剧", "纪录片")
        val fragmentList = ArrayList<Fragment>()
        for (i in data.indices) {
            val fragment = SearchResultListFragment()
            fragmentList.add(fragment)
            val bundle = Bundle()
            bundle.putString("type", "type")
            fragment.arguments = bundle
        }
        vpContainer.adapter = ViewPagerAdapter(childFragmentManager, fragmentList)
        CommonNavigator(requireActivity()).apply {
            adapter = NoLineIndicatorAdapter(viewPager = vpContainer, data = data)
            isAdjustMode = false
            indicator.navigator = this
        }
        ViewPagerHelper.bind(indicator, vpContainer)
    }

}