package com.junliu.cinema.view

import androidx.recyclerview.widget.GridLayoutManager
import com.junliu.cinema.R
import com.junliu.cinema.adapter.HotSearchAdapter
import com.junliu.cinema.bean.HotSearchBean
import com.junliu.cinema.listener.HistoryClickCallback
import dc.android.bridge.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_hot_search.*

/**
 * @author: jun.liu
 * @date: 2021/1/5 : 14:07
 * 热搜页面
 */
class HotSearchFragment : BaseFragment() {
    private var hotSearchAdapter: HotSearchAdapter? = null
    private var cb: HistoryClickCallback? = null
    override fun getLayoutId() = R.layout.fragment_hot_search

    fun setCallback(cb: HistoryClickCallback) {
        this.cb = cb
    }

    override fun initView() {
        rvList.layoutManager = GridLayoutManager(requireActivity(), 2)
        hotSearchAdapter = HotSearchAdapter()
        rvList.adapter = hotSearchAdapter
        hotSearchAdapter?.setOnItemClickListener { adapter, _, position ->
            cb?.onHistoryClick((adapter.data[position] as HotSearchBean).name)
        }
    }

    override fun initData() {
        val data = ArrayList<HotSearchBean>()
        for (i in 0 until 8) {
            val bean = HotSearchBean("下一站是幸福")
            data.add(bean)
        }
        hotSearchAdapter?.setList(data)
    }
}