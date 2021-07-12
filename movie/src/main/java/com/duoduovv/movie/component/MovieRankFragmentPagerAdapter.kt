package com.duoduovv.movie.component

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.duoduovv.common.domain.Column
import com.duoduovv.movie.view.MovieRankFragment

/**
 * @author: jun.liu
 * @date: 2021/7/12 14:24
 * @des:
 */
class MovieRankFragmentPagerAdapter(
    fm: FragmentManager,
    private val count: Int,
    private val data: ArrayList<Column>
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount() = count

    override fun getItem(position: Int) = MovieRankFragment.newInstance(data[position].id)
}