package com.junliu.movie.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.junliu.common.adapter.ScaleTitleNavAdapter
import com.junliu.common.adapter.ViewPagerAdapter
import com.junliu.movie.R
import dc.android.bridge.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_movie_library_nav.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * @author: jun.liu
 * @date: 2021/1/12 10:31
 * @des:影片库外部导航fragment
 */
class MovieLibraryNavFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_movie_library_nav

    override fun initData() {
        val data = listOf("电视剧", "电影", "纪录片", "恐怖片", "伦理片", "韩剧", "美剧")
        val fragmentList = ArrayList<Fragment>()
        for (i in data.indices) {
            val fragment = MovieLibraryFragment()
            val bundle = Bundle()
            bundle.putString("type", "type")
            fragmentList.add(fragment)
        }
        vpContainer.adapter = ViewPagerAdapter(childFragmentManager, fragmentList)
        CommonNavigator(requireActivity()).apply {
            adapter = ScaleTitleNavAdapter(
                viewPager = vpContainer,
                data = data,
                unSelectColor = R.color.color666666,
                selectColor = R.color.color000000,
                unSelectSize = R.dimen.sp_14,
                selectSize = R.dimen.sp_15
            )
            isAdjustMode = false
            indicator.navigator = this
        }
        ViewPagerHelper.bind(indicator, vpContainer)
    }
}