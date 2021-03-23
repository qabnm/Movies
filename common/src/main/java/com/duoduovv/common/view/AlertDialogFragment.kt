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
    private val listener: OnDialogSureClickListener?
) : DialogFragment() {
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
        val tvCancel: TextView = layoutView.findViewById(R.id.tvCancel)
        val tvSure: TextView = layoutView.findViewById(R.id.tvSure)
        tvContent.text = content
        tvCancel.setOnClickListener { this.dismiss() }
        tvSure.setOnClickListener {
            listener?.onSureClick()
            this.dismiss()
        }
    }

    interface OnDialogSureClickListener {
        fun onSureClick()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
    }

    private fun initWindow() {
        val window = dialog?.window
        window?.let {
            it.attributes.width = OsUtils.dip2px(requireContext(), 300f)
            it.attributes.gravity = Gravity.CENTER
        }
    }
}