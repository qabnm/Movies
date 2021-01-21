package com.junliu.movie.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.junliu.movie.R
import com.junliu.movie.bean.TypeListArray

/**
 * @author: jun.liu
 * @date: 2021/1/13 11:35
 * @des: 片库筛选条件
 */
class MovieLibraryTypeAdapter(data: MutableList<TypeListArray>) :
    BaseQuickAdapter<TypeListArray, BaseViewHolder>(R.layout.item_movie_library_type, data) {
    override fun convert(holder: BaseViewHolder, item: TypeListArray) {
        val tvType = holder.getView<TextView>(R.id.tvType)
        holder.setText(R.id.tvType, item.name)
    }
}