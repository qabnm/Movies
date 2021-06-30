package com.duoduovv.movie.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.adapter.NoLineIndicatorAdapter
import com.duoduovv.common.adapter.ViewPagerAdapter
import com.duoduovv.common.util.RouterPath
import com.duoduovv.movie.R
import com.duoduovv.movie.databinding.FragmentMovieBinding
import dc.android.bridge.BridgeContext
import dc.android.bridge.util.OsUtils
import dc.android.bridge.view.BaseFragment
import dc.android.tools.LiveDataBus
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * @author: jun.liu
 * @date: 2020/12/29 : 14:37
 * 影片专题、片库、榜单
 */
@Route(path = RouterPath.PATH_MOVIE)
class MovieFragment : BaseFragment() {
    private lateinit var mBind: FragmentMovieBinding
    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMovieBinding.inflate(inflater, container, false)

    override fun getLayoutId() = R.layout.fragment_movie
    private var typeId: String? = null
    private var libFragment: MovieLibraryNavFragment? = null

    override fun initView() {
        mBind = baseBinding as FragmentMovieBinding
        val layoutParams = mBind.vStatusBar.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.height = OsUtils.getStatusBarHeight(requireActivity())
        mBind.imgSearch.setOnClickListener {
            ARouter.getInstance().build(RouterPath.PATH_SEARCH_ACTIVITY)
                .withStringArrayList(BridgeContext.LIST, BaseApplication.hotList as? ArrayList)
                .navigation()
        }
        LiveDataBus.get().with(BridgeContext.ID, String::class.java).observe(this, {
            typeId = it
            Log.i("typeId", "我已经接受到typeId了，$typeId")
            if (null != mBind.vpContainer.adapter) {
                mBind.vpContainer.currentItem = 0
                libFragment?.setTypeId(it)
            }
        })
    }

    override fun initData() {
        typeId = arguments?.getString(BridgeContext.TYPE_ID)
        val data = listOf("专题","片库", "榜单")
        val fragmentList = ArrayList<Fragment>()
        val subjectFragment = SubjectFragment()
        fragmentList.add(subjectFragment)

        libFragment = MovieLibraryNavFragment()
        val bundle = Bundle()
        bundle.putString(BridgeContext.TYPE_ID, typeId)
        libFragment!!.arguments = bundle
        fragmentList.add(libFragment!!)
        Log.i("typeId", "初始化先执行了，$typeId")

        val rankNavFragment = MovieRankNavFragment()
        fragmentList.add(rankNavFragment)
        mBind.vpContainer.adapter = ViewPagerAdapter(childFragmentManager, fragmentList)
        CommonNavigator(requireActivity()).apply {
            adapter = NoLineIndicatorAdapter(
                viewPager = mBind.vpContainer,
                data = data,
                unSelectColor = R.color.color666666,
                selectColor = R.color.color000000,
                unSelectSize = R.dimen.sp_15,
                selectSize = R.dimen.sp_20
            )
            isAdjustMode = false
            mBind.indicator.navigator = this
        }
        ViewPagerHelper.bind(mBind.indicator, mBind.vpContainer)
        mBind.vpContainer.currentItem = 1
    }
}