package com.duoduovv.movie.view

import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.duoduovv.common.util.RouterPath
import com.duoduovv.movie.R
import com.duoduovv.movie.viewmodel.ReportViewModel
import dc.android.bridge.BridgeContext
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.view.BaseViewModelActivity
import kotlinx.android.synthetic.main.activity_report.*

/**
 * @author: jun.liu
 * @date: 2021/3/31 14:15
 * @des:举报页面
 */
@Route(path = RouterPath.PATH_REPORT)
class ReportActivity : BaseViewModelActivity<ReportViewModel>() {
    override fun getLayoutId() = R.layout.activity_report
    override fun providerVMClass() = ReportViewModel::class.java
    private var title = "其它"
    private var movieId = ""
    private var normalColor = 0
    private var selectColor = 0

    override fun initView() {
        normalColor = ContextCompat.getColor(this, R.color.color999999)
        selectColor = ContextCompat.getColor(this, R.color.colorFFFFFF)
        viewModel.getReport().observe(this, {
            AndroidUtils.toast("举报成功，我们会尽快处理！", this)
            finish()
        })
        btnSeQing.setOnCheckedChangeListener { _, isChecked ->
            btnSeQing.setTextColor(if (isChecked) selectColor else normalColor)
            if (isChecked) title = "色情"
        }
        btnBaoLi.setOnCheckedChangeListener { _, isChecked ->
            btnBaoLi.setTextColor(if (isChecked) selectColor else normalColor)
            if (isChecked) title = "暴力"
        }
        btnQinQuan.setOnCheckedChangeListener { _, isChecked ->
            btnQinQuan.setTextColor(if (isChecked) selectColor else normalColor)
            if (isChecked) title = "侵权"
        }
        btnOther.setOnCheckedChangeListener { _, isChecked ->
            btnOther.setTextColor(if (isChecked) selectColor else normalColor)
            if (isChecked) title = "其它"
        }
        btnCommit.setOnClickListener { commit() }
    }

    override fun initData() {
        movieId = intent.getStringExtra(BridgeContext.ID) ?: ""
    }

    private fun commit() {
        if (TextUtils.isEmpty(etContent.text)) {
            AndroidUtils.toast("请输入举报内容", this)
            return
        }
        viewModel.report("$title：${etContent.text}", movieId)
    }
}