package com.junliu.common.adapter

import android.content.Context
import android.util.TypedValue
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

/**
 * @author: jun.liu
 * @date: 2021/1/6 15:02
 * @des:
 */
class ScalePagerTitleView(context: Context?) : SimplePagerTitleView(context) {
    private var selectTextSize = 0
    private var unSelectTextSize = 0
    fun setSelectTextSize(selectTextSize: Int) {
        this.selectTextSize = selectTextSize
    }

    fun setUnSelectTextSize(unSelectTextSize: Int) {
        this.unSelectTextSize = unSelectTextSize
    }

    override fun onSelected(index: Int, totalCount: Int) {
        super.onSelected(index, totalCount)
        setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            resources.getDimensionPixelSize(selectTextSize).toFloat()
        )
        this.paint.isFakeBoldText = true
    }

    override fun onDeselected(index: Int, totalCount: Int) {
        super.onDeselected(index, totalCount)
        setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            resources.getDimensionPixelSize(unSelectTextSize).toFloat()
        )
        this.paint.isFakeBoldText = false
    }
}