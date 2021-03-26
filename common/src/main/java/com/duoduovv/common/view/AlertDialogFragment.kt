package com.duoduovv.common.view

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.duoduovv.common.R
import dc.android.bridge.util.OsUtils

/**
 * @author: jun.liu
 * @date: 2021/3/23 11:04
 * @des:警示弹窗
 */
class AlertDialogFragment(
    private val content: String,
    private val listener: OnDialogSureClickListener?,
    private val width:Float = 300f
) : DialogFragment() {
    private lateinit var tvCancel: TextView
    private lateinit var tvSure: TextView
    private lateinit var tvTitle: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutView = inflater.inflate(R.layout.dialog_alert, container, false)
        initViews(layoutView)
        return layoutView
    }

    private fun initViews(layoutView: View) {
        val tvContent: TextView = layoutView.findViewById(R.id.tvContent)
        tvCancel = layoutView.findViewById(R.id.tvCancel)
        tvSure = layoutView.findViewById(R.id.tvSure)
        tvTitle = layoutView.findViewById(R.id.tvTitle)
        tvContent.text = content
        tvCancel.setOnClickListener { listener?.onCancelClick() }
        tvSure.setOnClickListener {
            listener?.onSureClick()
        }
    }

    fun setCancelText(text: String) {
        tvCancel.text = text
    }

    fun setSureText(text: String) {
        tvSure.text = text
    }

    fun setTitleVisibility(visibility: Int) {
        tvTitle.visibility = visibility
    }

    fun setCanceledOnTouchOut(outCancel: Boolean) {
        dialog?.setCanceledOnTouchOutside(outCancel)
    }

    fun setCancel(cancelAble: Boolean) {
        dialog?.setCancelable(cancelAble)
    }

    interface OnDialogSureClickListener {
        fun onSureClick()
        fun onCancelClick()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
    }

    private fun initWindow() {
        val window = dialog?.window
        window?.let {
            it.attributes.width = OsUtils.dip2px(requireContext(), width)
            it.attributes.gravity = Gravity.CENTER
            it.setBackgroundDrawableResource(R.drawable.shape_radius3_solid_ffffff)
        }
    }
}