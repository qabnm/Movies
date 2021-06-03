package com.duoduovv.common.view

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.duoduovv.common.R
import com.duoduovv.common.databinding.ActivityWebViewBinding
import com.duoduovv.common.util.RouterPath
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import dc.android.bridge.BridgeContext
import dc.android.bridge.view.BridgeActivity

/**
 * @author: jun.liu
 * @date: 2021/3/16 18:30
 * @des:H5页面
 */
@Route(path = RouterPath.PATH_WEB_VIEW)
class WebViewActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_web_view
    private lateinit var mBind: ActivityWebViewBinding

    override fun initView() {
        mBind = ActivityWebViewBinding.bind(layoutView)
        val title = intent.getStringExtra(BridgeContext.TITLE)
        val loadUrl = intent.getStringExtra(BridgeContext.URL) ?: ""
        mBind.layoutTopBar.setTopTitle(title)
        mBind.layoutWebView.apply {
            loadUrl(loadUrl)
            settings.javaScriptEnabled = true
            settings.pluginsEnabled = true
            settings.allowFileAccess = true
            settings.domStorageEnabled = true
            settings.setSupportZoom(false)
            settings.builtInZoomControls = false
            settings.setGeolocationEnabled(false)
            webChromeClient = webChromeClient1
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(webView: WebView, p1: String): Boolean {
                    if (p1.startsWith("http")|| p1.startsWith("https"))webView.loadUrl(p1)
                    return true
                }

                override fun onReceivedSslError(p0: WebView?, p1: SslErrorHandler?, p2: SslError?) {
                    p1?.proceed()
                }
            }

        }
        mBind.layoutTopBar.setBackClickListener { onBackPressed() }
    }

    private val webChromeClient1 = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            mBind.progress.progress = newProgress
            mBind.progress.visibility = if (newProgress == 100) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        QbSdk.clearAllWebViewCache(this, true)
    }

    override fun onBackPressed() {
        if (mBind.layoutWebView.canGoBack()) {
            mBind.layoutWebView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}