package dc.android.bridge.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.provider.Settings
import com.junliu.common.BaseApplication

/**
 * @author: jun.liu
 * @date: 2021/1/18 18:15
 * @des:定位工具类
 */
class LocationUtils(private val context: Context) {
    private lateinit var locationManager:LocationManager
    private var bestProvider:String?=null
    init {
        initLocationManager()
    }

    private fun initLocationManager() {
        locationManager = BaseApplication.baseCtx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //没有打开GPS
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(this)
            }
        }
        getProviders()
    }

    /**
     * 获取定位的方式
     */
    private fun getProviders() {
        val providers:List<String> = locationManager.getProviders(true)
        Criteria().apply {
            // 查询精度：高，Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精确
            accuracy = Criteria.ACCURACY_FINE
            isAltitudeRequired = false //是否查询海拔
            isBearingRequired = false //是否查询方位角
            isSpeedRequired = false //是否要求速度
            bestProvider = locationManager.getBestProvider(this , false)
        }
    }

    private class MyLocationListener : LocationListener{
        override fun onLocationChanged(location: Location) {
            TODO("Not yet implemented")
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

    }
}