package com.duoduovv.main.component

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.duoduovv.advert.gdtad.GDTInfoAd
import com.duoduovv.advert.ttad.TTInfoAd
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.R
import com.duoduovv.main.databinding.DialogLogoutBinding
import dc.android.bridge.BridgeContext.Companion.TYPE_GDT_AD
import dc.android.bridge.BridgeContext.Companion.TYPE_TT_AD
import dc.android.bridge.util.OsUtils

/**
 * @author: jun.liu
 * @date: 2021/5/17 17:50
 * @des:首页退出广告位
 */
class LogoutDialogFragment(private val listener: OnLogoutSureClickListener?) : DialogFragment() {
    private lateinit var mBind: DialogLogoutBinding
    private var gdtInfoAd: GDTInfoAd? = null
    private var ttInfoAd:TTInfoAd?= null

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
        mBind.tvSure.setOnClickListener {
            gdtInfoAd?.destroyInfoAd()
            ttInfoAd?.destroyInfoAd()
            dismiss()
        }
        mBind.tvCancel.setOnClickListener {
            gdtInfoAd?.destroyInfoAd()
            ttInfoAd?.destroyInfoAd()
            listener?.onLogSureClick()
        }
        BaseApplication.configBean?.let { it ->
            it.ad?.let {
                when(it.logout?.type){
                    TYPE_TT_AD ->{ initTTAd(it.logout!!.value) }
                    TYPE_GDT_AD -> { initGDTAd(it.logout!!.value) }
                }
            }
        }
    }

    /**
     * 请求穿山甲广告
     */
    private fun initTTAd(posId:String){
        ttInfoAd = TTInfoAd()
        ttInfoAd?.initTTInfoAd(requireActivity(),posId,226f,0f,mBind.layoutContainer)
    }

    /**
     * 请求广点通的信息流广告
     */
    private fun initGDTAd(posId: String) {
        gdtInfoAd = GDTInfoAd()
        gdtInfoAd?.initInfoAd(
            requireActivity(),
            posId,
            mBind.layoutContainer,
            270,
            105
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
    }

    private var dialogWidth = 0
    private fun initWindow() {
        val window = dialog?.window
        window?.let {
            dialogWidth = OsUtils.dip2px(requireContext(), 246f)
            it.attributes.width = dialogWidth
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