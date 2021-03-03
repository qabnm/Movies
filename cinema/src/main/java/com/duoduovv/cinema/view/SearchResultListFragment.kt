package com.duoduovv.cinema.view

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.duoduovv.cinema.CinemaContext
import com.duoduovv.cinema.R
import com.duoduovv.cinema.adapter.SearchResultListAdapter
import com.duoduovv.cinema.bean.SearchResultBean
import com.duoduovv.cinema.viewmodel.SearchResultViewModel
import dc.android.bridge.BridgeContext
import dc.android.bridge.view.BaseViewModelFragment
import kotlinx.android.synthetic.main.fragment_search_result_list.*
import kotlinx.android.synthetic.main.layout_search_empty.*

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
        if (dataList?.isNotEmpty() == true) {
            layoutEmpty.visibility = View.GONE
            rvList.visibility = View.VISIBLE
            resultAdapter?.setList(dataList)
        }else{
            layoutEmpty.visibility = View.VISIBLE
            rvList.visibility = View.GONE
        }
    }

    override fun initData() {
        typeId = arguments?.getString(BridgeContext.ID, "") ?: ""
        keyWord = arguments?.getString(CinemaContext.KEY_WORD, "") ?: ""
        viewModel.searchResult(keyWord = keyWord, page = page, column = typeId)
    }
}