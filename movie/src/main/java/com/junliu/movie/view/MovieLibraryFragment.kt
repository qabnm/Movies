package com.junliu.movie.view

import androidx.recyclerview.widget.GridLayoutManager
import com.junliu.movie.R
import com.junliu.movie.adapter.MovieLibraryAdapter
import com.junliu.movie.bean.Filter
import com.junliu.movie.bean.Movie
import com.junliu.movie.bean.MovieLibraryBean
import com.junliu.movie.bean.Type
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.LIST
import dc.android.bridge.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_movie_library.*

/**
 * @author: jun.liu
 * @date: 2021/1/12 11:09
 * @des:片库fragment
 */
class MovieLibraryFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_movie_library

    private var adapter: MovieLibraryAdapter? = null
    private var key = ""
    private var typeList: ArrayList<Filter>? = null

    override fun initView() {
        rvList.layoutManager = GridLayoutManager(requireActivity(), 3)
    }

    override fun initData() {
        key = arguments?.getString(ID, "") ?: ""
        typeList = arguments?.getParcelableArrayList(LIST)

        val typeList = ArrayList<Type>()
        val data = ArrayList<Movie>()
        val coverUrl =
            "https://pics1.baidu.com/feed/79f0f736afc37931c4ee2c435a3a6c4341a911df.jpeg?token=04ff272dd8409c490e2e79f4b68152fb"

        typeList.add(Type(listOf("最热", "最近更新", "好评", "新剧", "经典"), "year"))
        typeList.add(Type(listOf("全部", "中国大陆", "美国", "中国香港", "中国台湾", "韩国", "日本"), "year"))
        typeList.add(
            Type(
                listOf(
                    "不限",
                    "2021",
                    "2020",
                    "2019",
                    "2018",
                    "2017",
                    "2016",
                    "2015",
                    "2014",
                    "2013",
                    "2012",
                    "2011",
                    "2010",
                    "2009"
                ), "year"
            )
        )
        typeList.add(
            Type(
                listOf("全部", "剧情", "喜剧", "动作", "爱情", "欧美", "经典", "伦理", "科幻", "动画"),
                "year"
            )
        )
        typeList.add(Type(listOf("全部", "连载", "完结", "预告"), "year"))
        for (i in 0 until 12) {
            data.add(Movie(coverUrl = coverUrl, movieName = "花木兰", score = "9.4"))
        }
        val bean = MovieLibraryBean(data, typeList)
        adapter = MovieLibraryAdapter(requireActivity(), bean)
        rvList.adapter = adapter
    }
}