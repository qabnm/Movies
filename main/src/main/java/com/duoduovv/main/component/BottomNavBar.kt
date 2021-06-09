package com.duoduovv.main.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.duoduovv.main.R
import com.duoduovv.main.databinding.LayoutBottomNavBinding

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
    private var mBind: LayoutBottomNavBinding
    private var listener: OnNavBarClickListener? = null

    init {
        val itemView = LayoutInflater.from(context).inflate(R.layout.layout_bottom_nav, this)
        mBind = LayoutBottomNavBinding.bind(itemView)
        initView()
    }

    private fun resetState() {
        mBind.imgMain.isSelected = false
        mBind.imgVideo.isSelected = false
        mBind.imgDiscover.isSelected = false
        mBind.imgMine.isSelected = false
        mBind.tvMain.isSelected = false
        mBind.tvVideo.isSelected = false
        mBind.tvDiscover.isSelected = false
        mBind.tvMine.isSelected = false
    }

    private fun setSelectState(textView: TextView, imageView: ImageView) {
        textView.isSelected = true
        imageView.isSelected = true
    }

    private fun initView() {
        mBind.layoutMain.setOnClickListener {
            listener?.onNavClick(0)
            resetState()
            setSelectState(mBind.tvMain, mBind.imgMain)
        }
        mBind.layoutVideo.setOnClickListener {
            listener?.onNavClick(1)
            resetState()
            setSelectState(mBind.tvVideo, mBind.imgVideo)
        }
        mBind.layoutDiscover.setOnClickListener {
            listener?.onNavClick(2)
            resetState()
            setSelectState(mBind.tvDiscover, mBind.imgDiscover)
        }
        mBind.layoutMine.setOnClickListener {
            listener?.onNavClick(3)
            resetState()
            setSelectState(mBind.tvMine, mBind.imgMine)
        }
        setSelectState(mBind.tvMain, mBind.imgMain)
    }

    fun setNavBarClickListener(listener: OnNavBarClickListener) {
        this.listener = listener
    }

    interface OnNavBarClickListener {
        fun onNavClick(position: Int)
    }
}