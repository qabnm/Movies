package com.duoduovv.cinema.component

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.duoduovv.cinema.view.SearchResultListFragment
import com.duoduovv.common.domain.Column

/**
 * @author: jun.liu
 * @date: 2021/7/12 14:36
 * @des:
 */
class SearchResultFragmentPagerAdapter(
    fm: FragmentManager,
    private val count: Int,
    private val dataList: List<Column>,
    private val keyWord: String
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount() = count

    override fun getItem(position: Int) =
        SearchResultListFragment.newInstance(dataList[position].id, keyWord)

}