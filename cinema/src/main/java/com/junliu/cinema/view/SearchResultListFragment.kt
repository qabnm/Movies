package com.junliu.cinema.view

import androidx.recyclerview.widget.LinearLayoutManager
import com.junliu.cinema.CinemaContext
import com.junliu.cinema.R
import com.junliu.cinema.adapter.SearchResultListAdapter
import com.junliu.cinema.bean.SearchResultBean
import com.junliu.cinema.viewmodel.SearchResultViewModel
import dc.android.bridge.BridgeContext
import dc.android.bridge.view.BaseViewModelFragment
import kotlinx.android.synthetic.main.fragment_search_result_list.*

/**
 * @author: jun.liu
 * @date: 2021/1/8 16:59
 * @des:搜索结果的列表页
 */
class SearchResultListFragment : BaseViewModelFragment<SearchResultViewModel>() {
    private var resultAdapter: SearchResultListAdapter? = null
    override fun providerVMClass() = SearchResultViewModel::class.java

    override fun getLayoutId() = R.layout.fragment_search_result_list
    private var typeId = ""
    private var keyWord = ""
    private var page = 1

    override fun initView() {
        viewModel.getSearchResult().observe(this, { setData(viewModel.getSearchResult().value) })
        rvList.layoutManager = LinearLayoutManager(requireActivity())
        resultAdapter = SearchResultListAdapter()
        rvList.adapter = resultAdapter
    }

    private fun setData(resultBean: SearchResultBean?) {
        val dataList = resultBean?.result
        if (dataList?.isNotEmpty() == true) resultAdapter?.setList(dataList)
    }

    override fun initData() {
        typeId = arguments?.getString(BridgeContext.ID, "") ?: ""
        keyWord = arguments?.getString(CinemaContext.KEY_WORD, "") ?: ""
        viewModel.searchResult(keyWord = keyWord, page = page, column = typeId)
    }
}