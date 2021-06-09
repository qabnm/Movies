package com.duoduovv.main.component

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.util.RouterPath
import com.duoduovv.main.R
import com.duoduovv.main.databinding.DialogPrivacyBinding
import dc.android.bridge.BridgeContext
import dc.android.bridge.util.OsUtils

/**
 * @author: jun.liu
 * @date: 2021/3/25 17:37
 * @des:隐私政策的一些弹窗
 */
class PrivacyDialogFragment(private val listener: OnDialogBtnClickListener?) : DialogFragment() {
    private lateinit var mBind: DialogPrivacyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = DialogPrivacyBinding.inflate(inflater, container, false)
        initViews()
        return mBind.root
    }

    private fun initViews() {
        val spannableString = SpannableString(
            "感谢您选择多多影视大全App！我们非常重视您的个人信息和隐私保护。为了更好的保障您的个人权益，" +
                    "在您使用我们的产品前，请务必审慎阅读《多多影视大全用户协议》和《多多影视大全隐私政策》内的所有条款，尤其是：" +
                    "\n1、我们对您的个人信息的收集/保存/使用/保护等规则条款；\n2、约定我们的限制责任，免责条款；\n3、其他以颜色加粗进行标识的重要条款。" +
                    "\n\n如您点击”同意”的行为即表示您已阅读完毕并同意以上协议的全部内容。\n如您同意以上协议内容，请点击“同意”，开始使用我们的产品和服务！"
        )
        spannableString.apply {
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color557CE7
                    )
                ), 65, 77, Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color557CE7
                    )
                ), 78, 90, Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            setSpan(
                BackgroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorFFFFFF
                    )
                ), 65, 77, Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            setSpan(
                BackgroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorFFFFFF
                    )
                ), 78, 90, Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    toWebActivity("用户协议", BridgeContext.URL_USER_AGREEMENT)
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = ContextCompat.getColor(requireContext(), R.color.color557CE7)
                    ds.isUnderlineText = false
                }
            }, 65, 77, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

            setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    toWebActivity("隐私政策", BridgeContext.URL_PRIVACY)
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = ContextCompat.getColor(requireContext(), R.color.color557CE7)
                    ds.isUnderlineText = false
                }
            }, 78, 90, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            mBind.tvContent.text = this
            mBind.tvContent.movementMethod = LinkMovementMethod.getInstance()
        }

        mBind.tvCancel.setOnClickListener { listener?.onDialogCancelClick() }
        mBind.tvSure.setOnClickListener { listener?.onDialogSureClick() }
    }

    /**
     * 跳转H5页面
     * @param title String
     * @param url String
     */
    private fun toWebActivity(title: String, url: String) {
        ARouter.getInstance().build(RouterPath.PATH_WEB_VIEW).withString(BridgeContext.TITLE, title)
            .withString(BridgeContext.URL, url).navigation()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
    }

    private fun initWindow() {
        val window = dialog?.window
        window?.let {
            it.attributes.width = OsUtils.dip2px(requireContext(), 300f)
            it.attributes.gravity = Gravity.CENTER
            it.setBackgroundDrawableResource(R.drawable.shape_radius3_solid_ffffff)
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
    }

    interface OnDialogBtnClickListener {
        fun onDialogCancelClick()
        fun onDialogSureClick()
    }
}