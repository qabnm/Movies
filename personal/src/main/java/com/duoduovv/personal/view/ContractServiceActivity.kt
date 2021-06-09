package com.duoduovv.personal.view

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.alibaba.android.arouter.facade.annotation.Route
import com.duoduovv.common.util.RouterPath.Companion.PATH_CONTRACT_SERVICE_ACTIVITY
import com.duoduovv.personal.R
import com.duoduovv.personal.databinding.ActivityContractServiceBinding
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.view.BridgeActivity

/**
 * @author: jun.liu
 * @date: 2020/12/31 : 10:23
 * 联系客服
 *
 */
@Route(path = PATH_CONTRACT_SERVICE_ACTIVITY)
class ContractServiceActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_contract_service
    private lateinit var mBind: ActivityContractServiceBinding
    override fun initView() {
        mBind = ActivityContractServiceBinding.bind(layoutView)
    }

    override fun initData() {
        mBind.etSuggest.addTextChangedListener(TextChangeListener())
        mBind.btnCommit.setOnClickListener {
            if (TextUtils.isEmpty(mBind.etSuggest.text)) {
                AndroidUtils.toast("请输入反馈内容！", this)
                return@setOnClickListener
            }
            AndroidUtils.toast("您的反馈内容我们已收到！！", this)
        }
    }

    private inner class TextChangeListener : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (!TextUtils.isEmpty(mBind.etSuggest.text)) {
                mBind.tvNum.text = "${mBind.etSuggest.text.length}/300"
            }
        }
    }
}