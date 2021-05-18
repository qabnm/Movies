package com.duoduovv.main.component

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.duoduovv.advert.gdtad.GDTInfoAdByImg
import com.duoduovv.common.R
import com.duoduovv.main.databinding.DialogLogoutBinding
import dc.android.bridge.util.OsUtils

/**
 * @author: jun.liu
 * @date: 2021/5/17 17:50
 * @des:首页退出广告位
 */
class LogoutDialogFragment(private val listener: OnLogoutSureClickListener?) : DialogFragment() {
    private lateinit var mBind: DialogLogoutBinding
    private var gdtInfoAd: GDTInfoAdByImg? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = DialogLogoutBinding.inflate(inflater, container, false)
        initViews()
        return mBind.root
    }

    private fun initViews() {
        mBind.tvCancel.setOnClickListener {
            gdtInfoAd?.destroyInfoAd()
            dismiss()
        }
        mBind.tvSure.setOnClickListener { listener?.onLogSureClick() }
        initGDTAd()
    }

    /**
     * 请求广点通的信息流广告
     */
    private fun initGDTAd() {
        gdtInfoAd = GDTInfoAdByImg()
        gdtInfoAd?.initInfoAd(
            requireActivity(),
            "7021380766691974",
            mBind.layoutContainer,
            234,
            105
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
    }

    private fun initWindow() {
        val window = dialog?.window
        window?.let {
            it.attributes.width = OsUtils.dip2px(requireContext(), 246f)
            it.attributes.gravity = Gravity.CENTER
            it.setBackgroundDrawableResource(R.drawable.shape_radius3_solid_ffffff)
        }
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    interface OnLogoutSureClickListener {
        fun onLogSureClick()
    }
}