package com.junliu.cinema.view

import android.Manifest
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.junliu.cinema.R
import com.junliu.cinema.adapter.MainPageAdapter
import com.junliu.cinema.viewmodel.CinemaViewModel
import com.junliu.common.util.RouterPath
import com.junliu.common.util.SharedPreferencesHelper
import com.permissionx.guolindev.PermissionX
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dc.android.bridge.BridgeContext.Companion.ADDRESS
import dc.android.bridge.domain.LocationBean
import dc.android.bridge.util.LocationUtils
import dc.android.bridge.util.StringUtils
import dc.android.bridge.view.BaseViewModelFragment
import kotlinx.android.synthetic.main.fragment_cinema.*

/**
 * @author: jun.liu
 * @date: 2020/12/29 : 14:32
 * 首页
 */
@Route(path = RouterPath.PATH_CINEMA)
class CinemaFragment : BaseViewModelFragment<CinemaViewModel>(), OnRefreshListener,
    OnLoadMoreListener {
    override fun getLayoutId() = R.layout.fragment_cinema
    override fun providerVMClass() = CinemaViewModel::class.java
    private var page = 1
    private var adapter: MainPageAdapter? = null
    private var locationUtils: LocationUtils? = null

    override fun initView() {
        tvSearch.setOnClickListener {
            ARouter.getInstance().build(RouterPath.PATH_SEARCH_ACTIVITY).navigation()
        }
        rvList.layoutManager = GridLayoutManager(requireActivity(), 3)
        refreshLayout.apply {
            setRefreshHeader(ClassicsHeader(requireActivity()))
            setRefreshFooter(ClassicsFooter(requireActivity()))
            setOnRefreshListener(this@CinemaFragment)
            setOnLoadMoreListener(this@CinemaFragment)
        }
        viewModel.getMain().observe(this, Observer {
            val value = viewModel.getMain().value
            if (null == adapter) {
                adapter = MainPageAdapter(requireActivity(), value!!)
                rvList.adapter = adapter
            } else {
                adapter?.notifyDataSetChanged()
            }
        })
        viewModel.getMainRecommend().observe(this, Observer {
            val value = viewModel.getMainRecommend().value
        })
    }

    private fun location() {
        PermissionX.init(this).permissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).onExplainRequestReason { scope, deniedList ->
            val msg = "多多影视需要获取您以下权限"
            scope.showRequestReasonDialog(deniedList, msg, "确定", "取消")
        }.request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                locationUtils = LocationUtils(requireActivity(), LocationListener())
                locationUtils?.startLocation()
            } else {

            }
        }
    }

    override fun initData() {
        location()
        Log.i("address", SharedPreferencesHelper.helper.getValue(ADDRESS, "") as String)
//        viewModel.hah(page = page)
//        viewModel.configure()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        viewModel.main(page)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page++
        viewModel.mainRecommend(page)
    }

    private inner class LocationListener : LocationUtils.LbsLocationListener {
        override fun onLocation(bean: LocationBean) {
            Log.i(
                "address",
                "${bean.countryName},${bean.adminArea},${bean.locality},${bean.subAdminArea},${bean.featureName}"
            )
            locationUtils?.removeLocation()
            //将定位信息保存到本地
            SharedPreferencesHelper.helper.setValue(
                ADDRESS,
                "{\"p\":\"${StringUtils.gbEncoding(bean.adminArea)}\",\"c\":\"${StringUtils.gbEncoding(
                    bean.locality
                )}\",\"d\":\"${StringUtils.gbEncoding(bean.subAdminArea)}\"}"
            )
        }
    }
}