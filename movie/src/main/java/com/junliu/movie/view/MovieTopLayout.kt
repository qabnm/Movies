package com.junliu.movie.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.junliu.movie.R
import com.junliu.movie.adapter.MovieLibraryTypeAdapter
import com.junliu.movie.bean.TypeListArray

/**
 * @author: jun.liu
 * @date: 2021/1/13 11:26
 * @des：片库筛选条件列表
 */
class MovieTopLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var rvList: RecyclerView

    init {
        removeAllViews()
        val view = inflate(context, R.layout.item_recyclerview, null)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        view.layoutParams = layoutParams
        rvList = view.findViewById(R.id.rvList)
        rvList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        gravity = Gravity.CENTER_VERTICAL
        addView(view)
    }

    fun setList(data: List<TypeListArray>) {
        rvList.adapter = MovieLibraryTypeAdapter(data as MutableList<TypeListArray>)
    }
}