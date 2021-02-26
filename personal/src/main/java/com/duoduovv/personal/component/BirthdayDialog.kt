package com.duoduovv.personal.component

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.duoduovv.personal.R
import dc.android.bridge.util.OsUtils
import java.util.*

/**
 * @author: jun.liu
 * @date: 2021/2/23 9:41
 * @des:选择生日dialog
 */
class BirthdayDialog @JvmOverloads constructor(context: Context, def: Int = 0) :
    Dialog(context, def) {
    private val startDate = Calendar.getInstance()
    private val endDate = Calendar.getInstance()
    private var listener: OnTimeSelectListener? = null
    private val year = Calendar.getInstance().get(Calendar.YEAR)
    private val month = Calendar.getInstance().get(Calendar.MONTH)
    private val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private lateinit var pvTime: TimePickerView
    private val date = Calendar.getInstance()

    init {
        startDate.set(year - 60, 0, 1)
        endDate.set(Calendar.getInstance().get(Calendar.YEAR), month, day)
        date.set(year - 18, 0, 1)
    }

    fun showTimeDialog() {
        pvTime = TimePickerBuilder(context) { date, _ ->
            listener?.onTimeSelect(date)
        }.setType(booleanArrayOf(true, true, true, false, false, false))//分别对应年月日时分秒，默认全部显示
            .setItemVisibleCount(3)
            .setOutSideCancelable(true)
            .isCyclic(false)
            .setRangDate(startDate, endDate)
            .setLineSpacingMultiplier(3.3f)
            .setLabel("年", "月", "日", "", "", "")
            .isDialog(true)
            .setDate(date)
            .setLayoutRes(R.layout.dialog_birthday) { view ->
                val tvCancel: TextView = view.findViewById(R.id.tvCancel)
                val tvSure: TextView = view.findViewById(R.id.tvSure)
                tvCancel.setOnClickListener { pvTime.dismiss() }
                tvSure.setOnClickListener {
                    pvTime.returnData()
                    pvTime.dismiss()
                }
            }
            .build()
        val dialog = pvTime.dialog
        if (null != dialog) {
            val params = dialog.window!!.attributes
            params.gravity = Gravity.CENTER
            params.width = OsUtils.getScreenWidth(context) - OsUtils.dip2px(context, 75f)
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window?.setBackgroundDrawableResource(R.drawable.shape_radius3_solid_ffffff)
            dialog.window?.attributes = params
            pvTime.show()
        }
    }

    fun setOnTimeSelectListener(listener: OnTimeSelectListener) {
        this.listener = listener
    }

    interface OnTimeSelectListener {
        fun onTimeSelect(date: Date)
    }
}