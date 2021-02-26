package com.junliu.personal.view

import android.text.Editable
import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.common.util.RouterPath
import com.junliu.common.view.MyTextWatcher
import com.junliu.personal.R
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_modify_sign_name.*

/**
 * @author: jun.liu
 * @date: 2021/2/23 10:50
 * @des:修改签名
 */
@Route(path = RouterPath.PATH_MODIFY_SIGN_NAME)
class ModifySignNameActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_modify_sign_name

    override fun initView() {
        etSignName.addTextChangedListener(object :MyTextWatcher(){
            override fun afterTextChanged(s: Editable?) {
                tvCount.text = "${224 - etSignName.text.length}"
            }
        })
        layoutTopBar.setRightClick {  }
    }
    override fun initData() {
        tvCount.text = "${224 - etSignName.text.length}"
    }
}