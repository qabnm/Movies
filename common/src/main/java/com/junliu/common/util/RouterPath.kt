package com.junliu.common.util

/**
 * @author: jun.liu
 * @date: 2020/12/24 13:39
 * @des:路由地址管理
 */
class RouterPath {
    companion object{

        /**
         * 首页
         */
        const val PATH_MAIN = "/main/MainActivity"
        /**
         * 首页
         */
        const val PATH_CINEMA = "/cinema/CinemaFragment"

        /**
         * 热点
         */
        const val PATH_HOTSPOT = "/hotspot/HotSpotFragment"

        /**
         * 发现
         */
        const val PATH_MOVIE = "/movie/MovieFragment"

        /**
         * 个人中心
         */
        const val PATH_PERSONAL = "/personal/PersonalFragment"

        /**
         * 设置页面
         */
        const val PATH_SETTING_ACTIVITY = "/personal/SettingActivity"

        /**
         * 联系客服
         */
        const val PATH_CONTRACT_SERVICE_ACTIVITY = "/personal/ContractServiceActivity"

        /**
         * 搜索
         */
        const val PATH_SEARCH_ACTIVITY = "/cinema/SearchActivity"

        /**
         * 观看历史
         */
        const val PATH_WATCH_HISTORY = "/movie/WatchHistoryActivity"

        /**
         * 我的收藏
         */
        const val PATH_MY_COLLECTION = "/personal/MyCollectionActivity"

        /**
         * 影片详情
         */
        const val PATH_MOVIE_DETAIL ="/movie/MovieDetailActivity"

        /**
         * 编辑资料
         */
        const val PATH_EDIT_MATERIALS = "/personal/EditMaterialsActivity"

        /**
         * 修改昵称
         */
        const val PATH_MODIFY_NICKNAME = "/personal/ModifyNickNameActivity"
    }
}