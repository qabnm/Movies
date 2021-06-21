package com.duoduovv.common.viewmodel

import androidx.lifecycle.MutableLiveData
import com.duoduovv.advert.AdvertBridge
import com.duoduovv.common.domain.ConfigureBean
import dc.android.bridge.BridgeContext
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/6/21 9:43
 * @des:
 */
open class ConfigureViewModel:BaseViewModel() {
    private var configure: MutableLiveData<ConfigureBean> = MutableLiveData()
    fun getConfigure() = configure
    private val repository = PubRepository()

    /**
     * 首页配置
     * @return Job
     */
    fun configure() = request {
        val result = repository.configure()
        if (result.code == BridgeContext.SUCCESS) {
            //获取广告位信息
            val bean = result.data
            AdvertBridge.AD_TYPE = bean.adType
            val ttBean = bean.ttAd
            val gdtBean = bean.gdtAd
            if (AdvertBridge.TT_AD == bean.adType) {
                //这是穿山甲的广告
                AdvertBridge.SPLASH = ttBean?.splash ?: ""
                AdvertBridge.LOGOUT = ttBean?.logout ?: ""
                AdvertBridge.MAIN_PAGE_BANNER = ttBean?.mainPageBanner ?: ""
                AdvertBridge.SEARCH = ttBean?.search ?: ""
                AdvertBridge.MOVIE_DETAIL_BANNER = ttBean?.movieDetailBanner ?: ""
                AdvertBridge.CENTER_TOP = ttBean?.centerTop ?: ""
                AdvertBridge.VIDEO_AD = ttBean?.videoAd ?:""
                //3011896776111084
            } else {
                //这是广点通的广告
                AdvertBridge.SPLASH = gdtBean?.splash ?: ""
                AdvertBridge.LOGOUT = gdtBean?.logout ?: ""
                AdvertBridge.MAIN_PAGE_BANNER = gdtBean?.mainPageBanner ?: ""
                AdvertBridge.SEARCH = gdtBean?.search ?: ""
                AdvertBridge.MOVIE_DETAIL_BANNER = gdtBean?.movieDetailBanner ?: ""
                AdvertBridge.CENTER_TOP = gdtBean?.centerTop ?: ""
            }
            configure.postValue(result.data)
        }
    }
}