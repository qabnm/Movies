package com.junliu.movie.view

import androidx.recyclerview.widget.LinearLayoutManager
import com.junliu.movie.R
import com.junliu.movie.adapter.MovieRankAdapter
import com.junliu.movie.bean.MovieRankBean
import dc.android.bridge.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_movie_rank.*

/**
 * @author: jun.liu
 * @date: 2021/1/12 11:10
 * @des:榜单页面
 */
class MovieRankFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_movie_rank
    private var rankAdapter: MovieRankAdapter? = null

    override fun initView() {
        rvList.layoutManager = LinearLayoutManager(requireActivity())
        rankAdapter = MovieRankAdapter()
        rvList.adapter = rankAdapter
    }

    override fun initData() {
        val coverUrl =
            "https://pics1.baidu.com/feed/79f0f736afc37931c4ee2c435a3a6c4341a911df.jpeg?token=04ff272dd8409c490e2e79f4b68152fb"
        val data = ArrayList<MovieRankBean>()
        for (i in 0 until 15) {
            data.add(
                MovieRankBean(
                    coverUrl = coverUrl,
                    name = "花木兰",
                    year = "2020",
                    type = "电影",
                    county = "中国大陆",
                    language = "中文",
                    score = "9.2",
                    mainActor = "刘烨  马伊琍  梅婷  保剑锋  曾黎",
                    director = "吴京"
                )
            )
        }
        rankAdapter?.setList(data)
    }

}