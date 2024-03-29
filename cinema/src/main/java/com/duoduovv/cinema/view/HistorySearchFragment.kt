package com.duoduovv.cinema.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.duoduovv.advert.gdtad.GDTBannerAd
import com.duoduovv.advert.ttad.TTBannerAd
import com.duoduovv.cinema.CinemaContext
import com.duoduovv.cinema.R
import com.duoduovv.cinema.adapter.HotSearchAdapter
import com.duoduovv.cinema.component.HistoryUtil
import com.duoduovv.cinema.databinding.FragmentHistorySearchBinding
import com.duoduovv.cinema.listener.IHistoryClickCallback
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.util.FlowLayout
import com.duoduovv.common.util.SharedPreferencesHelper
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.TYPE_GDT_AD
import dc.android.bridge.BridgeContext.Companion.TYPE_TT_AD
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

    private var cb: IHistoryClickCallback? = null
    private var hotSearchAdapter: HotSearchAdapter? = null
    private var hotList: List<String>? = null
    private var gdtInfoAd: GDTBannerAd? = null
    private var ttInfoAd: TTBannerAd? = null

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
        BaseApplication.configBean?.ad?.searchBanner?.let {
            when(it.type){
                TYPE_TT_AD->{ initTTAd(it.value) }
                TYPE_GDT_AD ->{ initGDTAd(it.value) }
            }
        }
    }

    /**
     * 请求穿山甲广告
     */
    private fun initTTAd(posId:String) {
        ttInfoAd = TTBannerAd(requireActivity())
        val width = OsUtils.px2dip(requireContext(),OsUtils.getScreenWidth(requireContext()).toFloat()).toFloat() -20
        ttInfoAd?.initBanner(posId, width, 0f, mBind.adContainer)
    }

    /**
     * 请求广点通的信息流广告
     */
    private fun initGDTAd(posId: String) {
        gdtInfoAd = GDTBannerAd()
        gdtInfoAd?.initBanner(
            requireActivity(),
            posId,
            mBind.adContainer
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
        mBind.imgMore.visibility = View.GONE
        //展开所有的记录
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
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

    override fun onDestroyView() {
        ttInfoAd?.onDestroy()
        gdtInfoAd?.onDestroy()
        super.onDestroyView()
    }
}