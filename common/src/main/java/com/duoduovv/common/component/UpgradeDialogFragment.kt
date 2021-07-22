package com.duoduovv.common.component

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.duoduovv.common.databinding.DialogUpgradeBinding
import dc.android.bridge.util.OsUtils

/**
 * @author: jun.liu
 * @date: 2021/3/4 14:04
 * @des:升级弹窗
 */
class UpgradeDialogFragment() : DialogFragment() {
    private var upgradeClickListener: OnUpgradeClickListener? = null
    private lateinit var mBind: DialogUpgradeBinding
    private var isForce = ""
    private var upgradeContent = ""
    private var downloadUrl = ""

    constructor(isForce: String, upgradeContent: String, downloadUrl: String) : this() {
        this.isForce = isForce
        this.upgradeContent = upgradeContent
        this.downloadUrl = downloadUrl
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = DialogUpgradeBinding.inflate(inflater, container, false)
        initViews()
        return mBind.root
    }

    private fun initViews() {
        if (isForce == "1") {
            mBind.tvCancel.visibility = View.GONE
        } else {
            mBind.tvCancel.visibility = View.VISIBLE
            mBind.tvCancel.setOnClickListener { dismiss() }
        }
        mBind.tvContent.text = upgradeContent
        mBind.btnUpgrade.setOnClickListener { upgradeClickListener?.onUpgradeClick(downloadUrl) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
    }

    fun setOnUpgradeClickListener(upgradeClickListener: OnUpgradeClickListener?) {
        this.upgradeClickListener = upgradeClickListener
    }

    fun onProgressUpdate(progress: Int) {
        mBind.btnUpgrade.text = "下载中${progress}%"
        mBind.btnUpgrade.isEnabled = false
        if (progress == 100) dismiss()
    }

    interface OnUpgradeClickListener {
        fun onUpgradeClick(url: String)
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