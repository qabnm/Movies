package com.duoduovv.main.component

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.duoduovv.advert.gdtad.GDTInfoAdForSelfRender
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
class LogoutDialogFragment: DialogFragment() {
    private lateinit var mBind: DialogLogoutBinding
    private var gdtInfoAd: GDTInfoAdForSelfRender? = null
    private var ttInfoAd:TTInfoAd?= null
    private var listener: OnLogoutSureClickListener? = null

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
            gdtInfoAd?.onDestroy()
            ttInfoAd?.destroyInfoAd()
            dismiss()
        }
        mBind.tvCancel.setOnClickListener {
            gdtInfoAd?.onDestroy()
            ttInfoAd?.destroyInfoAd()
            listener?.onLogSureClick()
        }
        BaseApplication.configBean?.ad?.logout?.let {
            when(it.type){
                TYPE_TT_AD ->{ initTTAd(it.value) }
                TYPE_GDT_AD -> {
                    mBind.layoutTTAd.visibility = View.GONE
                    mBind.layoutGdt.visibility = View.VISIBLE
                    initGDTAd(it.value)
                }
            }
        }
    }

    /**
     * 请求穿山甲广告
     */
    private fun initTTAd(posId:String){
        ttInfoAd = TTInfoAd()
        ttInfoAd?.initTTInfoAd(requireActivity(),posId,246f,0f,mBind.layoutTTAd)
    }

    /**
     * 请求广点通的信息流广告
     */
    private fun initGDTAd(posId: String) {
        gdtInfoAd = GDTInfoAdForSelfRender()
        gdtInfoAd?.initInfoAd(
            requireActivity(),
            posId,
            mBind.adImgCover,
            mBind.mediaView,
            mBind.layoutGdt
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

    fun setLogoutSureClickListener(listener: OnLogoutSureClickListener){
        this.listener = listener
    }

    interface OnLogoutSureClickListener {
        fun onLogSureClick()
    }
}