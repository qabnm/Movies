package com.duoduovv.cinema.component

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.duoduovv.cinema.R
import com.duoduovv.common.BaseApplication
import com.duoduovv.room.database.WatchHistoryDatabase
import com.google.android.material.snackbar.Snackbar
import dc.android.bridge.BridgeContext.Companion.TYPE_ALBUM
import dc.android.bridge.BridgeContext.Companion.TYPE_TV
import dc.android.bridge.BridgeContext.Companion.TYPE_TV0
import dc.android.bridge.util.OsUtils
import dc.android.bridge.util.StringUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * @author: jun.liu
 * @date: 2021/6/22 18:02
 * @des:
 */
class SnackBarView {
    private var listener: OnSnackClickListener? = null

    /**
     * 弹出snackBar
     * @param view View
     * @param context Context
     */
    fun initSnack(view: View, context: Context) {
        GlobalScope.launch(Dispatchers.Main) {
            val list = getDB()
            if (list.isNotEmpty()) {
                Collections.sort(list) { o1, o2 -> (o2.currentTime - o1.currentTime).toInt() }
                val historyBean = list[0]
                val content: String = when (historyBean.type) {
                    TYPE_TV, TYPE_TV0 -> {
                        "上次看到：${historyBean.title}第${historyBean.vidTitle}集${
                            StringUtils.getDifferTime(
                                historyBean.currentLength
                            )
                        }"
                    }
                    TYPE_ALBUM -> {
                        "上次看到：${historyBean.title}第${historyBean.vidTitle}${
                            StringUtils.getDifferTime(
                                historyBean.currentLength
                            )
                        }"
                    }
                    else -> {
                        "上次看到：${historyBean.title}${StringUtils.getDifferTime(historyBean.currentLength)}"
                    }
                }

                val snackBar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
                snackBar.apply {
                    setAction("×") { this.dismiss() }
                    setText(content)
                    setActionTextColor(ContextCompat.getColor(context, R.color.colorFFFFFF))
                    duration = 5000
                }
                val snackBarView = snackBar.view
                snackBarView.apply {
                    val parent = this as Snackbar.SnackbarLayout
                    parent.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    parent.setPadding(0, 0, OsUtils.dip2px(context, -10f), 0)
                    val textView = findViewById<TextView>(R.id.snackbar_text)
                    textView.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        resources.getDimensionPixelSize(R.dimen.sp_12).toFloat()
                    )
                    textView.setTextColor(ContextCompat.getColor(context, R.color.colorFFFFFF))
                    setBackgroundColor(Color.parseColor("#CC3D75FF"))
                    setOnClickListener {
                        listener?.onSnackClick(historyBean.movieId)
                        snackBar.dismiss()
                    }
                }
                snackBar.show()
            }
        }
    }

    /**
     * 获取数据库数据
     * @return List<VideoWatchHistoryBean>
     */
    private suspend fun getDB() = withContext(Dispatchers.IO) {
        WatchHistoryDatabase.getInstance(BaseApplication.baseCtx).history().queryAll()
    }

    fun setSnackClick(listener: OnSnackClickListener) {
        this.listener = listener
    }

    interface OnSnackClickListener {
        fun onSnackClick(movieId: String)
    }
}