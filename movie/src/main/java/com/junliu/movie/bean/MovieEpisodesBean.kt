package com.junliu.movie.bean

/**
 * @author: jun.liu
 * @date: 2021/2/23 17:41
 * @des:剧集播放信息 没集的不同清晰度的播放链接
 */
data class MovieEpisodesBean(
    val playInfo: PlayInfoS
)

data class PlayInfoS(
    val lineList: List<Lines>,
    val playNumber: String,
    val playUrls: List<PlayUrls>
)

data class Lines(
    val line: Int,
    val name: String
)

data class PlayUrls(
    val deft: String,
    val url: String
)