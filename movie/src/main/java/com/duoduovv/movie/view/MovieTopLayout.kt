package com.duoduovv.movie.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.MovieLibraryTypeAdapter
import com.duoduovv.movie.bean.TypeListArray
import com.duoduovv.movie.databinding.ItemRecyclerviewBinding

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
    private var mBind: ItemRecyclerviewBinding
    private var listener: OnTypeClickListener? = null

    init {
        removeAllViews()
        val view = inflate(context, R.layout.item_recyclerview, null)
        mBind = ItemRecyclerviewBinding.bind(view)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        view.layoutParams = layoutParams
        mBind.rvList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        gravity = Gravity.CENTER_VERTICAL
        addView(view)
    }

    fun setList(data: ArrayList<TypeListArray>, key: String) {
        if (data.size > 0) {
            var count = 0
            for (i in data.indices) {
                //如果没有一个选项是被选中的 那么就选中第一个
                if (!data[i].isSelect) count++
            }
            if (count == data.size) data[0].isSelect = true
        }
        val typeAdapter = MovieLibraryTypeAdapter(data as MutableList<TypeListArray>)
        typeAdapter.addChildClickViewIds(R.id.tvType)
        typeAdapter.setOnItemChildClickListener { adapter, _, position ->
            for (i in data.indices) {
                data[i].isSelect = false
            }
            data[position].isSelect = true
            adapter.notifyDataSetChanged()
            listener?.onTypeClick(key, data[position].key)
        }
        mBind.rvList.adapter = typeAdapter
    }

    interface OnTypeClickListener {
        fun onTypeClick(key: String, name: String)
    }

    fun setOnTypeClickListener(listener: OnTypeClickListener) {
        this.listener = listener
    }
}