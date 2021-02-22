package com.junliu.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * @author: jun.liu
 * @date: 2021/2/7 10:51
 * @des:
 */
class MediumBoldTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    override fun onDraw(canvas: Canvas?) {
        paint.apply {
            strokeWidth = 1.4f
            style = Paint.Style.FILL_AND_STROKE
        }
        super.onDraw(canvas)
    }
}