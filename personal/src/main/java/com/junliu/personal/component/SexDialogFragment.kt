package com.junliu.personal.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import com.junliu.personal.R
import com.junliu.personal.listener.ISelectSexListener

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