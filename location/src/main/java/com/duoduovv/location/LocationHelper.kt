package com.duoduovv.location

import android.content.Context
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener

/**
 * @author: jun.liu
 * @date: 2021/4/15 15:52
 * @des:定位功能
 */
class LocationHelper(private val context: Context, private val lbsListener: OnLocationListener?) {
    private var mLocationClient: AMapLocationClient? = null

    /**
     * 开始定位
     */
    fun startLocation(isMock:Boolean) {
        mLocationClient = AMapLocationClient(context)
        mLocationClient?.setLocationListener(locationListener)
        AMapLocationClientOption().apply {
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            isOnceLocation = true
            isNeedAddress = true
            //设置是否允许模拟位置,默认为true，允许模拟位置
            isMockEnable = isMock
            httpTimeOut = 20000
            //当开启定位缓存功能，在高精度模式和低功耗模式下进行的网络定位结果均会生成本地缓存
            isLocationCacheEnable = true
            mLocationClient?.setLocationOption(this)
        }
        mLocationClient?.startLocation()
    }

    /**
     * 异步定位回调监听
     */
    private val locationListener = AMapLocationListener { aMapLocation ->
        aMapLocation?.let {
            if (it.errorCode == 0) {
                lbsListener?.onLocationChange(
                    it.latitude,
                    it.longitude,
                    it.country,
                    it.province,
                    it.city,
                    it.district,
                    it.street,
                    it.aoiName
                )
            } else {
                lbsListener?.onLocationFail()
            }
        } ?: also { lbsListener?.onLocationFail() }
    }

    /**
     * 销毁定位
     */
    fun destroyLocation() {
        //停止定位后，本地定位服务并不会被销毁
//        mLocationClient?.stopLocation()
        //销毁定位客户端，同时销毁本地定位服务。
        mLocationClient?.onDestroy()
    }

    /**
     * 停止定位
     */
    fun stopLocation(){
        mLocationClient?.stopLocation()
    }

    interface OnLocationListener {
        /**
         *  定位成功
         * @param latitude Double  纬度
         * @param longitude Double 经度
         * @param country String   国家
         * @param province String  省
         * @param city String      城市信息
         * @param district String  城区信息
         * @param street String    街道信息
         * @param aioName String   定位点的aio信息
         */
        fun onLocationChange(
            latitude: Double,
            longitude: Double,
            country: String,
            province: String,
            city: String,
            district: String,
            street: String,
            aioName: String
        )

        /**
         * 定位失败
         */
        fun onLocationFail()
    }
}