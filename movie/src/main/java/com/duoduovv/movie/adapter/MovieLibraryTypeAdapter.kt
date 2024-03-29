package com.duoduovv.movie.adapter

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.TypeListArray

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
        tvType.setTextColor(if (item.isSelect) ContextCompat.getColor(context,R.color.color567CE7) else ContextCompat.getColor(context, R.color.color666666))
    }
}