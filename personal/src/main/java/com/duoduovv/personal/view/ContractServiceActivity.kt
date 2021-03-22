package com.duoduovv.personal.view

import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.alibaba.android.arouter.facade.annotation.Route
import com.duoduovv.common.util.RouterPath.Companion.PATH_CONTRACT_SERVICE_ACTIVITY
import com.duoduovv.personal.R
import dc.android.bridge.util.LoggerSnack
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_contract_service.*

/**
 * @author: jun.liu
 * @date: 2020/12/31 : 10:23
 * 联系客服
 *
 */
@Route(path = PATH_CONTRACT_SERVICE_ACTIVITY)
class ContractServiceActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_contract_service

    override fun initData() {
        etSuggest.addTextChangedListener(TextChangeListener())
        btnCommit.setOnClickListener {
            if (TextUtils.isEmpty(etSuggest.text)){
                LoggerSnack.show(this, "请输入反馈内容！")
                return@setOnClickListener
            }
            LoggerSnack.show(this, "您的反馈内容我们已收到！！")
            Handler().postDelayed( { this.finish() },1500)
        }
    }

    private inner class TextChangeListener: TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (!TextUtils.isEmpty(etSuggest.text)){
                tvNum.text = "${etSuggest.text.length}/300"
            }
        }
    }
}