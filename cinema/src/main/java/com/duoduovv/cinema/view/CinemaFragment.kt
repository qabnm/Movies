package com.duoduovv.cinema.view

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.cinema.R
import com.duoduovv.cinema.bean.Column
import com.duoduovv.cinema.bean.ConfigureBean
import com.duoduovv.cinema.bean.Version
import com.duoduovv.cinema.component.UpgradeDialogFragment
import com.duoduovv.cinema.viewmodel.CinemaViewModel
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.adapter.ScaleTitleNavAdapter
import com.duoduovv.common.adapter.ViewPagerAdapter
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.SharedPreferencesHelper
import com.permissionx.guolindev.PermissionX
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.ADDRESS
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.domain.LocationBean
import dc.android.bridge.util.LocationUtils
import dc.android.bridge.util.OsUtils
import dc.android.bridge.util.StringUtils
import dc.android.bridge.view.BaseViewModelFragment
import kotlinx.android.synthetic.main.fragment_cinema.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * @author: jun.liu
 * @date: 2020/12/29 : 14:32
 * 首页
 */
@Route(path = RouterPath.PATH_CINEMA)
class CinemaFragment : BaseViewModelFragment<CinemaViewModel>() {
    override fun getLayoutId() = R.layout.fragment_cinema
    override fun providerVMClass() = CinemaViewModel::class.java
    private var locationUtils: LocationUtils? = null
    private var hotList: List<String>? = null
    private var upgradeDialogFragment: UpgradeDialogFragment? = null

    override fun initView() {
        tvSearch.setOnClickListener {
            ARouter.getInstance().build(RouterPath.PATH_SEARCH_ACTIVITY)
                .withStringArrayList(BridgeContext.LIST, hotList as? ArrayList).navigation()
        }
        viewModel.getConfigure().observe(this, {
            val result = viewModel.getConfigure().value?.data
            val columns = result?.columns
            initFragment(columns)
            SharedPreferencesHelper.helper.setValue(BridgeContext.isRes, result?.isRs ?: 0)
            hotList = result?.hotSearch
            BaseApplication.hotList = hotList
            checkUpdate(result?.version)
        })
        viewModel.getProgress().observe(this, {
            val progress = viewModel.getProgress().value
            upgradeDialogFragment?.onProgressUpdate(progress ?: 0)
        })
    }

    private fun checkUpdate(bean: Version?) {
        bean?.let {
            val versionCode = OsUtils.getVerCode(requireContext())
            if (versionCode != -1 && it.version_number > versionCode) {
                //需要升级  弹出升级框
                upgradeDialogFragment = UpgradeDialogFragment(it)
                upgradeDialogFragment?.showNow(childFragmentManager, "upgrade")
                upgradeDialogFragment?.setOnUpgradeClickListener(upgradeListener)
            }
        }
    }

    /**
     * 点击了升级
     */
    private val upgradeListener = object : UpgradeDialogFragment.OnUpgradeClickListener {
        override fun onUpgradeClick(url: String) {
            viewModel.downloadApk(url, requireActivity())
        }
    }

    /**
     * 创建首页fragment
     * @param columns List<Column>?
     */
    private fun initFragment(columns: List<Column>?) {
        if (columns?.isEmpty() == true) return
        val titleList = ArrayList<String>()
        val fragmentList = ArrayList<Fragment>()
        for (i in columns!!.indices) {
            val fragment = CinemaListFragment()
            val bundle = Bundle()
            bundle.putString(ID, columns[i].id)
            fragment.arguments = bundle
            fragmentList.add(fragment)
            titleList.add(columns[i].name)
        }
        vpContainer.adapter = ViewPagerAdapter(childFragmentManager, data = fragmentList)
        CommonNavigator(requireActivity()).apply {
            adapter = ScaleTitleNavAdapter(vpContainer, titleList)
            isAdjustMode = titleList.size <= 6
            indicator.navigator = this
        }
        ViewPagerHelper.bind(indicator, vpContainer)
    }

    private fun location() {
        PermissionX.init(this).permissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).onExplainRequestReason { scope, deniedList ->
            val msg = "多多影视需要获取您以下权限"
            scope.showRequestReasonDialog(deniedList, msg, "确定", "取消")
        }.request { allGranted, _, _ ->
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
        viewModel.configure()
    }

    private inner class LocationListener : LocationUtils.LbsLocationListener {
        override fun onLocation(bean: LocationBean) {
            locationUtils?.removeLocation()
            //将定位信息保存到本地
            SharedPreferencesHelper.helper.setValue(
                ADDRESS,
                "{\"p\":\"${StringUtils.gbEncoding(bean.adminArea)}\",\"c\":\"${
                    StringUtils.gbEncoding(
                        bean.locality
                    )
                }\",\"d\":\"${StringUtils.gbEncoding(bean.subAdminArea)}\"}"
            )
        }
    }
}