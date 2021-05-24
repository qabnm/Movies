package dc.android.bridge.util

import android.content.Context
import android.webkit.WebSettings
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.R

/**
 * @author: jun.liu
 * @date: 2020/12/24 11:09
 * @des:
 */
class GlideUtils {
    companion object {
        fun setImg(context: Context, url: String, imageView: ImageView) {
            val glideUrl = GlideUrl(url, LazyHeaders.Builder().addHeader("User-Agent",
                WebSettings.getDefaultUserAgent(BaseApplication.baseCtx)).build())
            Glide.with(context).load(glideUrl).into(imageView)
        }

        fun setMovieImg(context: Context, url: String, imageView: ImageView) {
            val glideUrl = GlideUrl(url, LazyHeaders.Builder().addHeader("User-Agent",
                WebSettings.getDefaultUserAgent(BaseApplication.baseCtx)).build())
            RequestOptions().apply {
                placeholder(R.drawable.movie_default)
                error(R.drawable.movie_default)
                Glide.with(context).load(glideUrl).apply(this).into(imageView)
            }
        }
    }
}