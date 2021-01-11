package com.junliu.common.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.junliu.common.R

/**
 * @author: jun.liu
 * @date: 2021/1/11 16:45
 * @des:
 */
class EmptyLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): RelativeLayout(context, attrs, defStyleAttr){
    private lateinit var imgEmpty:ImageView
    private lateinit var tvEmpty:TextView
    private var imgSource = 0
    private var emptyText = ""
    private var visible = 2 //1 可见
    private var emptyTextColor = 0
    private var emptyTextSize = 0f
    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.EmptyLayout)
        imgSource = typeArray.getResourceId(R.styleable.EmptyLayout_imgResource, 0)
        emptyText = typeArray.getString(R.styleable.EmptyLayout_emptyText).toString()
        visible = typeArray.getInt(R.styleable.EmptyLayout_visibility, 2)
        emptyTextColor = typeArray.getColor(R.styleable.EmptyLayout_emptyTextColor, Color.parseColor("#869EED"))
        emptyTextSize = typeArray.getDimension(R.styleable.EmptyLayout_emptyTextSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, context.resources.displayMetrics))
        typeArray.recycle()
        val view = LayoutInflater.from(context).inflate(R.layout.layout_empty, this)
        imgEmpty = view.findViewById(R.id.imgEmpty)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        imgEmpty.visibility = if (visible ==1) View.VISIBLE else View.GONE
        tvEmpty.visibility = if (visible ==1) View.VISIBLE else View.GONE
        imgEmpty.setImageResource(imgSource)
        tvEmpty.text = emptyText
        tvEmpty.setTextColor(emptyTextColor)
        tvEmpty.setTextSize(TypedValue.COMPLEX_UNIT_PX , emptyTextSize)
    }

    fun setEmptyVisibility(visibility: Int){
        imgEmpty.visibility = visibility
        tvEmpty.visibility = visibility
    }

    fun setEmptyView(imgSource: Int){
        imgEmpty.setImageResource(imgSource)
    }

    fun setEmptyText(text: String){
        tvEmpty.text = text
    }

}