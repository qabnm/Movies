package com.junliu.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @author: jun.liu
 * @date: 2021/1/5 : 17:43
 */
class ViewPagerAdapter(private val fm: FragmentManager,private val data:List<Fragment>) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount() = data.size

    override fun getItem(position: Int) = data[position]
}