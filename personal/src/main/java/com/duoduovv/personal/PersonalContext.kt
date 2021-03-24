package com.duoduovv.personal

import dc.android.bridge.BridgeContext

/**
 * @author: jun.liu
 * @date: 2021/3/24 10:21
 * @des:
 */
class PersonalContext :BridgeContext() {
    companion object{
        /**
         * 用户协议
         */
        const val URL_USER_AGREEMENT ="${BASE_URL}help/agreement.html"

        /**
         * 隐私政策
         */
        const val URL_PRIVACY ="${BASE_URL}help/privacy.html"
    }
}