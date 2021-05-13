package com.duoduovv.movie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.duoduovv.common.adapter.ScaleTitleNavAdapter
import com.duoduovv.common.adapter.ViewPagerAdapter
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.MovieRankCategoryBean
import com.duoduovv.movie.databinding.FragmentMovieRankNavBinding
import com.duoduovv.movie.viewmodel.MovieRankCategoryViewModel
import dc.android.bridge.BridgeContext
import dc.android.bridge.view.BaseViewModelFragment
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
    private lateinit var mBind: FragmentMovieRankNavBinding
    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMovieRankNavBinding.inflate(inflater, container, false)

    override fun initView() {
        mBind = baseBinding as FragmentMovieRankNavBinding
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
            if (data[i].name == "精选") data[i].name = "全部"
            titleList.add(data[i].name)
        }
        mBind.vpContainer.adapter = ViewPagerAdapter(childFragmentManager, fragmentList)
        CommonNavigator(requireActivity()).apply {
            adapter = ScaleTitleNavAdapter(
                viewPager = mBind.vpContainer,
                data = titleList,
                unSelectColor = R.color.color666666,
                selectColor = R.color.color000000,
                unSelectSize = R.dimen.sp_14,
                selectSize = R.dimen.sp_15
            )
            isAdjustMode = false
            mBind.indicator.navigator = this
        }
        ViewPagerHelper.bind(mBind.indicator, mBind.vpContainer)
    }

    override fun initData() {
        viewModel.movieRankCategory()
    }
}