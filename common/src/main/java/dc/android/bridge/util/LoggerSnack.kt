package dc.android.bridge.util

import android.app.Activity
import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * @author: jun.liu
 * @date: 2020/10/16 : 14:53
 */
class LoggerSnack {
    companion object {
        fun show(ctx: Context, str: String?) {
            show(
                ((ctx as Activity).window.decorView),
                str,
                Snackbar.LENGTH_SHORT
            )
        }

        fun show(ctx: Context, strId: Int) {
            show(
                ((ctx as Activity).window.decorView),
                strId,
                Snackbar.LENGTH_SHORT
            )
        }

        fun show(view: View, strId: Int) {
            show(
                view,
                strId,
                Snackbar.LENGTH_SHORT
            )
        }

        fun show(view: View, strId: Int, duration: Int) {
            Snackbar.make(view, strId, duration).show()
        }

        fun show(view: View, str: String?, duration: Int) {
            str?.let { Snackbar.make(view, it, duration).show() }
        }
    }
}