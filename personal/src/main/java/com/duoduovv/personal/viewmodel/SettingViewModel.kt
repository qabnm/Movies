package com.duoduovv.personal.viewmodel

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.util.FileUtils
import com.duoduovv.common.util.InstallFileProvider
import com.duoduovv.personal.bean.VersionBean
import com.duoduovv.personal.repository.PersonRepository
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.net.BaseRepository
import dc.android.bridge.net.BaseViewModel
import java.io.File

/**
 * @author: jun.liu
 * @date: 2021/3/22 18:30
 * @des:設置
 */
class SettingViewModel :BaseViewModel() {
    private var upgrade:MutableLiveData<VersionBean> = MutableLiveData()
    fun getUpgrade() = upgrade
    private var totalSize: Long = 0
    private var filePath: String? = null
    private var downloadProgress: MutableLiveData<Int> = MutableLiveData()
    fun getProgress() = downloadProgress
    private var installState: MutableLiveData<Intent> = MutableLiveData()
    fun getInstallState() = installState
    private val repository = PersonRepository()

    /**
     * 檢查升級
     * @return Job
     */
    fun upgrade() = request {
        val result = repository.upgrade()
        if (result.code == SUCCESS) upgrade.postValue(result.data.version)
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
            error.postValue(BaseRepository.ParameterException("下载错误"))
        }
    }
}