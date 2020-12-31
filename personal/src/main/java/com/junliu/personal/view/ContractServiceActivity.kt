package com.junliu.personal.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.common.util.RouterPath.Companion.PATH_CONTRACT_SERVICE_ACTIVITY
import com.junliu.personal.R
import dc.android.bridge.view.BridgeActivity

/**
 * @author: jun.liu
 * @date: 2020/12/31 : 10:23
 * 联系客服
 *
 */
@Route(path = PATH_CONTRACT_SERVICE_ACTIVITY)
class ContractServiceActivity :BridgeActivity(){
    override fun getLayoutId() = R.layout.activity_contract_service
}