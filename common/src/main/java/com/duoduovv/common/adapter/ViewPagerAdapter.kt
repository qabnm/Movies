package com.duoduovv.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * @author: jun.liu
 * @date: 2021/1/5 : 17:43
 */
class ViewPagerAdapter(fm: FragmentManager, private val data: ArrayList<Fragment>) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount() = data.size

    override fun getItem(position: Int) = data[position]
}