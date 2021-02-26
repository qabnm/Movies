package com.junliu.movie.adapter

import android.widget.TextView
import androidx.core.content.ContextCompat
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
        tvType.setTextColor(if (item.isSelect) ContextCompat.getColor(context,R.color.color567CE7) else ContextCompat.getColor(context, R.color.color666666))
        tvType.setOnClickListener {
            for (i in data.indices){
                data[i].isSelect = false
            }
            data[holder.layoutPosition].isSelect = true
            this.notifyDataSetChanged()
            //将选择的数据返回 更新列表显示
        }
    }
}