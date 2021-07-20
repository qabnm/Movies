package dc.android.bridge

/**
 * @author: jun.liu
 * @date: 2021/7/15 9:44
 * @des:
 */
class EventContext {
    companion object{

        /**
         * 栏目分类
         */
        const val EVENT_CATEGORY = "category"
        /**
         * 从banner进入影视详情
         */
        const val EVENT_BANNER_MOVIE_DETAIL= "banner_movie_detail"
        /**
         * 点击栏目进入影视详情页面
         */
        const val EVENT_MOVIE_DETAIL = "movie_detail"
        /**
         * 可播放统计
         */
        const val EVENT_WAY_RELEASE = "way_release"

        /**
         * 搜索关键词
         */
        const val EVENT_SEARCH_KEYWORD = "search_keyWord"

        /**
         * 剧集解析失败
         */
        const val EVENT_JX_ERROR = "jx_error"

        /**
         * 播放成功
         */
        const val EVENT_PLAY_SUCCESS = "play_success"

        /**
         * 播放失败
         */
        const val EVENT_PLAY_ERROR = "play_error"

        /**
         * 观看历史
         */
        const val EVENT_WATCH_HISTORY = "watch_history"

        /**
         * 收藏列表
         */
        const val EVENT_COLLECTION = "collection"

        /**
         * 分享好友
         */
        const val EVENT_SHARE_CENTER="share_center"

        /**
         * QQ分享
         */
        const val EVENT_SHARE_QQ = "share_qq"

        /**
         * QQ空间分享
         */
        const val EVENT_SHARE_QQ_ZONE = "share_qq_zone"

        /**
         * 微信分享
         */
        const val EVENT_SHARE_WEICHAT = "share_weichat"

        /**
         * 微信朋友圈分享
         */
        const val EVENT_SHARE_WEI_CIELE = "share_weichat_circle"

        /**
         * 复制链接分享
         */
        const val EVENT_SHARE_COPY = "share_copy"

        /**
         * 微信登录成功
         */
        const val EVENT_WEICHAT_LOGIN_SUCCESS = "weichat_login_success"

        /**
         * 微信登录失败
         */
        const val EVENT_WEICHAT_LOGIN_fail = "weichat_login_fail"

        /**
         * QQ登录成功
         */
        const val EVENT_QQ_LOGIN_SUCCESS = "qq_login_success"

        /**
         * QQ登录失败
         */
        const val EVENT_QQ_LOGIN_fail = "qq_login_fail"

        /**
         * 专题tab
         */
        const val EVENT_SUBJECT_TAB = "subject_tab"

        /**
         * 片库tab
         */
        const val EVENT_MOVIE_LIB_TAB = "movie_lib_tab"

        /**
         * 榜单tab
         */
        const val EVENT_RANK_TAB = "rank_tab"

        /**
         * 专题跳转播放详情
         */
        const val EVENT_SUBJECT_TO_DETAIL = "subject_to_detail"

        /**
         * 片库跳转播放详情
         */
        const val EVENT_MOVIELIB_TO_DETAIL = "movie_lib_to_detail"

        /**
         * 排行榜跳转播放详情
         */
        const val EVENT_RANK_TO_DETAIL = "rank_to_detail"

        /**
         * 详情页推荐
         */
        const val EVENT_MOVIE_DETAIL_RECOMMEND = "movie_detail_recommend"

    }
}