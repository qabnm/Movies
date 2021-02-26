package com.junliu.cinema.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.junliu.cinema.CinemaContext
import com.junliu.cinema.R
import com.junliu.cinema.bean.SearchResultCategoryBean
import com.junliu.cinema.viewmodel.SearchResultCategoryViewModel
import com.junliu.common.adapter.NoLineIndicatorAdapter
import com.junliu.common.adapter.ViewPagerAdapter
import dc.android.bridge.BridgeContext
import dc.android.bridge.view.BaseViewModelFragment
import kotlinx.android.synthetic.main.fragment_search_result.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * @author: jun.liu
 * @date: 2021/1/8 14:54
 * @des:搜索结果
 */
class SearchResultFragment : BaseViewModelFragment<SearchResultCategoryViewModel>() {
    override fun getLayoutId() = R.layout.fragment_search_result
    override fun providerVMClass() = SearchResultCategoryViewModel::class.java
    private var keyWord = ""

    override fun initView() {
        viewModel.getCategory().observe(this, {
            val result = viewModel.getCategory().value
            initFragment(result)
        })
    }

    fun setKeyWord(keyWord: String) {
        this.keyWord = keyWord
    }

    private fun initFragment(categoryBean: SearchResultCategoryBean?) {
        val dataList = categoryBean?.columns
        if (dataList?.isNotEmpty() == true) {
            val titleList = ArrayList<String>()
            val fragmentList = ArrayList<Fragment>()
            for (i in dataList.indices) {
                val fragment = SearchResultListFragment()
                val bundle = Bundle()
                bundle.putString(BridgeContext.ID, dataList[i].id)
                bundle.putString(CinemaContext.KEY_WORD, keyWord)
                fragment.arguments = bundle
                fragmentList.add(fragment)
                titleList.add(dataList[i].name)
            }
            vpContainer.adapter = ViewPagerAdapter(childFragmentManager, fragmentList)
            CommonNavigator(requireActivity()).apply {
                adapter = NoLineIndicatorAdapter(viewPager = vpContainer, data = titleList)
                isAdjustMode = false
                indicator.navigator = this
            }
            ViewPagerHelper.bind(indicator, vpContainer)
        }
    }

    override fun initData() {
        viewModel.searchResultCategory()
    }

}