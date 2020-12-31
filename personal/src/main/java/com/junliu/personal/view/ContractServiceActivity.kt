package com.junliu.personal.view

import android.text.Editable
import android.text.TextWatcher
import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.common.util.RouterPath.Companion.PATH_CONTRACT_SERVICE_ACTIVITY
import com.junliu.personal.R
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
    }

    private class TextChangeListener: TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
        }
    }
}