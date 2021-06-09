package com.duoduovv.personal.view

import android.text.Editable
import com.alibaba.android.arouter.facade.annotation.Route
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.view.MyTextWatcher
import com.duoduovv.personal.R
import com.duoduovv.personal.databinding.ActivityModifySignNameBinding
import dc.android.bridge.view.BridgeActivity

/**
 * @author: jun.liu
 * @date: 2021/2/23 10:50
 * @des:修改签名
 */
@Route(path = RouterPath.PATH_MODIFY_SIGN_NAME)
class ModifySignNameActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_modify_sign_name
    private lateinit var mBind: ActivityModifySignNameBinding

    override fun initView() {
        mBind = ActivityModifySignNameBinding.bind(layoutView)
        mBind.etSignName.addTextChangedListener(object : MyTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                mBind.tvCount.text = "${224 - mBind.etSignName.text.length}"
            }
        })
        mBind.layoutTopBar.setRightClick { }
    }

    override fun initData() {
        mBind.tvCount.text = "${224 - mBind.etSignName.text.length}"
    }
}