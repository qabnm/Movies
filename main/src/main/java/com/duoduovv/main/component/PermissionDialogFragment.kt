package com.duoduovv.main.component

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.duoduovv.main.R
import com.duoduovv.main.databinding.DialogPermissionBinding
import com.permissionx.guolindev.dialog.RationaleDialogFragment
import dc.android.bridge.util.OsUtils

/**
 * @author: jun.liu
 * @date: 2021/4/12 14:23
 * @des:
 */
class PermissionDialogFragment(
    private val deniedList: MutableList<String>,
    private val message: String
) : RationaleDialogFragment() {
    private lateinit var mBind: DialogPermissionBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = DialogPermissionBinding.inflate(inflater, container, false)
        initViews()
        return mBind.root
    }

    private fun initViews() {
        mBind.tvContent.text = message
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
    }

    private fun initWindow() {
        val window = dialog?.window
        window?.let {
            it.attributes.width = OsUtils.dip2px(requireContext(), 260f)
            it.attributes.gravity = Gravity.CENTER
            it.setBackgroundDrawableResource(R.drawable.shape_radius3_solid_ffffff)
        }
    }

    override fun getPositiveButton() = mBind.tvSure

    override fun getNegativeButton() = mBind.tvCancel

    override fun getPermissionsToRequest() = deniedList
}