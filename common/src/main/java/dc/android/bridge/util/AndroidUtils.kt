package dc.android.bridge.util

import android.content.pm.PackageManager
import com.duoduovv.common.BaseApplication

/**
 * @author: jun.liu
 * @date: 2021/3/23 18:36
 * @des:
 */
class AndroidUtils {
    companion object {
        /**
         * 获取app渠道标识
         * @return String?
         */
        fun getAppMetaData(): String? {
            val applicationInfo = BaseApplication.baseCtx.packageManager.getApplicationInfo(
                BaseApplication.baseCtx.packageName,
                PackageManager.GET_META_DATA
            )
            if (null != applicationInfo.metaData) {
                return applicationInfo.metaData.getString("UMENG_CHANNEL")
            }
            return null
        }
    }
}