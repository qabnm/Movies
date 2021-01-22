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
    private var listener: OnTypeClickListener? = null

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

    fun setList(data: List<TypeListArray>, key: String) {
        val typeAdapter = MovieLibraryTypeAdapter(data as MutableList<TypeListArray>)
        typeAdapter.setOnItemChildClickListener { adapter, view, position ->
            for (i in data.indices) {
                data[i].isSelect = false
            }
            data[position].isSelect = true
            adapter.notifyDataSetChanged()
            listener?.onTypeClick(key, data[position].key)
        }
        rvList.adapter = typeAdapter
    }

    interface OnTypeClickListener {
        fun onTypeClick(key: String, name: String)
    }

    fun setOnTypeClickListener(listener: OnTypeClickListener) {
        this.listener = listener
    }
}