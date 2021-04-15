package com.duoduovv.main.component

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.duoduovv.main.R
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
    private lateinit var tvCancel:TextView
    private lateinit var tvSure:TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutView = inflater.inflate(R.layout.dialog_permission, container, false)
        initViews(layoutView)
        return layoutView
    }

    private fun initViews(layoutView: View) {
        val tvContent:TextView = layoutView.findViewById(R.id.tvContent)
        tvCancel = layoutView.findViewById(R.id.tvCancel)
        tvSure = layoutView.findViewById(R.id.tvSure)
        tvContent.text = message
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

    override fun getPositiveButton() = tvSure

    override fun getNegativeButton() = tvCancel

    override fun getPermissionsToRequest() = deniedList
}