package com.duoduovv.common.view

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.alibaba.android.arouter.facade.annotation.Route
import com.duoduovv.common.R
import com.duoduovv.common.util.RouterPath
import dc.android.bridge.BridgeContext
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_web_view.*

/**
 * @author: jun.liu
 * @date: 2021/3/16 18:30
 * @des:H5页面
 */
@Route(path = RouterPath.PATH_WEB_VIEW)
class WebViewActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_web_view

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        val title = intent.getStringExtra(BridgeContext.TITLE)
        val loadUrl = intent.getStringExtra(BridgeContext.URL) ?: ""
        layoutTopBar.setTopTitle(title)
        layoutWebView.apply {
            loadUrl(loadUrl)
            settings.javaScriptEnabled = true
            settings.setSupportZoom(false)
            settings.builtInZoomControls = false
            webChromeClient = webChromeClient1
        }
    }

    private val webChromeClient1 = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            progress.progress = newProgress
            progress.visibility = if (newProgress == 100) View.GONE else View.VISIBLE
        }
    }
}