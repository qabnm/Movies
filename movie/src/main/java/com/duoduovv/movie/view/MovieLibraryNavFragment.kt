package com.duoduovv.movie.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.duoduovv.common.adapter.ScaleTitleNavAdapter
import com.duoduovv.common.adapter.ViewPagerAdapter
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.Config
import com.duoduovv.movie.component.MovieLibraryFragmentPagerAdapter
import com.duoduovv.movie.databinding.FragmentMovieLibraryNavBinding
import com.duoduovv.movie.viewmodel.MovieLibCategoryViewModel
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.LIST
import dc.android.bridge.view.BaseViewModelFragment
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * @author: jun.liu
 * @date: 2021/1/12 10:31
 * @des:影片库外部导航fragment
 */
class MovieLibraryNavFragment : BaseViewModelFragment<MovieLibCategoryViewModel>() {
    override fun getLayoutId() = R.layout.fragment_movie_library_nav
    override fun providerVMClass() = MovieLibCategoryViewModel::class.java
    private lateinit var mBind: FragmentMovieLibraryNavBinding
    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMovieLibraryNavBinding.inflate(inflater, container, false)

    private var dataList: List<Config>? = null

    override fun initView() {
        mBind = baseBinding as FragmentMovieLibraryNavBinding
        viewModel.getMovieLibCategory().observe(this,  {
            val value = viewModel.getMovieLibCategory().value
            initFragment(value?.configs)
        })
    }

    override fun initData() {
        //获取分类接口
        viewModel.movieLibCategory()
    }

    fun setTypeId(typeId: String) {
        if (dataList?.isNotEmpty() == true) {
            for (i in dataList!!.indices) {
                if (dataList!![i].key == typeId) {
                    mBind.vpContainer.currentItem = i
                }
            }
        }
    }

    /**
     * 初始化顶部分类tab
     * @param configs List<Config>?
     */
    private fun initFragment(configs: List<Config>?) {
        val typeId = arguments?.getString(BridgeContext.TYPE_ID)
        var position = 0
        dataList = configs
        val titleList = ArrayList<String>()
        if (configs?.isNotEmpty() == true) {
            for (i in configs.indices) {
                titleList.add(configs[i].name)
                if (typeId == configs[i].key) position = i
            }
            mBind.vpContainer.adapter = MovieLibraryFragmentPagerAdapter(
                childFragmentManager,
                titleList.size,
                configs as ArrayList<Config>
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
            mBind.vpContainer.currentItem = position
        }
    }
}