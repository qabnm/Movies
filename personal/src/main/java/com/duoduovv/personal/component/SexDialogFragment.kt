package com.duoduovv.personal.component

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import com.duoduovv.personal.R
import com.duoduovv.personal.listener.ISelectSexListener
import dc.android.bridge.util.OsUtils

/**
 * @author: jun.liu
 * @date: 2021/2/23 11:07
 * @des:选择性别
 */
class SexDialogFragment(private val listener:ISelectSexListener?):DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_select_sex,container,false)
        initViews(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
    }

    private fun initWindow() {
        val window = dialog?.window
        window?.let {
            it.attributes.width =
                OsUtils.getScreenWidth(requireContext()) - OsUtils.dip2px(requireContext(), 75f)
            it.attributes.gravity = Gravity.CENTER
        }
    }

    private fun initViews(view: View) {
        val layoutMan :LinearLayout = view.findViewById(R.id.layoutMan)
        val layoutWoman:LinearLayout = view.findViewById(R.id.layoutWoman)
        val checkMan:RadioButton = view.findViewById(R.id.checkMan)
        val checkWoman:RadioButton = view.findViewById(R.id.checkWoman)
        layoutMan.setOnClickListener {
            checkMan.isChecked = true
            listener?.selectSex("男")
            dismiss()
        }
        layoutWoman.setOnClickListener {
            checkWoman.isChecked = true
            listener?.selectSex("女")
            dismiss()
        }
    }
}