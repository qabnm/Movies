package com.duoduovv.movie.component

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.duoduovv.movie.R
import com.duoduovv.movie.bean.MovieDetail
import com.duoduovv.movie.databinding.LayoutMovieDetailBinding

/**
 * @author: jun.liu
 * @date: 2021/2/24 17:24
 * @des:详情简介
 */
class MovieDetailDialogFragment(
    private val height: Int,
    private val bean: MovieDetail,
    private val listener: OnReportClickListener?
) :
    DialogFragment() {
    private lateinit var mBind: LayoutMovieDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mBind = LayoutMovieDetailBinding.inflate(inflater, container, false)
        initViews()
        return mBind.root
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
            it.decorView.setPadding(0, 0, 0, 0)
            it.attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            it.attributes.height = height
            it.attributes.gravity = Gravity.BOTTOM
            it.setBackgroundDrawableResource(R.color.colorFFFFFF)
            it.attributes.windowAnimations = R.style.BottomToTopAnim
            it.setDimAmount(0f)
        }
    }

    private fun initViews() {
        mBind.tvName.text = bean.vodName
        mBind.tvType.text = "${bean.vodArea}/${bean.vodYear}"
        mBind.imgCancel.setOnClickListener { dismiss() }
        mBind.tvJubao.setOnClickListener { }
        mBind.tvContent.text = bean.vodDetail
        mBind.tvJubao.setOnClickListener { listener?.onReportClick(bean.id) }
    }

    interface OnReportClickListener {
        fun onReportClick(movieId: String)
    }
}