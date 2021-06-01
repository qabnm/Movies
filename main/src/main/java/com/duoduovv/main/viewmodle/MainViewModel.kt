package com.duoduovv.main.viewmodle

import androidx.lifecycle.MutableLiveData
import com.duoduovv.advert.AdvertBridge
import com.duoduovv.common.domain.ConfigureBean
import com.duoduovv.main.repository.MainRepository
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseResponseData
import dc.android.bridge.net.BaseViewModel

/**
 * @author: jun.liu
 * @date: 2021/5/28 18:31
 * @des:
 */
class MainViewModel : BaseViewModel() {
    private var configure: MutableLiveData<BaseResponseData<ConfigureBean>> = MutableLiveData()
    fun getConfigure() = configure
    private val repository = MainRepository()

    /**
     * 首页配置
     * @return Job
     */
    fun configure() = request {
        val result = repository.configure()
        if (result.code == SUCCESS) {
            //获取广告位信息
            val bean = result.data
            AdvertBridge.AD_TYPE = bean.adType
            val ttBean = bean.ttAd
            val gdtBean = bean.gdtAd
            if ("ttAd" == bean.adType) {
                //这是穿山甲的广告
                AdvertBridge.SPLASH = ttBean?.splash ?: ""
                AdvertBridge.LOGOUT = ttBean?.logout ?: ""
                AdvertBridge.MAIN_PAGE_BANNER = ttBean?.mainPageBanner ?: ""
                AdvertBridge.SEARCH = ttBean?.search ?: ""
                AdvertBridge.MOVIE_DETAIL_BANNER = ttBean?.movieDetailBanner ?: ""
                AdvertBridge.CENTER_TOP = ttBean?.centerTop ?: ""
            } else {
                //这是广点通的广告
                AdvertBridge.SPLASH = gdtBean?.splash ?: ""
                AdvertBridge.LOGOUT = gdtBean?.logout ?: ""
                AdvertBridge.MAIN_PAGE_BANNER = gdtBean?.mainPageBanner ?: ""
                AdvertBridge.SEARCH = gdtBean?.search ?: ""
                AdvertBridge.MOVIE_DETAIL_BANNER = gdtBean?.movieDetailBanner ?: ""
                AdvertBridge.CENTER_TOP = gdtBean?.centerTop ?: ""
            }
            configure.postValue(result)
        }
    }
}