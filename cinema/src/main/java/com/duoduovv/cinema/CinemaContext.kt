package com.duoduovv.cinema

import dc.android.bridge.BridgeContext

/**
 * @author: jun.liu
 * @date: 2021/1/4 17:57
 * @des:
 */
class CinemaContext:BridgeContext() {
    companion object{
        const val local = "history"
        const val KEY_WORD = "keyWord"

        const val TYPE_BANNER = 100
        const val TYPE_TODAY_RECOMMEND = 200
        const val TYPE_ALL_LOOK = 300
        const val TYPE_RECOMMEND_LIST = 400
    }
}