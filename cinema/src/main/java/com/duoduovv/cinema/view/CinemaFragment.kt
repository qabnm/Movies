package com.duoduovv.cinema.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.advert.gdtad.GDTInsertAd
import com.duoduovv.advert.ttad.TTInsertAd
import com.duoduovv.cinema.R
import com.duoduovv.cinema.component.CinemaFragmentViewPagerAdapter
import com.duoduovv.cinema.component.SnackBarView
import com.duoduovv.cinema.databinding.FragmentCinemaBinding
import com.duoduovv.cinema.viewmodel.CinemaViewModel
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.adapter.ScaleTitleNavAdapter
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
    override fun providerVMClass() = CinemaViewModel::class.java
    private var hotList: List<String>? = null
    private var upgradeDialogFragment: UpgradeDialogFragment? = null
    private var bean: Version? = null
    private lateinit var mBind: FragmentCinemaBinding
    private var configureBean: ConfigureBean? = null
    private var isFirstShowRecord = true
    private val idList = ArrayList<String>()

    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCinemaBinding.inflate(inflater, container, false)

    override fun initView() {
        mBind = baseBinding as FragmentCinemaBinding
        val layoutParams = mBind.vStatusBar.layoutParams as ConstraintLayout.LayoutParams
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
            intent?.let {
                startActivity(it)
                requireActivity().finish()
            }
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
            val currentDay = SharedPreferencesHelper.helper.getValue("currentTime", "")
            if (currentDay != StringUtils.getCurrentDay()) {
                it.ad?.insertAd?.let { initInsertAd(it) }
            }
        }
    }

    /**
     * 插屏广告
     * @param ad AdValue?
     */
    private fun initInsertAd(ad: AdValue) {
        SharedPreferencesHelper.helper.setValue("currentTime", StringUtils.getCurrentDay())
        when (ad.type) {
            TYPE_TT_AD -> {
                val ttAd = TTInsertAd()
                ttAd.initInsertAd(requireActivity(), ad.value, 300f, 450f)
            }
            TYPE_GDT_AD -> {
                val gdtAd = GDTInsertAd()
                gdtAd.initInsertAd(requireActivity(), ad.value)
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
        for (i in columns!!.indices) {
            idList.add(columns[i].id)
            titleList.add(columns[i].name)
        }
        mBind.vpContainer.adapter =
            CinemaFragmentViewPagerAdapter(childFragmentManager, idList.size, columns as ArrayList<Column>)
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

    /**
     * 显示上次播放记录
     */
    fun showRecord() {
        if (isFirstShowRecord) {
            isFirstShowRecord = false
            val way = SharedPreferencesHelper.helper.getValue(BridgeContext.WAY, "") as String
            if (way == BridgeContext.WAY_RELEASE) {
                val snackBarView = SnackBarView()
                snackBarView.setSnackClick(object : SnackBarView.OnSnackClickListener {
                    override fun onSnackClick(movieId: String) {
                        onMovieClick(movieId)
                    }
                })
                snackBarView.initSnack(this.view!!.findViewById(R.id.coordinator), requireContext())
            }
        }
    }

    private fun onMovieClick(movieId: String) {
        ARouter.getInstance().build(RouterPath.PATH_MOVIE_DETAIL).withString(ID, movieId)
            .navigation()
    }
}