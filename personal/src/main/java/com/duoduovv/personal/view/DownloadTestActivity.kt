package com.duoduovv.personal.view

import android.os.Environment
import androidx.recyclerview.widget.LinearLayoutManager
import com.duoduovv.common.BaseApplication
import com.duoduovv.download.download.AppDownload
import com.duoduovv.download.download.DownloadInfo
import com.duoduovv.personal.R
import com.duoduovv.personal.adapter.DownloadAdapter
import com.duoduovv.personal.adapter.DownloadBean
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_download_test.*

/**
 * @author: jun.liu
 * @date: 2021/3/23 15:42
 * @des:下载测试页
 */
class DownloadTestActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_download_test
    private var downloadAdapter: DownloadAdapter? = null
    private var path = ""

    override fun initView() {
        path =
            BaseApplication.baseCtx.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath+""
        rvList.layoutManager = LinearLayoutManager(this)
        downloadAdapter = DownloadAdapter(this)
        rvList.adapter = downloadAdapter
        downloadAdapter?.addChildClickViewIds(R.id.tvStart, R.id.tvPause)
        downloadAdapter?.setOnItemChildClickListener { adapter, view, position ->
            val bean = (adapter as DownloadAdapter).data[position]
            val downloadUrl = bean.downloadUrl
            val downloadScope = AppDownload.request(downloadUrl, this,bean,path)
            val downloadInfo = downloadScope?.downloadInfo() ?: return@setOnItemChildClickListener
            when(view.id){
                R.id.tvStart ->{
                    when(downloadInfo.status){
                        DownloadInfo.ERROR,DownloadInfo.PAUSE,DownloadInfo.NONE ->downloadScope.start()
                        DownloadInfo.LOADING -> downloadScope.pause()
                    }
                }
                R.id.tvPause->{

                }
            }
        }
    }

    override fun initData() {
        val data = ArrayList<DownloadBean>()
        val url = "http:\\/\\/down2.okdown10.com\\/20210105\\/2642_e5ede2d1\\/25岁当代单身女性尝试相亲APP的成果日记.EP03.mp4"
        for (i in 0 until 5) {
            //先添加5个任务
            data.add(DownloadBean(url, 0))
        }
        downloadAdapter?.setList(data)
    }
}