package com.duoduovv.common.adapter

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.duoduovv.common.R
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

/**
 * @author: jun.liu
 * @date: 2021/1/6 15:20
 * @des:
 */
class ScaleTitleNavAdapter(
    private val viewPager: ViewPager,
    private val data: List<String>?,
    private val unSelectColor: Int = R.color.color363636,
    private val selectColor: Int = R.color.color000000,
    private val unSelectSize: Int = R.dimen.sp_15,
    private val selectSize: Int = R.dimen.sp_18
) :
    CommonNavigatorAdapter() {
    override fun getCount() = data?.size ?: 0

    override fun getTitleView(context: Context?, index: Int) = ScalePagerTitleView(context).run {
        normalColor = context?.let { ContextCompat.getColor(it, unSelectColor) }
            ?: Color.parseColor("#363636")
        selectedColor = context?.let { ContextCompat.getColor(it, selectColor) }
            ?: Color.parseColor("#000000")
        text = data?.get(index)
        setOnClickListener { viewPager.currentItem = index }
        setSelectTextSize(selectSize)
        setUnSelectTextSize(unSelectSize)
        this
    }

    override fun getIndicator(context: Context?) = LinePagerIndicator(context).run {
        mode = LinePagerIndicator.MODE_EXACTLY
        context?.let { setColors(ContextCompat.getColor(it, R.color.color567CE7)) }
        roundRadius = UIUtil.dip2px(context, 1.0).toFloat()
        lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
        lineWidth = UIUtil.dip2px(context, 18.0).toFloat()
        this
    }
}