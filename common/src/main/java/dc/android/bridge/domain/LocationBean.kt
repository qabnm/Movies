package dc.android.bridge.domain

import androidx.annotation.Keep

/**
 * @author: jun.liu
 * @date: 2021/1/19 9:42
 * @des:
 */
@Keep
data class LocationBean(
    val countryCode: String?,
    val countryName: String?,
    val adminArea: String?,
    val locality: String?,
    val subAdminArea: String?,
    val featureName: String?,
    val latitude: String?,
    val longitude: String?
)