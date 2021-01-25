package com.junliu.cinema.view

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.junliu.cinema.CinemaContext
import com.junliu.cinema.HistoryUtil
import com.junliu.cinema.R
import com.junliu.cinema.adapter.HotSearchAdapter
import com.junliu.cinema.listener.HistoryClickCallback
import com.junliu.common.util.FlowLayout
import com.junliu.common.util.SharedPreferencesHelper
import dc.android.bridge.BridgeContext
import dc.android.bridge.util.OsUtils
import dc.android.bridge.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * @author: jun.liu
 * @date: 2021/1/8 14:19
 * @des:历史搜索 搜索热词
 */
class SearchFragment : BaseFragment(), HistoryClickCallback {
    override fun getLayoutId() = R.layout.fragment_search
    private var cb: HistoryClickCallback? = null
    private var hotSearchAdapter: HotSearchAdapter? = null
    private var hotList:List<String>? =null

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
        rvList.layoutManager = GridLayoutManager(requireActivity(), 2)
        hotSearchAdapter = HotSearchAdapter()
        rvList.adapter = hotSearchAdapter
        hotSearchAdapter?.setOnItemClickListener { adapter, _, position ->
            cb?.onHistoryClick(adapter.data[position] as String)
        }
    }

    override fun initData() {
        hotList = arguments?.getStringArrayList(BridgeContext.LIST)
        hotSearchAdapter?.setList(hotList)
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