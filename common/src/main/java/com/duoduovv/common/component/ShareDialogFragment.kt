package com.duoduovv.common.component

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.duoduovv.common.R
import com.duoduovv.weichat.WeiChatBridgeContext

/**
 * @author: jun.liu
 * @date: 2021/4/20 9:39
 * @des:分享
 */
class ShareDialogFragment(private val listener: OnShareClickListener?) :
    DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val layoutView = inflater.inflate(R.layout.dialog_share, container, false)
        initViews(layoutView)
        return layoutView
    }

    private fun initViews(layoutView: View) {
        val layoutQQ:LinearLayout = layoutView.findViewById(R.id.layoutQQ)
        val layoutZone:LinearLayout = layoutView.findViewById(R.id.layoutZone)
        val layoutCopy:LinearLayout = layoutView.findViewById(R.id.layoutCopy)
        val tvCancel:TextView = layoutView.findViewById(R.id.tvCancel)
        tvCancel.setOnClickListener { dismiss() }
        layoutQQ.setOnClickListener { listener?.onQQShareClick(WeiChatBridgeContext.shareToQQ) }
        layoutZone.setOnClickListener { listener?.onQQShareClick(WeiChatBridgeContext.shareToQQZone) }
        layoutCopy.setOnClickListener { listener?.onCopyClick() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
    }

    /**
     * 设置dialog宽高
     */
    private fun initWindow() {
        val window = dialog?.window
        window?.let {
            it.decorView.setPadding(0, 0, 0, 0)
            it.attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            it.attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
            it.attributes.gravity = Gravity.BOTTOM
            it.setBackgroundDrawableResource(R.color.colorFFFFFF)
            it.attributes.windowAnimations = R.style.BottomToTopAnim
        }
    }

    interface OnShareClickListener {
        fun onQQShareClick(flag:Int)
        fun onCopyClick()
    }
}