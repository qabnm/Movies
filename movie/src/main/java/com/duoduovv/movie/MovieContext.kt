package com.duoduovv.movie

import dc.android.bridge.BridgeContext

/**
 * @author: jun.liu
 * @date: 2021/3/11 11:44
 * @des:
 */
class MovieContext :BridgeContext() {
    companion object{
        /**
         * 电影
         */
        const val TYPE_MOVIE = 1

        /**
         * 电视剧
         */
        const val TYPE_TV = 2

        /**
         * 综艺
         */
        const val TYPE_VARIETY = 3
    }
}