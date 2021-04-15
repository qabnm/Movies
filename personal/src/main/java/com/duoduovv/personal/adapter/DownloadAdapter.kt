package com.duoduovv.personal.adapter

import android.os.Environment
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.duoduovv.common.BaseApplication
import com.duoduovv.download.download.AppDownload
import com.duoduovv.download.download.DownloadInfo
import com.duoduovv.personal.R

/**
 * @author: jun.liu
 * @date: 2021/3/24 17:04
 * @des:
 */
class DownloadAdapter(private val lifecycleOwner: LifecycleOwner) :BaseQuickAdapter<DownloadBean,DownloadViewHolder>(R.layout.item_download_test) {
   private val path =
    BaseApplication.baseCtx.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath!!
    override fun convert(holder: DownloadViewHolder, item: DownloadBean) {
        holder.tag = item.downloadUrl
        val layoutProgress:ProgressBar = holder.getView(R.id.layoutProgress)
        val tvStart:TextView = holder.getView(R.id.tvStart)
        val tvPause:TextView = holder.getView(R.id.tvPause)
        val tvClear:TextView = holder.getView(R.id.tvClear)
        val downloadScope = AppDownload.request(item.downloadUrl,context,data = item,path)
        downloadScope?.observer(lifecycleOwner, DownloadObserver(item.downloadUrl,holder))
        val downloadInfo = downloadScope?.downloadInfo()
        downloadInfo?.let {
            val progressPercent = (it.currentLength/(it.contentLength*1.0)).toInt()
            layoutProgress.progress = progressPercent
            when(it.status){
                DownloadInfo.LOADING ->{
                    layoutProgress.progress = progressPercent
                }
            }
        }
    }

    inner class DownloadObserver(private val tag: Any, private val holder: DownloadViewHolder) : Observer<DownloadInfo> {
        override fun onChanged(t: DownloadInfo?) {
            if (tag == holder.tag) {
                try {
                    notifyItemChanged(holder.adapterPosition)
                } catch (e: Exception) {
                }
            }
        }
    }
}