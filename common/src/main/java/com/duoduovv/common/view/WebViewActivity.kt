package com.duoduovv.common.view

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

    override fun initView() {
        val title = intent.getStringExtra(BridgeContext.TITLE)
        val loadUrl = intent.getStringExtra(BridgeContext.URL)?:""
        layoutTopBar.setTopTitle(title)
        layoutWebView.apply {
            loadUrl(loadUrl)
            settings.javaScriptEnabled = true
            settings.setSupportZoom(false)
            settings.builtInZoomControls = false
        }
    }
}