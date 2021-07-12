package com.duoduovv.movie.component

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.duoduovv.movie.view.MovieLibraryNavFragment
import com.duoduovv.movie.view.MovieRankNavFragment
import com.duoduovv.movie.view.SubjectFragment

/**
 * @author: jun.liu
 * @date: 2021/7/12 14:04
 * @des:
 */
class MovieFragmentPagerAdapter(fm: FragmentManager, private val typeId: String, private val count: Int) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount() = count

    override fun getItem(position: Int): Fragment {
        if (count == 2){
            //这时候没有专题
            return when(position){
                0 ->{
                    //片库页面
                    MovieLibraryNavFragment.newInstance(typeId = typeId)
                }
                else ->{
                    //排行榜页面
                    MovieRankNavFragment.newInstance()
                }
            }
        }else{
            //有专题 现在有三个对象
            return when(position){
                0->{
                    //专题
                    SubjectFragment.newInstance()
                }
                1->{
                    //片库
                    MovieLibraryNavFragment.newInstance(typeId = typeId)
                }
                else ->{
                    //排行榜
                    MovieRankNavFragment.newInstance()
                }
            }
        }
    }
}