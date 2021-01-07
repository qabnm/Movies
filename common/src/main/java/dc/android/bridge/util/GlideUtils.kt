package dc.android.bridge.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

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
    }
}