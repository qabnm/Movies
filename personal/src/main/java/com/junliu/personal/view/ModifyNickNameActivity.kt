package com.junliu.personal.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.common.util.RouterPath
import com.junliu.personal.R
import dc.android.bridge.view.BridgeActivity

/**
 * @author: jun.liu
 * @date: 2021/2/23 10:26
 * @des:修改昵称
 */
@Route(path = RouterPath.PATH_MODIFY_NICKNAME)
class ModifyNickNameActivity:BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_modify_nickname
}