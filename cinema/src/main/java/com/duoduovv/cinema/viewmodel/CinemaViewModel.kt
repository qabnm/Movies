package com.duoduovv.cinema.viewmodel

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import com.duoduovv.advert.AdvertBridge
import com.duoduovv.cinema.repository.CinemaRepository
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.domain.ConfigureBean
import com.duoduovv.common.util.FileUtils
import com.duoduovv.common.util.InstallFileProvider
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseRepository
import dc.android.bridge.net.BaseResponseData
import dc.android.bridge.net.BaseViewModel
import java.io.File

/**
 * @author: jun.liu
 * @date: 2021/1/19 17:41
 * @des:
 */
class CinemaViewModel : BaseViewModel() {
    private var configure: MutableLiveData<BaseResponseData<ConfigureBean>> = MutableLiveData()
    fun getConfigure() = configure
    private val repository = CinemaRepository()
    private var totalSize: Long = 0
    private var filePath: String? = null
    private var downloadProgress: MutableLiveData<Int> = MutableLiveData()
    fun getProgress() = downloadProgress
    private var installState: MutableLiveData<Intent> = MutableLiveData()
    fun getInstallState() = installState

    /**
     * 首页配置
     * @return Job
     */
    fun configure() = request {
        val result = repository.configure()
        if (result.code == SUCCESS) {
            val bean = result.data
            AdvertBridge.AD_TYPE = bean.adType
            val ttBean = bean.ttAd
            val gdtBean = bean.gdtAd
            if ("ttAd" == bean.adType){
                //这是穿山甲的广告
                AdvertBridge.SPLASH = ttBean?.splash?:""
                AdvertBridge.LOGOUT = ttBean?.logout?:""
                AdvertBridge.MAIN_PAGE_BANNER = ttBean?.mainPageBanner?:""
                AdvertBridge.SEARCH = ttBean?.search?:""
                AdvertBridge.MOVIE_DETAIL_BANNER = ttBean?.movieDetailBanner?:""
            }else{
                //这是广点通的广告
                AdvertBridge.SPLASH = gdtBean?.splash?:""
                AdvertBridge.LOGOUT = gdtBean?.logout?:""
                AdvertBridge.MAIN_PAGE_BANNER = gdtBean?.mainPageBanner?:""
                AdvertBridge.SEARCH = gdtBean?.search?:""
                AdvertBridge.MOVIE_DETAIL_BANNER = gdtBean?.movieDetailBanner?:""
            }
            configure.postValue(result)
        }
    }

    /**
     * apk下载
     * @param url String
     * @return Job
     */
    fun downloadApk(url: String) = request {
        filePath =
            BaseApplication.baseCtx.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath + "/duoduovv.apk"
        val responseBody = repository.downloadFile(url)
        totalSize = responseBody.contentLength()
        Thread { FileUtils.is2File(responseBody.byteStream(), filePath, cbFile) }.start()
    }

    /**
     * 安装apk文件
     * @param filePath String
     */
    private fun installFile(filePath: String) {
        try {
            val file = File(filePath)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val contentUri = InstallFileProvider.getUriForFile(
                    BaseApplication.baseCtx,
                    "com.junliu.install.fileProvider", file
                )
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
            }
            installState.postValue(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val cbFile = object : FileUtils.Callback {
        override fun doSuss(filePath: String) {
            installFile(filePath)
        }

        override fun doSchedule(downloadSize: Long) {
            val progress = (downloadSize / (totalSize * 1.0)) * 100
            downloadProgress.postValue(progress.toInt())
        }

        override fun err(str: String) {
            getException().postValue(BaseRepository.ParameterException("下载错误"))
        }
    }
}