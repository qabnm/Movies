package dc.android.bridge.util

import android.content.Context
import android.content.pm.PackageManager
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.R

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

        fun toast(str: String?, context: Context) {
            val customToast = Toast(BaseApplication.baseCtx)
            val layoutView =
                LayoutInflater.from(context).inflate(R.layout.layout_custom_toast, null)
            val tvContent: TextView = layoutView.findViewById(R.id.tvContent)
            tvContent.text = str
            customToast.apply {
                view = layoutView
                duration = Toast.LENGTH_SHORT
                setGravity(Gravity.CENTER, 0, 0)
                str?.let { this.show() }
            }
        }
    }
}