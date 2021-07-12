package com.duoduovv.cinema.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.duoduovv.cinema.R
import com.duoduovv.cinema.component.SearchResultFragmentPagerAdapter
import com.duoduovv.cinema.databinding.FragmentSearchResultBinding
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.adapter.NoLineIndicatorAdapter
import com.duoduovv.common.domain.Column
import com.duoduovv.common.viewmodel.ConfigureViewModel
import dc.android.bridge.view.BaseViewModelFragment
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * @author: jun.liu
 * @date: 2021/1/8 14:54
 * @des:搜索结果
 */
class SearchResultFragment : BaseViewModelFragment<ConfigureViewModel>() {
    override fun getLayoutId() = R.layout.fragment_search_result
    private lateinit var mBind: FragmentSearchResultBinding
    override fun providerVMClass() = ConfigureViewModel::class.java
    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSearchResultBinding.inflate(inflater, container, false)

    private var keyWord = ""

    override fun initView() {
        mBind = baseBinding as FragmentSearchResultBinding
        viewModel.getConfigure().observe(this, {
            val result = viewModel.getConfigure().value
            initFragment(result?.columns)
        })
    }

    fun setKeyWord(keyWord: String) {
        this.keyWord = keyWord
    }

    private fun initFragment(dataList: List<Column>?) {
        if (dataList?.isNotEmpty() == true) {
            val titleList = ArrayList<String>()
            for (i in dataList.indices) {
                if (dataList[i].name == "精选") dataList[i].name = "全部"
                titleList.add(dataList[i].name)
            }
            mBind.vpContainer.adapter = SearchResultFragmentPagerAdapter(
                childFragmentManager,
                titleList.size,
                dataList,
                keyWord
            )
            CommonNavigator(requireActivity()).apply {
                adapter = NoLineIndicatorAdapter(viewPager = mBind.vpContainer, data = titleList)
                isAdjustMode = titleList.size <= 6
                mBind.indicator.navigator = this
            }
            ViewPagerHelper.bind(mBind.indicator, mBind.vpContainer)
        }
    }

    override fun initData() {
        BaseApplication.configBean?.let {
            initFragment(it.columns)
        } ?: also {
            viewModel.configure()
        }
    }
}