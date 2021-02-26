package com.junliu.movie.component

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.junliu.movie.R
import com.junliu.movie.bean.MovieDetail
import dc.android.bridge.util.OsUtils

/**
 * @author: jun.liu
 * @date: 2021/2/24 17:24
 * @des:详情简介
 */
class MovieDetailDialogFragment(private val height: Int, private val bean: MovieDetail) :
    DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_movie_detail, container, false)
        initViews(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
    }

    /**
     * 设置dialog宽高
     */
    private fun initWindow() {
        val window = dialog?.window
        window?.let {
            it.attributes.width = OsUtils.getScreenWidth(requireContext())
            it.attributes.height = height
            it.attributes.gravity = Gravity.BOTTOM
        }
    }

    private fun initViews(view: View) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCount: TextView = view.findViewById(R.id.tvCount)
        val tvType: TextView = view.findViewById(R.id.tvType)
        val imgCancel: ImageView = view.findViewById(R.id.imgCancel)
        val tvJubao: TextView = view.findViewById(R.id.tvJubao)
        val tvContent: TextView = view.findViewById(R.id.tvContent)
        tvName.text = bean.vod_name
        tvType.text = "${bean.vod_area_text}/${bean.vod_year}"
        imgCancel.setOnClickListener { dismiss() }
        tvJubao.setOnClickListener {  }
        tvContent.text = bean.vod_blurb
    }
}