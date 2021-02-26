package com.duoduovv.download.download

import android.content.Context
import android.text.TextUtils
import java.io.Serializable
import java.util.concurrent.ConcurrentHashMap

/**
 * @author: jun.liu
 * @date: 2020/12/22 13:51
 * @des:对DownloadScope的管理,同时也是获取DownloadScope的唯一途径
 */
object AppDownload {
    private const val MAX_SCOPE = 3

    //    val downloadFolder: String? by lazy {
//        Environment.getExternalStorageState()
//        Utils.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
//    }
    var downloadFolder: String? = null

    private val scopeMap: ConcurrentHashMap<String, DownloadScope> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        ConcurrentHashMap<String, DownloadScope>()
    }

    private val taskScopeMap: ConcurrentHashMap<String, DownloadScope> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        ConcurrentHashMap<String, DownloadScope>()
    }

    /**
     * 暂停所有的任务
     * 只有任务的状态为[DownloadInfo.WAITING]和[DownloadInfo.LOADING]才可以被暂停
     * 暂停任务会先暂停[DownloadInfo.WAITING]的任务而后再暂停[DownloadInfo.LOADING]的任务
     *
     */
    fun pauseAll() {
        for (entry in scopeMap) {
            val downloadScope = entry.value
            if (downloadScope.isWaiting())
                downloadScope.pause()
        }
        for (entry in scopeMap) {
            val downloadScope = entry.value
            if (downloadScope.isLoading())
                downloadScope.pause()
        }
    }

    /**
     * 移除所有的任务
     * 移除任务会先移除状态不为[DownloadInfo.LOADING]的任务
     * 而后再移除状态为[DownloadInfo.LOADING]的任务
     */
    fun removeAll() {
        for (entry in scopeMap) {
            val downloadScope = entry.value
            if (!downloadScope.isLoading())
                downloadScope.remove()
        }
        for (entry in scopeMap) {
            val downloadScope = entry.value
            if (downloadScope.isLoading())
                downloadScope.pause()
        }
    }

    /**
     * 启动下载任务
     * 请不要直接使用此方法启动下载任务,它是交由[DownloadScope]进行调用
     */
    fun launchScope(scope: DownloadScope) {
        if (taskScopeMap.size >= MAX_SCOPE) return
        if (taskScopeMap.contains(scope.url)) return
        taskScopeMap[scope.url] = scope
        scope.launch()
    }

    /**
     * 启动下一个任务,如果有正在等待中的任务的话
     * 请不要直接使用此方法启动下载任务,它是交由[DownloadScope]进行调用
     * @param previousUrl 上一个下载任务的下载连接
     */
    fun launchNext(previousUrl: String) {
        taskScopeMap.remove(previousUrl)
        for (entrySet in scopeMap) {
            val downloadScope = entrySet.value
            if (downloadScope.isWaiting()) {
                launchScope(downloadScope)
                break
            }
        }
    }

    /**
     * 通过url恢复任务
     *
     * @param urls 需要恢复任务的连接,url请通过DownloadDao进行获取
     */
    fun restore(urls: List<String>,context: Context): MutableList<DownloadScope> {
        val downloadScopes = mutableListOf<DownloadScope>()
        for (url in urls) {
            var downloadScope = scopeMap[url]
            if (downloadScope == null) {
                downloadScope = DownloadScope(url = url,context = context)
                scopeMap[url] = downloadScope
            }
            downloadScopes.add(downloadScope)
        }
        return downloadScopes
    }

    /**
     * 请求一个下载任务[DownloadScope]
     * 这是创建[DownloadScope]的唯一途径,请不要通过其他方式创建[DownloadScope]
     * 首次任务调用此方法获取[DownloadScope]并不会在数据库中生成数据
     * 首次任务只有调用了[DownloadScope.start]并且成功进入[DownloadInfo.WAITING]状态才会在数据库中生成数据
     * 首次任务的判断依据为数据库中是否保留有当前的任务数据
     */
    fun request(url: String?, data: Serializable? = null, path: String? = null,context: Context): DownloadScope? {
        if (TextUtils.isEmpty(url)) return null
        var downloadScope = scopeMap[url]
        if (downloadScope == null) {
            downloadScope = DownloadScope(url = url!!, data = data, path = path,context = context)
            scopeMap[url] = downloadScope
        }
        return downloadScope
    }
}