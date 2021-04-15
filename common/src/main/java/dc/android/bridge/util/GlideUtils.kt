package dc.android.bridge.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.duoduovv.common.R

/**
 * @author: jun.liu
 * @date: 2020/12/24 11:09
 * @des:
 */
class GlideUtils {
    companion object {
        fun setImg(context: Context, url: String, imageView: ImageView) {
            Glide.with(context).load(url).into(imageView)
        }

        fun setMovieImg(context: Context, url: String, imageView: ImageView) {
            RequestOptions().apply {
                placeholder(R.drawable.movie_default)
                error(R.drawable.movie_default)
                Glide.with(context).load(url).apply(this).into(imageView)
            }
        }
    }
}