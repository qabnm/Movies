package com.duoduovv.main.component

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.duoduovv.common.R
import com.duoduovv.main.databinding.DialogLogoutBinding
import com.qq.e.ads.nativ.express2.AdEventListener
import com.qq.e.ads.nativ.express2.NativeExpressAD2
import com.qq.e.ads.nativ.express2.NativeExpressADData2
import com.qq.e.comm.util.AdError
import dc.android.bridge.util.OsUtils

/**
 * @author: jun.liu
 * @date: 2021/5/17 17:50
 * @des:首页退出广告位
 */
class LogoutDialogFragment(private val listener: OnLogoutSureClickListener?) : DialogFragment() {
    private lateinit var mBind: DialogLogoutBinding
    private var mNativeExpressAD: NativeExpressAD2? = null
    private var nativeExpressADData: NativeExpressADData2? = null
    private val TAG = "AD_DEMO"

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
            nativeExpressADData?.destroy()
            dismiss()
        }
        mBind.tvSure.setOnClickListener { listener?.onLogSureClick() }
        initGDTAd()
    }

    /**
     * 请求广点通的信息流广告
     */
    private fun initGDTAd() {
        mNativeExpressAD = NativeExpressAD2(
            requireActivity(),
            "7021380766691974",
            object : NativeExpressAD2.AdLoadListener {
                override fun onNoAD(error: AdError?) {
                    Log.d(TAG, "onAdError${error?.errorCode}${error?.errorMsg}")
                }

                override fun onLoadSuccess(adDataList: MutableList<NativeExpressADData2>?) {
                    Log.d(TAG, "onLoadSuccess")
                    if (adDataList?.isNotEmpty() == true) {
                        mBind.layoutContainer.removeAllViews()
                        nativeExpressADData = adDataList[0]
                        nativeExpressADData?.setAdEventListener(object : AdEventListener {
                            override fun onClick() {
                                Log.d(TAG, "onClick")
                            }

                            override fun onExposed() {
                                Log.d(TAG, "onExposed")
                            }

                            override fun onRenderSuccess() {
                                Log.d(TAG, "onRenderSuccess")
                                mBind.layoutContainer.visibility = View.VISIBLE
                                mBind.layoutContainer.removeAllViews()
                                nativeExpressADData?.adView?.let {
                                    mBind.layoutContainer.addView(
                                        nativeExpressADData?.adView
                                    )
                                }
                            }

                            override fun onRenderFail() {
                                Log.d(TAG, "onRenderFail")
                            }

                            override fun onAdClosed() {
                                Log.d(TAG, "onAdClosed")
                                mBind.layoutContainer.removeAllViews()
                                nativeExpressADData?.destroy()
                            }
                        })
                        nativeExpressADData?.render()
                    }
                }
            })
        //这里的单位是dp
        mNativeExpressAD?.setAdSize(234, 105)
        mNativeExpressAD?.loadAd(1)
        nativeExpressADData?.destroy()
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