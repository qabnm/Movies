package com.duoduovv.common.adapter

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.duoduovv.common.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

/**
 * @author: jun.liu
 * @date: 2021/1/11 9:49
 * @des:
 */
class NoLineIndicatorAdapter(
    private val viewPager: ViewPager,
    private val data: List<String>?,
    private val unSelectColor: Int = R.color.color000000,
    private val selectColor: Int = R.color.color567CE7,
    private val unSelectSize :Int = R.dimen.sp_16,
    private val selectSize :Int = R.dimen.sp_16
) : CommonNavigatorAdapter() {
    override fun getCount() = data?.size ?: 0

    override fun getTitleView(context: Context?, index: Int) = ScalePagerTitleView(context).run {
        normalColor = context?.let { ContextCompat.getColor(it, unSelectColor) }
            ?: Color.parseColor("#000000")
        selectedColor = context?.let { ContextCompat.getColor(it, selectColor) }
            ?: Color.parseColor("#567CE7")
        text = data?.get(index)
        setSelectTextSize(selectSize)
        setUnSelectTextSize(unSelectSize)
        setOnClickListener { viewPager.currentItem = index }
        this
    }

    override fun getIndicator(context: Context?) = LinePagerIndicator(context).run {
        mode = LinePagerIndicator.MODE_EXACTLY
        lineHeight = 0f
        lineWidth = 0f
        this
    }
}