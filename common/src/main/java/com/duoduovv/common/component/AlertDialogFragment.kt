package com.duoduovv.common.component

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.duoduovv.common.R
import com.duoduovv.common.databinding.DialogAlertBinding
import dc.android.bridge.util.OsUtils

/**
 * @author: jun.liu
 * @date: 2021/3/23 11:04
 * @des:警示弹窗
 */
class AlertDialogFragment(
    private val content: String,
    private val width: Float = 300f,
    private var listener: OnDialogSureClickListener? = null
) : DialogFragment() {
    private lateinit var mBind: DialogAlertBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = DialogAlertBinding.inflate(inflater, container, false)
        initViews()
        return mBind.root
    }

    private fun initViews() {
        mBind.tvContent.text = content
        mBind.tvCancel.setOnClickListener { listener?.onCancelClick() }
        mBind.tvSure.setOnClickListener { listener?.onSureClick() }
    }

    /**
     * 设置取消按钮的文字
     * @param text String
     */
    fun setCancelText(text: String) {
        mBind.tvCancel.text = text
    }

    /**
     * 设置确认按钮的文字
     * @param text String
     */
    fun setSureText(text: String) {
        mBind.tvSure.text = text
    }

    /**
     * 是否显示标题
     * @param visibility Int
     */
    fun setTitleVisibility(visibility: Int) {
        mBind.tvTitle.visibility = visibility
    }

    /**
     * 是否允许外部触摸取消dialog
     * @param outCancel Boolean
     */
    fun setCanceledOnTouchOut(outCancel: Boolean) {
        dialog?.setCanceledOnTouchOutside(outCancel)
    }

    /**
     * 是否可以取消dialog
     * @param cancelAble Boolean
     */
    fun setCancel(cancelAble: Boolean) {
        dialog?.setCancelable(cancelAble)
    }

    interface OnDialogSureClickListener {
        fun onSureClick()
        fun onCancelClick()
    }

    fun setDialogClickListener(listener: OnDialogSureClickListener?) {
        this.listener = listener
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