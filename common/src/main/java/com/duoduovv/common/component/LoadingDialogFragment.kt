package com.duoduovv.common.component

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieAnimationView
import com.duoduovv.common.R
import dc.android.bridge.util.OsUtils

/**
 * @author: jun.liu
 * @date: 2021/3/31 11:24
 * @des:loading弹窗
 */
class LoadingDialogFragment : DialogFragment() {
    private var lottieView: LottieAnimationView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_loading, container, false)
        lottieView = view.findViewById(R.id.lottieView)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return NoLeakDialog(requireContext(),theme)
    }

    private fun initWindow() {
        val window = dialog?.window
        window?.let {
            it.attributes.width = OsUtils.dip2px(requireContext(), 60f)
            it.attributes.gravity = Gravity.CENTER
            it.setDimAmount(0.0f)
            it.setBackgroundDrawableResource(android.R.color.transparent)
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
    }

    override fun onDestroyView() {
        clearAnimation()
        super.onDestroyView()
    }

    fun clearAnimation() {
        lottieView?.let {
            it.clearAnimation()
            it.removeAllAnimatorListeners()
            it.removeAllUpdateListeners()
        }
    }
}