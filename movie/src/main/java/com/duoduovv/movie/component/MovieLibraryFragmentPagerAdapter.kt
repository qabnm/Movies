package com.duoduovv.movie.component

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.duoduovv.movie.bean.Config
import com.duoduovv.movie.bean.Filter
import com.duoduovv.movie.view.MovieLibraryFragment

/**
 * @author: jun.liu
 * @date: 2021/7/12 13:42
 * @des:
 */
class MovieLibraryFragmentPagerAdapter(
    fm: FragmentManager,
    private val count: Int,
    private val configures: ArrayList<Config>
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount() = count

    override fun getItem(position: Int) = MovieLibraryFragment.newInstance(
        configures[position].key,
        configures[position].filter as ArrayList<Filter>
    )
}