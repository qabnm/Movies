package com.duoduovv.common.component

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.duoduovv.advert.ttad.TTInsertAd
import com.duoduovv.common.databinding.DialogAdInsertBinding
import dc.android.bridge.util.OsUtils

/**
 * @author: jun.liu
 * @date: 2021/6/21 16:09
 * @des:插屏广告
 */
class InsertAdDialogFragment(private val width: Float,private val height:Float,private val posId:String) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = DialogAdInsertBinding.inflate(inflater, container, false)
        initView()
        return mBind.root
    }

    private lateinit var mBind: DialogAdInsertBinding
    private var ttAd: TTInsertAd? = null

    private fun initView() {
        ttAd = TTInsertAd()
        ttAd?.initInsertAd(requireActivity(), posId,width,height,mBind.adContainer)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        ttAd?.onDestroy()
    }

    private fun initWindow() {
        val window = dialog?.window
        window?.let {
            it.attributes.width = OsUtils.dip2px(requireContext(), width)
            it.attributes.gravity = Gravity.CENTER
            it.setDimAmount(0f)
        }
    }
}