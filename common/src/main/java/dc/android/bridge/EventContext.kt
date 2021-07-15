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
         * 播放失败
         */
        const val EVENT_PLAY_ERROR = "play_error"
    }
}