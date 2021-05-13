package com.duoduovv.cinema.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.duoduovv.cinema.CinemaContext
import com.duoduovv.cinema.R
import com.duoduovv.cinema.bean.SearchResultCategoryBean
import com.duoduovv.cinema.databinding.FragmentSearchResultBinding
import com.duoduovv.cinema.viewmodel.SearchResultCategoryViewModel
import com.duoduovv.common.adapter.NoLineIndicatorAdapter
import com.duoduovv.common.adapter.ViewPagerAdapter
import dc.android.bridge.BridgeContext
import dc.android.bridge.view.BaseViewModelFragment
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * @author: jun.liu
 * @date: 2021/1/8 14:54
 * @des:搜索结果
 */
class SearchResultFragment : BaseViewModelFragment<SearchResultCategoryViewModel>() {
    override fun getLayoutId() = R.layout.fragment_search_result
    private lateinit var mBind:FragmentSearchResultBinding
    override fun providerVMClass() = SearchResultCategoryViewModel::class.java
    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) = FragmentSearchResultBinding.inflate(inflater,container,false)

    private var keyWord = ""

    override fun initView() {
        mBind = baseBinding as FragmentSearchResultBinding
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
                if (dataList[i].name == "精选") dataList[i].name = "全部"
                titleList.add(dataList[i].name)
            }
            mBind.vpContainer.adapter = ViewPagerAdapter(childFragmentManager, fragmentList)
            CommonNavigator(requireActivity()).apply {
                adapter = NoLineIndicatorAdapter(viewPager = mBind.vpContainer, data = titleList)
                isAdjustMode = titleList.size <= 6
                mBind.indicator.navigator = this
            }
            ViewPagerHelper.bind(mBind.indicator, mBind.vpContainer)
        }
    }

    override fun initData() {
        viewModel.searchResultCategory()
    }

}