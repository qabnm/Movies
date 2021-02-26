package com.duoduovv.hotspot.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.duoduovv.common.adapter.ScaleTitleNavAdapter
import com.duoduovv.common.adapter.ViewPagerAdapter
import com.duoduovv.common.util.RouterPath
import com.duoduovv.hotspot.R
import com.shuyu.gsyvideoplayer.GSYVideoManager
import dc.android.bridge.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_hotspot.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * @author: jun.liu
 * @date: 2020/12/29 : 14:34
 * 短视频页面
 */
@Route(path = RouterPath.PATH_HOTSPOT)
class HotSpotFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_hotspot

    override fun initView() {
        val data = listOf("推荐", "电影", "电视剧", "美剧", "韩剧", "动漫", "综艺")
        val fragmentList = ArrayList<Fragment>()
        for (i in data.indices) {
            val fragment = ShortVideoFragment()
            fragmentList.add(fragment)
            val bundle = Bundle()
            bundle.putString("type", "type")
            fragment.arguments = bundle
        }
        vpContainer.adapter = ViewPagerAdapter(fm = childFragmentManager, data = fragmentList)
        CommonNavigator(requireActivity()).apply {
            adapter = ScaleTitleNavAdapter(vpContainer, data)
            isAdjustMode = false
            indicator.navigator = this
        }
        ViewPagerHelper.bind(indicator, vpContainer)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d("hide",hidden.toString())
        if (hidden) GSYVideoManager.onPause() else GSYVideoManager.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }
}