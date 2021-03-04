package com.duoduovv.cinema.viewmodel

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import com.duoduovv.cinema.bean.ConfigureBean
import com.duoduovv.cinema.repository.CinemaRepository
import com.duoduovv.common.util.FileUtils
import com.duoduovv.common.util.InstallFileProvider
import dc.android.bridge.BridgeContext
import dc.android.bridge.net.BaseRepository
import dc.android.bridge.net.BaseResponseData
import dc.android.bridge.net.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

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
    private lateinit var appContext: Activity

    /**
     * 首页配置
     * @return Job
     */
    fun configure() = request {
        val result = repository.configure()
        if (result.code == BridgeContext.SUCCESS) configure.postValue(result)
    }

    /**
     * apk下载
     * @param url String
     * @return Job
     */
    fun downloadApk(url: String, context: Activity) = request {
        appContext = context
        filePath =
            appContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath + "/duoduovv"
        val responseBody = repository.downloadFile(url)
        totalSize = responseBody.contentLength()
        filePath?.let { downloadFile(responseBody.byteStream(), it) }
    }

    private suspend fun downloadFile(inputStream: InputStream?, filePath: String) =
        withContext(Dispatchers.IO) {
            val file = File(filePath)
            var os: FileOutputStream? = null
            try {
                os = FileOutputStream(filePath)
                if (file.exists()) file.delete()
                // 1K的数据缓冲
                val bs = ByteArray(1024)
                var len = 0
                var downloadSize = 0
                // 开始读取
                while (inputStream?.read(bs).also { if (it != null) len = it } != -1) {
                    os?.write(bs, 0, len)
                    downloadSize += len
                    val progress = (downloadSize / (totalSize * 1.0)) * 100
                    downloadProgress.postValue(progress.toInt())
                }
                //下载成功
                withContext(Dispatchers.Main) { installFile(filePath) }
            } catch (e: Exception) {
                error.postValue(BaseRepository.ParameterException("下载错误"))
            } finally {
                inputStream?.close()
                os?.close()
            }
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
                val contentUri = InstallFileProvider.getUriForFile(appContext, "com.junliu.install.fileProvider", file)
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
            }
            appContext.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}