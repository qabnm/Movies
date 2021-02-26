package com.junliu.movie.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.junliu.common.adapter.ScaleTitleNavAdapter
import com.junliu.common.adapter.ViewPagerAdapter
import com.junliu.movie.R
import com.junliu.movie.bean.MovieRankCategoryBean
import com.junliu.movie.viewmodel.MovieRankCategoryViewModel
import dc.android.bridge.BridgeContext
import dc.android.bridge.view.BaseFragment
import dc.android.bridge.view.BaseViewModelFragment
import kotlinx.android.synthetic.main.fragment_movie_rank_nav.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * @author: jun.liu
 * @date: 2021/1/12 10:32
 * @des:影片榜单外部导航fragment
 */
class MovieRankNavFragment : BaseViewModelFragment<MovieRankCategoryViewModel>() {
    override fun getLayoutId() = R.layout.fragment_movie_rank_nav
    override fun providerVMClass() = MovieRankCategoryViewModel::class.java

    override fun initView() {
        viewModel.getMovieRankCategory().observe(this, {
            val result = viewModel.getMovieRankCategory().value
            initFragment(result)
        })
    }

    /**
     * 分类fragment初始化
     * @param categoryBean MovieRankCategoryBean?
     */
    private fun initFragment(categoryBean: MovieRankCategoryBean?) {
        if (null == categoryBean) return
        val data = categoryBean.columns
        val titleList = ArrayList<String>()
        val fragmentList = ArrayList<Fragment>()
        for (i in data.indices) {
            val fragment = MovieRankFragment()
            val bundle = Bundle()
            bundle.putString(BridgeContext.ID, data[i].id)
            fragment.arguments = bundle
            fragmentList.add(fragment)
            titleList.add(data[i].name)
        }
        vpContainer.adapter = ViewPagerAdapter(childFragmentManager, fragmentList)
        CommonNavigator(requireActivity()).apply {
            adapter = ScaleTitleNavAdapter(
                viewPager = vpContainer,
                data = titleList,
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

    override fun initData() {
        viewModel.movieRankCategory()
    }
}