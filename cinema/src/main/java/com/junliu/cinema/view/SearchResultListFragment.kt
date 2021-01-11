package com.junliu.cinema.view

import androidx.recyclerview.widget.LinearLayoutManager
import com.junliu.cinema.R
import com.junliu.cinema.adapter.SearchResultListAdapter
import com.junliu.cinema.bean.SearchResultBean
import dc.android.bridge.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_search_result_list.*

/**
 * @author: jun.liu
 * @date: 2021/1/8 16:59
 * @des:搜索结果的列表页
 */
class SearchResultListFragment :BaseFragment() {
    private var resultAdapter:SearchResultListAdapter?= null

    override fun getLayoutId() = R.layout.fragment_search_result_list

    override fun initView() {
        rvList.layoutManager = LinearLayoutManager(requireActivity())
        resultAdapter = SearchResultListAdapter()
        rvList.adapter = resultAdapter
    }

    override fun initData() {
        val data = ArrayList<SearchResultBean>()
        val coverUrl = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1274215983,3875630385&fm=26&gp=0.jpg"
        for (i in 0 until 8){
            data.add(SearchResultBean(coverUrl, "釜山行", "2015", "病毒", "韩国", "韩语","米花浩瀚",40))
        }
        resultAdapter?.setList(data)
    }
}