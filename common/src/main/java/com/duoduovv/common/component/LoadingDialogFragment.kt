package com.duoduovv.common.component

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.duoduovv.common.R
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.util.OsUtils
import kotlinx.android.synthetic.main.dialog_loading.*

/**
 * @author: jun.liu
 * @date: 2021/3/31 11:24
 * @des:
 */
class LoadingDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_loading, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
    }

    private fun initWindow() {
        val window = dialog?.window
        window?.let {
            it.attributes.width = OsUtils.dip2px(requireContext(), 80f)
            it.attributes.gravity = Gravity.CENTER
            it.setDimAmount(0.0f)
            it.setBackgroundDrawableResource(android.R.color.transparent)
        }
        dialog?.setCanceledOnTouchOutside(false)
    }
}