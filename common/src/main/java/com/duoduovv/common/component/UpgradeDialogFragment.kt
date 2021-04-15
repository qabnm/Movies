package com.duoduovv.common.component

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.duoduovv.common.R
import dc.android.bridge.util.OsUtils

/**
 * @author: jun.liu
 * @date: 2021/3/4 14:04
 * @des:升级弹窗
 */
class UpgradeDialogFragment(private val isForce:Int,private val upgradeContent:String,private val downloadUrl:String) : DialogFragment() {
    private lateinit var btnUpgrade: Button
    private var upgradeClickListener: OnUpgradeClickListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_upgrade, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        val tvContent: TextView = view.findViewById(R.id.tvContent)
        btnUpgrade = view.findViewById(R.id.btnUpgrade)
        val tvCancel: TextView = view.findViewById(R.id.tvCancel)
        if (isForce == 1) {
            tvCancel.visibility = View.GONE
        } else {
            tvCancel.visibility = View.VISIBLE
            tvCancel.setOnClickListener { dismiss() }
        }
        tvContent.text = upgradeContent
        btnUpgrade.setOnClickListener { upgradeClickListener?.onUpgradeClick(downloadUrl) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
    }

    fun setOnUpgradeClickListener(upgradeClickListener: OnUpgradeClickListener?) {
        this.upgradeClickListener = upgradeClickListener
    }

    fun onProgressUpdate(progress: Int) {
        btnUpgrade.text = "下载中${progress}%"
        btnUpgrade.isEnabled = false
        if (progress == 100) dismiss()
    }

    interface OnUpgradeClickListener {
        fun onUpgradeClick(url:String)
    }

    private fun initWindow() {
        val window = dialog?.window
        window?.let {
            it.attributes.width =
                OsUtils.getScreenWidth(requireContext()) - OsUtils.dip2px(requireContext(), 103f)
            it.attributes.gravity = Gravity.CENTER
            it.setBackgroundDrawableResource(android.R.color.transparent)
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)

    }
}