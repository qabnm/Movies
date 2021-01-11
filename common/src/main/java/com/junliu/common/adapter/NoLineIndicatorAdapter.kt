package com.junliu.common.adapter

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.junliu.common.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

/**
 * @author: jun.liu
 * @date: 2021/1/11 9:49
 * @des:
 */
class NoLineIndicatorAdapter(private val viewPager: ViewPager, private val data: List<String>?) : CommonNavigatorAdapter() {
    override fun getCount() = data?.size ?: 0

    override fun getTitleView(context: Context?, index: Int)= ScalePagerTitleView(context).run {
        normalColor = context?.let { ContextCompat.getColor(it , R.color.color000000) }?: Color.parseColor("#000000")
        selectedColor = context?.let { ContextCompat.getColor(it , R.color.color567CE7) }?: Color.parseColor("#567CE7")
        text = data?.get(index)
        setSelectTextSize(R.dimen.sp_16)
        setUnSelectTextSize(R.dimen.sp_16)
        setOnClickListener { viewPager.currentItem = index }
        this
    }

    override fun getIndicator(context: Context?) = LinePagerIndicator(context).run {
        mode = LinePagerIndicator.MODE_EXACTLY
        context?.let {  setColors(ContextCompat.getColor(it, R.color.color567CE7))}
        lineHeight = 0f
        lineWidth = 0f
        this
    }
}