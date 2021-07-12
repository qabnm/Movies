package com.duoduovv.movie.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.adapter.ScaleTitleNavAdapter
import com.duoduovv.common.domain.Column
import com.duoduovv.common.viewmodel.ConfigureViewModel
import com.duoduovv.movie.R
import com.duoduovv.movie.component.MovieRankFragmentPagerAdapter
import com.duoduovv.movie.databinding.FragmentMovieRankNavBinding
import dc.android.bridge.view.BaseViewModelFragment
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * @author: jun.liu
 * @date: 2021/1/12 10:32
 * @des:影片榜单外部导航fragment
 */
class MovieRankNavFragment : BaseViewModelFragment<ConfigureViewModel>() {
    override fun getLayoutId() = R.layout.fragment_movie_rank_nav
    override fun providerVMClass() = ConfigureViewModel::class.java
    private lateinit var mBind: FragmentMovieRankNavBinding
    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMovieRankNavBinding.inflate(inflater, container, false)

    override fun initView() {
        mBind = baseBinding as FragmentMovieRankNavBinding
        viewModel.getConfigure().observe(this, {
            val result = viewModel.getConfigure().value?.columns
            initFragment(result)
        })
    }

    companion object {
        fun newInstance() = MovieRankNavFragment()
    }

    /**
     * 分类fragment初始化
     * @param data MovieRankCategoryBean?
     */
    private fun initFragment(data: List<Column>?) {
        if (data?.isNotEmpty() == true) {
            val titleList = ArrayList<String>()
            for (i in data.indices) {
                if (data[i].name == "精选") data[i].name = "全部"
                titleList.add(data[i].name)
            }
            mBind.vpContainer.adapter = MovieRankFragmentPagerAdapter(
                childFragmentManager,
                titleList.size,
                data as ArrayList<Column>
            )
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
    }

    override fun initData() {
        BaseApplication.configBean?.let {
            initFragment(it.columns)
        } ?: also {
            viewModel.configure()
        }
    }
}