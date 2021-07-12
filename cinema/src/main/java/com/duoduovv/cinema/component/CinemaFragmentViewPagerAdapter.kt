package com.duoduovv.cinema.component

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.duoduovv.cinema.view.CinemaListFragment

/**
 * @author: jun.liu
 * @date: 2021/7/12 11:49
 * @des:
 */
class CinemaFragmentViewPagerAdapter(
    fm: FragmentManager,
    private val count: Int,
    private val idList: ArrayList<String>
) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount() = count

    override fun getItem(position: Int) = CinemaListFragment.newInstance(id = idList[position])
}