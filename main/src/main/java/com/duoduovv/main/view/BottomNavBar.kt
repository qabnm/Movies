package com.duoduovv.main.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.duoduovv.main.R

/**
 * @author: jun.liu
 * @date: 2021/1/14 11:34
 * @des:底部导航
 */
class BottomNavBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var layoutMain: LinearLayout
    private var layoutVideo: LinearLayout
    private var layoutDis: LinearLayout
    private var layoutMine: LinearLayout
    private var imgMain: ImageView
    private var imgVideo: ImageView
    private var imgDis: ImageView
    private var imgMine: ImageView
    private var tvMain: TextView
    private var tvVideo: TextView
    private var tvDis: TextView
    private var tvMine: TextView
    private var listener: OnNavBarClickListener? = null

    init {
        val itemView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_nav, this)
        layoutMain = itemView.findViewById(R.id.layoutMain)
        layoutVideo = itemView.findViewById(R.id.layoutVideo)
        layoutDis = itemView.findViewById(R.id.layoutDiscover)
        layoutMine = itemView.findViewById(R.id.layoutMine)
        imgMain = itemView.findViewById(R.id.imgMain)
        imgVideo = itemView.findViewById(R.id.imgVideo)
        imgDis = itemView.findViewById(R.id.imgDiscover)
        imgMine = itemView.findViewById(R.id.imgMine)
        tvMain = itemView.findViewById(R.id.tvMain)
        tvVideo = itemView.findViewById(R.id.tvVideo)
        tvDis = itemView.findViewById(R.id.tvDiscover)
        tvMine = itemView.findViewById(R.id.tvMine)
        initView()
    }

    private fun resetState() {
        imgMain.isSelected = false
        imgVideo.isSelected = false
        imgDis.isSelected = false
        imgMine.isSelected = false
        tvMain.isSelected = false
        tvVideo.isSelected = false
        tvDis.isSelected = false
        tvMine.isSelected = false
    }

    private fun setSelectState(textView: TextView, imageView: ImageView) {
        textView.isSelected = true
        imageView.isSelected = true
    }

    private fun initView() {
        layoutMain.setOnClickListener {
            listener?.onNavClick(0)
            resetState()
            setSelectState(tvMain, imgMain)
        }
        layoutVideo.setOnClickListener {
            listener?.onNavClick(1)
            resetState()
            setSelectState(tvVideo, imgVideo)
        }
        layoutDis.setOnClickListener {
            listener?.onNavClick(2)
            resetState()
            setSelectState(tvDis, imgDis)
        }
        layoutMine.setOnClickListener {
            listener?.onNavClick(3)
            resetState()
            setSelectState(tvMine, imgMine)
        }
        setSelectState(tvMain, imgMain)
    }

    fun setNavBarClickListener(listener: OnNavBarClickListener) {
        this.listener = listener
    }

    interface OnNavBarClickListener {
        fun onNavClick(position: Int)
    }
}