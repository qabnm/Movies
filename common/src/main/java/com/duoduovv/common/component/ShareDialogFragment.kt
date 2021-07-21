package com.duoduovv.common.component

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.duoduovv.common.R
import com.duoduovv.common.databinding.DialogShareBinding
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.shareToQQ
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.shareToQQZone
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.weiChatCircle
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.weiChatFriend
import dc.android.bridge.EventContext

/**
 * @author: jun.liu
 * @date: 2021/4/20 9:39
 * @des:分享
 */
class ShareDialogFragment(private val listener: OnShareClickListener?) :
    DialogFragment() {
    private lateinit var mBind: DialogShareBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        mBind = DialogShareBinding.inflate(inflater, container, false)
        initViews()
        return mBind.root
    }

    private fun initViews() {
        mBind.tvCancel.setOnClickListener { dismiss() }
        mBind.layoutQQ.setOnClickListener {
            listener?.onQQShareClick(shareToQQ)
            EventContext.uMenEvent(EventContext.EVENT_SHARE_QQ,null)
        }
        mBind.layoutZone.setOnClickListener {
            listener?.onQQShareClick(shareToQQZone)
            EventContext.uMenEvent(EventContext.EVENT_SHARE_QQ_ZONE,null)
        }
        mBind.layoutCopy.setOnClickListener {
            listener?.onCopyClick()
            EventContext.uMenEvent(EventContext.EVENT_SHARE_COPY,null)
        }
        mBind.layoutWeiChat.setOnClickListener {
            listener?.onWeiChatClick(weiChatFriend)
            EventContext.uMenEvent(EventContext.EVENT_SHARE_WEICHAT,null)
        }
        mBind.layoutWeiChatCircle.setOnClickListener {
            listener?.onWeiChatClick(weiChatCircle)
            EventContext.uMenEvent(EventContext.EVENT_SHARE_WEI_CIELE,null)
        }
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
        fun onQQShareClick(flag: Int)
        fun onCopyClick()
        fun onWeiChatClick(flag: Int)
    }
}