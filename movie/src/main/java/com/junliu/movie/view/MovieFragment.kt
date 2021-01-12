package com.junliu.movie.view

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.junliu.common.adapter.NoLineIndicatorAdapter
import com.junliu.common.adapter.ViewPagerAdapter
import com.junliu.common.util.RouterPath
import com.junliu.movie.R
import dc.android.bridge.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_movie.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * @author: jun.liu
 * @date: 2020/12/29 : 14:37
 * 影片专题、片库、榜单
 */
@Route(path = RouterPath.PATH_MOVIE)
class MovieFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_movie

    override fun initView() {
        imgSearch.setOnClickListener {
            ARouter.getInstance().build(RouterPath.PATH_SEARCH_ACTIVITY).navigation()
        }
    }

    override fun initData() {
        val data = listOf("片库", "榜单")
        val fragmentList = ArrayList<Fragment>()
        val libFragment = MovieLibraryNavFragment()
        fragmentList.add(libFragment)

        val rankNavFragment = MovieRankNavFragment()
        fragmentList.add(rankNavFragment)
        vpContainer.adapter = ViewPagerAdapter(childFragmentManager, fragmentList)
        CommonNavigator(requireActivity()).apply {
            adapter = NoLineIndicatorAdapter(
                viewPager = vpContainer,
                data = data,
                unSelectColor = R.color.color666666,
                selectColor = R.color.color000000,
                unSelectSize = R.dimen.sp_15,
                selectSize = R.dimen.sp_20
            )
            isAdjustMode = false
            indicator.navigator = this
        }
        ViewPagerHelper.bind(indicator, vpContainer)
    }
}