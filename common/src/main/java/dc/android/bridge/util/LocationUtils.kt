package dc.android.bridge.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.*
import android.provider.Settings
import android.util.Log
import com.duoduovv.common.BaseApplication
import dc.android.bridge.domain.LocationBean

/**
 * @author: jun.liu
 * @date: 2021/1/18 18:15
 * @des:定位工具类
 */
class LocationUtils(private val listener: LbsLocationListener?) {
    private lateinit var locationManager: LocationManager
    private var bestProvider: String? = null

    init {
        initLocationManager()
    }

    private fun initLocationManager() {
        locationManager =
            BaseApplication.baseCtx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //没有打开GPS
//            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                context.startActivity(this)
//            }
            listener?.gpsNotOpen()
        }
        getProviders()
    }

    private lateinit var myLocationListener: MyLocationListener
    @SuppressLint("MissingPermission")
    fun startLocation() {
        bestProvider?.let {
            locationManager.requestLocationUpdates(it, 0L, 0F, myLocationListener)
        }
    }

    /**
     * 获取定位的方式
     */
    private fun getProviders() {
        val providers: List<String> = locationManager.getProviders(true)
        Criteria().apply {
            // 查询精度：高，Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精确
            accuracy = Criteria.ACCURACY_FINE
            isAltitudeRequired = true //是否查询海拔
            isBearingRequired = false //是否查询方位角
            isSpeedRequired = false //是否要求速度
            powerRequirement = Criteria.POWER_LOW
            bestProvider = locationManager.getBestProvider(this, true)
            Log.i("address","$bestProvider")
        }
        myLocationListener = MyLocationListener(listener)
    }

    /**
     * 移除定位监听
     */
    fun removeLocation(){
        locationManager.removeUpdates(myLocationListener)
    }

    interface LbsLocationListener {
        fun onLocation(bean: LocationBean)
        fun gpsNotOpen()
    }

    private class MyLocationListener(private val listener: LbsLocationListener?) : LocationListener {
        override fun onLocationChanged(location: Location) {
            val geocode = Geocoder(BaseApplication.baseCtx)
            val address = geocode.getFromLocation(location.latitude, location.longitude, 1)
            for (element in address) {
                val bean = LocationBean(
                    countryCode = element.countryCode,
                    countryName = element.countryName,
                    adminArea = element.adminArea,
                    locality = element.locality,
                    subAdminArea = element.subLocality,
                    featureName = element.featureName,
                    latitude = element.latitude.toString(),
                    longitude = element.longitude.toString()
                )
                listener?.onLocation(bean)
            }
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

    }
}