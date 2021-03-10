package com.duoduovv.movie.bean

/**
 * @author: jun.liu
 * @date: 2021/3/10 10:26
 * @des:审核版的影视详情
 */
data class MovieDetailForDebugBean(
    val movie: MovieForDebug,
    val movieDetail: MovieDetailForDebug
)

data class MovieForDebug(
    val cover_url: String,
    val hot: String,
    val id: String,
    val is_end: String,
    val last_remark: String,
    val movie_detail_id: String,
    val movie_flag: String,
    val remark: String,
    val score: String,
    val str_id: String,
    val type_id: String,
    val type_id_text: String,
    val vod_actor: String,
    val vod_area: String,
    val vod_area_text: String,
    val vod_blurb: String,
    val vod_director: String,
    val vod_lang: String,
    val vod_name: String,
    val vod_number: String,
    val vod_year: String
)

data class MovieDetailForDebug(
    val actor: String,
    val actor_array: List<ActorArray>,
    val alias_name: String,
    val area: String,
    val blurb: String,
    val category: String,
    val comment_array: List<CommentArray>,
    val comment_count: String,
    val created_at: String,
    val director: String,
    val drama: String,
    val duration: String,
    val id: String,
    val imdb: String,
    val lang: String,
    val name: String,
    val numbers: String,
    val playbill_array: List<PlaybillArray>,
    val recommend_array: List<RecommendArray>,
    val released_date: String,
    val score: String,
    val source: String,
    val source_id: String,
    val stage_photo_array: List<StagePhotoArray>,
    val tag_array: List<String>,
    val updated_at: String,
    val year: String
)

data class ActorArray(
    val Img: String,
    val Name: String,
    val Role: String
)

data class CommentArray(
    val Content: String,
    val Nick: String,
    val Score: Double,
    val Time: String
)

data class PlaybillArray(
    val Img: String,
    val Size: String,
    val Title: String
)

data class RecommendArray(
    val Img: String,
    val Name: String,
    val Url: String
)

data class StagePhotoArray(
    val Img: String,
    val Size: String,
    val Title: String
)