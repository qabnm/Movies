package com.duoduovv.cinema.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.advert.gdtad.GDTInsertAd
import com.duoduovv.advert.ttad.TTInsertAd
import com.duoduovv.cinema.R
import com.duoduovv.cinema.databinding.FragmentCinemaBinding
import com.duoduovv.cinema.viewmodel.CinemaViewModel
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.adapter.ScaleTitleNavAdapter
import com.duoduovv.common.adapter.ViewPagerAdapter
import com.duoduovv.common.component.UpgradeDialogFragment
import com.duoduovv.common.domain.AdValue
import com.duoduovv.common.domain.Column
import com.duoduovv.common.domain.ConfigureBean
import com.duoduovv.common.domain.Version
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.SharedPreferencesHelper
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.ADDRESS
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.TYPE_GDT_AD
import dc.android.bridge.BridgeContext.Companion.TYPE_TT_AD
import dc.android.bridge.BridgeContext.Companion.WAY_VERIFY
import dc.android.bridge.util.OsUtils
import dc.android.bridge.util.StringUtils
import dc.android.bridge.view.BaseViewModelFragment
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
    private var hotList: List<String>? = null
    private var upgradeDialogFragment: UpgradeDialogFragment? = null
    private var bean: Version? = null
    private lateinit var mBind: FragmentCinemaBinding
    private var configureBean: ConfigureBean? = null

    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCinemaBinding.inflate(inflater, container, false)

    override fun initView() {
        mBind = baseBinding as FragmentCinemaBinding
        val layoutParams = mBind.vStatusBar.layoutParams as LinearLayout.LayoutParams
        layoutParams.height = OsUtils.getStatusBarHeight(requireActivity())
        mBind.tvSearch.setOnClickListener {
            ARouter.getInstance().build(RouterPath.PATH_SEARCH_ACTIVITY)
                .withStringArrayList(BridgeContext.LIST, hotList as? ArrayList).navigation()
        }
        viewModel.getConfigure().observe(this, {
            val result = viewModel.getConfigure().value
            initConfig(result)
        })
        viewModel.getProgress().observe(this, {
            val progress = viewModel.getProgress().value
            upgradeDialogFragment?.onProgressUpdate(progress ?: 0)
        })
        viewModel.getInstallState().observe(this, {
            val intent = viewModel.getInstallState().value
            intent?.let { startActivity(it) }
            requireActivity().finish()
        })
        mBind.imgHistory.setOnClickListener {
            ARouter.getInstance().build(RouterPath.PATH_WATCH_HISTORY).navigation()
        }
        configureBean = BaseApplication.configBean
    }

    /**
     *
     * @param configureBean ConfigureBean?
     */
    private fun initConfig(configureBean: ConfigureBean?) {
        configureBean?.let { it ->
            val columns = it.columns
            BaseApplication.configBean = it
            initFragment(columns)
            SharedPreferencesHelper.helper.setValue(BridgeContext.WAY, it.way)
            mBind.imgHistory.visibility =
                if (it.way == WAY_VERIFY) View.INVISIBLE else View.VISIBLE
            hotList = it.hotSearch
            BaseApplication.hotList = hotList
            this.bean = it.version
            checkUpdate(it.version)
            //展示插屏广告
            val currentDay = SharedPreferencesHelper.helper.getValue("currentTime","")
            if (currentDay != StringUtils.getCurrentDay()){
                it.ad?.let { initInsertAd(it.insertAd) }
            }
        }
    }

    /**
     * 插屏广告
     * @param ad AdValue?
     */
    private fun initInsertAd(ad: AdValue?) {
        SharedPreferencesHelper.helper.setValue("currentTime",StringUtils.getCurrentDay())
        when (ad?.type) {
            TYPE_TT_AD -> {
                val ttAd = TTInsertAd()
                ttAd.initInsertAd(requireActivity(),ad.value,300f,450f)
            }
            TYPE_GDT_AD -> {
                val gdtAd = GDTInsertAd()
                gdtAd.initInsertAd(
                    requireActivity(),
                    ad.value
                )
            }
        }
    }

    /**
     * 检查升级
     * @param bean Version?
     */
    private fun checkUpdate(bean: Version?) {
        bean?.let {
            val versionCode = OsUtils.getVerCode(requireContext())
            if (versionCode != -1 && it.versionNum > versionCode) {
                //需要升级  弹出升级框
                upgradeDialogFragment = UpgradeDialogFragment(it.isForce, it.content, it.url)
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
            viewModel.downloadApk(url)
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
        mBind.vpContainer.adapter = ViewPagerAdapter(childFragmentManager, data = fragmentList)
        CommonNavigator(requireActivity()).apply {
            adapter = ScaleTitleNavAdapter(mBind.vpContainer, titleList)
            isAdjustMode = titleList.size <= 5
            mBind.indicator.navigator = this
        }
        ViewPagerHelper.bind(mBind.indicator, mBind.vpContainer)
    }

    override fun initData() {
        Log.i("address", "${SharedPreferencesHelper.helper.getValue(ADDRESS, "")}")
        configureBean?.let {
            initConfig(it)
        } ?: run { viewModel.configure() }
    }
}