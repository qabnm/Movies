package com.junliu.cinema.view

import com.bumptech.glide.Glide
import com.junliu.cinema.R
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity :BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_test

    override fun initData() {
        val url = "https://img9.doubanio.com/view/photo/l/public/p2605901615.webp"
        Glide.with(this).load(url).into(imgSrc)
    }
}