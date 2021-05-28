package com.duoduovv.cinema.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.duoduovv.advert.gdtad.GDTInfoAd
import com.duoduovv.advert.ttad.TTInfoAd
import com.duoduovv.cinema.CinemaContext
import com.duoduovv.cinema.R
import com.duoduovv.cinema.adapter.HotSearchAdapter
import com.duoduovv.cinema.component.HistoryUtil
import com.duoduovv.cinema.databinding.FragmentHistorySearchBinding
import com.duoduovv.cinema.listener.IHistoryClickCallback
import com.duoduovv.common.util.FlowLayout
import com.duoduovv.common.util.SharedPreferencesHelper
import dc.android.bridge.BridgeContext
import dc.android.bridge.util.OsUtils
import dc.android.bridge.view.BaseFragment

/**
 * @author: jun.liu
 * @date: 2021/1/8 14:19
 * @des:历史搜索 搜索热词
 */
class HistorySearchFragment : BaseFragment(), IHistoryClickCallback {
    private lateinit var mBind: FragmentHistorySearchBinding
    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHistorySearchBinding.inflate(inflater, container, false)

    override fun getLayoutId() = R.layout.fragment_history_search
    private var cb: IHistoryClickCallback? = null
    private var hotSearchAdapter: HotSearchAdapter? = null
    private var hotList: List<String>? = null
    private var gdtInfoAd: GDTInfoAd? = null
    private var ttInfoAd: TTInfoAd? = null

    fun setCallback(cb: IHistoryClickCallback) {
        this.cb = cb
    }

    override fun initView() {
        mBind = baseBinding as FragmentHistorySearchBinding
        mBind.imgMore.setOnClickListener { onMoreClick() }
        mBind.layoutHistory.setOnItemClickListener(listener)
        mBind.imgClear.setOnClickListener {
            SharedPreferencesHelper.helper.remove(CinemaContext.local)
            mBind.layoutHistory.clear()
            setSearchHistory()
        }
        hotSearchAdapter = HotSearchAdapter()
        mBind.rvList.adapter = hotSearchAdapter
        hotSearchAdapter?.setOnItemClickListener { adapter, _, position ->
            cb?.onHistoryClick(adapter.data[position] as String)
        }
    }

    override fun initData() {
        hotList = arguments?.getStringArrayList(BridgeContext.LIST)
        hotSearchAdapter?.setList(hotList)
        setSearchHistory()
        initTTAd()
    }

    /**
     * 请求穿山甲广告
     */
    private fun initTTAd() {
        ttInfoAd = TTInfoAd()
        ttInfoAd?.initTTInfoAd(requireActivity(), "946164817", 0f, 0f, mBind.adContainer)
    }

    /**
     * 请求广点通的信息流广告
     */
    private fun initGDTAdTop() {
        gdtInfoAd = GDTInfoAd()
        gdtInfoAd?.initInfoAd(
            requireActivity(),
            "5051684812707537",
            mBind.adContainer,
            375,
            0
        )
    }

    /**
     * 处理历史记录的显示
     */
    fun setSearchHistory() {
        val history = HistoryUtil.getLocalHistory()
        mBind.layoutHistory.setData(history)
        if (history.isEmpty()) mBind.imgMore.visibility = View.GONE
        if (history.isNotEmpty()) {
            mBind.layoutHistoryContainer.visibility = View.VISIBLE
            mBind.layoutFlowLayout.visibility = View.VISIBLE
            val layoutParams = mBind.layoutHistory.layoutParams as LinearLayout.LayoutParams
            if (mBind.layoutHistory.isSingLine) {
                layoutParams.height = OsUtils.dip2px(requireActivity(), 38f)
                mBind.imgMore.visibility = View.GONE
            } else {
                layoutParams.height = OsUtils.dip2px(requireActivity(), 76f)
                mBind.imgMore.visibility = View.VISIBLE
            }
        } else {
            mBind.layoutHistoryContainer.visibility = View.GONE
            mBind.layoutFlowLayout.visibility = View.GONE
        }
    }

    /**
     * 展开所有的本地搜索记录
     */
    private fun onMoreClick() {
        val layoutParams = mBind.layoutHistory.layoutParams as LinearLayout.LayoutParams
        //展开所有的记录
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        mBind.imgMore.visibility = View.GONE
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
                val layoutParams = mBind.layoutHistory.layoutParams as LinearLayout.LayoutParams
                layoutParams.height = OsUtils.dip2px(requireActivity(), 38f)
                mBind.imgMore.visibility = View.GONE
            }
        }

        override fun currentHeight(height: Int) {
            mBind.imgMore.visibility = if (height > 76) View.VISIBLE else View.GONE
        }
    }

    override fun onHistoryClick(result: String) {
        cb?.onHistoryClick(result)
    }

    override fun onDestroy() {
        super.onDestroy()
        ttInfoAd?.destroyInfoAd()
        gdtInfoAd?.destroyInfoAd()
    }
}