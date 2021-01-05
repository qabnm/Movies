package com.junliu.cinema.view

import androidx.recyclerview.widget.GridLayoutManager
import com.junliu.cinema.R
import com.junliu.cinema.adapter.HotSearchAdapter
import com.junliu.cinema.bean.HotSearchBean
import dc.android.bridge.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_hot_search.*

/**
 * @author: jun.liu
 * @date: 2021/1/5 : 14:07
 * 热搜页面
 */
class HotSearchFragment : BaseFragment(){
    private var hotSearchAdapter: HotSearchAdapter?= null
    override fun getLayoutId() = R.layout.fragment_hot_search

    override fun initView() {
        rvList.layoutManager = GridLayoutManager(requireActivity(),2)
        hotSearchAdapter = HotSearchAdapter()
        rvList.adapter = hotSearchAdapter
    }
    override fun initData() {
        val data  = ArrayList<HotSearchBean>()
        for (i in 0 until 8){
            val bean = HotSearchBean("下一站是幸福")
            data.add(bean)
        }
        hotSearchAdapter?.setList(data)
    }
}