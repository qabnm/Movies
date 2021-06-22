package com.duoduovv.personal.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI
import com.duoduovv.advert.gdtad.GDTInfoAdForSelfRender
import com.duoduovv.advert.ttad.TTInfoAd
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.component.ShareDialogFragment
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.RouterPath.Companion.PATH_EDIT_MATERIALS
import com.duoduovv.common.util.RouterPath.Companion.PATH_PERSONAL
import com.duoduovv.common.util.RouterPath.Companion.PATH_SETTING_ACTIVITY
import com.duoduovv.common.util.SharedPreferencesHelper
import com.duoduovv.personal.R
import com.duoduovv.personal.bean.*
import com.duoduovv.personal.databinding.FragmentPersonalBinding
import com.duoduovv.personal.viewmodel.WeiChatViewModel
import com.duoduovv.tent.TentLoginListener
import com.duoduovv.tent.TentUserInfo
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.SHARE_CONTENT
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.SHARE_LINK
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.SHARE_TITLE
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.accessTokenUrl
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.accessTokenValidUrl
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.refreshTokenUrl
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.weiChatAppId
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.weiChatSecret
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.weiChatUserInfoUrl
import com.duoduovv.weichat.WeiChatTool
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.BridgeContext.Companion.TOKEN
import dc.android.bridge.BridgeContext.Companion.TYPE_GDT_AD
import dc.android.bridge.BridgeContext.Companion.TYPE_TT_AD
import dc.android.bridge.BridgeContext.Companion.WAY
import dc.android.bridge.BridgeContext.Companion.WAY_VERIFY
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.OsUtils
import dc.android.bridge.util.StringUtils
import dc.android.bridge.view.BaseViewModelFragment
import dc.android.tools.LiveDataBus
import java.util.*

/**
 * @author: jun.liu
 * @date: 2020/12/29 : 14:49
 * 个人中心
 */
@Route(path = PATH_PERSONAL)
class PersonalFragment : BaseViewModelFragment<WeiChatViewModel>() {
    override fun getLayoutId() = R.layout.fragment_personal
    override fun providerVMClass() = WeiChatViewModel::class.java
    private lateinit var mBind: FragmentPersonalBinding
    private var ttAd: TTInfoAd? = null
    private var gdtAd: GDTInfoAdForSelfRender? = null
    private var width = 0f

    override fun initView() {
        mBind = baseBinding as FragmentPersonalBinding
        if (SharedPreferencesHelper.helper.getValue(WAY, "") != WAY_VERIFY) {
            //正式版
            mBind.layoutIsRes.visibility = View.VISIBLE
            mBind.layoutHistory.setOnClickListener {
                ARouter.getInstance().build(RouterPath.PATH_WATCH_HISTORY).navigation()
            }
            mBind.layoutDownload.setOnClickListener { }
            mBind.layoutCollection.setOnClickListener {
                ARouter.getInstance().build(RouterPath.PATH_MY_COLLECTION).navigation()
            }
            mBind.layoutShare.setOnClickListener { onShareClick() }
            mBind.layoutContainer.visibility = View.VISIBLE
            mBind.vLine.visibility = View.VISIBLE
        } else {
            //审核版
            mBind.layoutIsRes.visibility = View.GONE
            mBind.layoutContainer.visibility = View.GONE
            mBind.vLine.visibility = View.GONE
        }
        mBind.layoutContract.setOnClickListener {
            //问题反馈
            FeedbackAPI.openFeedbackActivity()
        }

        mBind.layoutSetting.setOnClickListener {
            ARouter.getInstance().build(PATH_SETTING_ACTIVITY).navigation()
        }
        mBind.layoutAbout.setOnClickListener {
            ARouter.getInstance().build(RouterPath.PATH_ABOUT_US).navigation()
        }
        mBind.layoutTop.setOnClickListener {
            ARouter.getInstance().build(PATH_EDIT_MATERIALS).navigation()
        }
        mBind.imgWeiChat.setOnClickListener { weiChatLogin() }
        mBind.imgQQ.setOnClickListener { qqLogin() }
        viewModel.getUserInfo().observe(this, { onGetUserInfoSuc(viewModel.getUserInfo().value) })
        LiveDataBus.get().with("logout", Int::class.java).observe(this, {
            if (it == SUCCESS) {
                WeiChatTool.mTenCent?.logout(requireContext())
                viewModel.userInfo()
            }
        })
        width = OsUtils.px2dip(requireContext(),OsUtils.getScreenWidth(requireContext()).toFloat()).toFloat()

//        //以下是小程序测试
//        LiveDataBus.get().with("extraData",String::class.java).observe(this,{
//            if (it == "appCallBack"){
//                ARouter.getInstance().build(RouterPath.PATH_ABOUT_US).navigation()
//            }
//        })
    }
    private var isHide = true
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d("hidden","$hidden")
        isHide = hidden
        if (!hidden) initAD()
    }

    override fun onResume() {
        super.onResume()
        Log.d("hidden","onResume")
        if (!isHide) initAD()
        gdtAd?.onResume()
    }

    private fun setFeedbackUi() {
        FeedbackAPI.setBackIcon(R.drawable.back)
        FeedbackAPI.setTranslucent(true)
    }

    /**
     * 成功获取了用户信息
     * @param value User?
     */
    private fun onGetUserInfoSuc(value: User?) {
        value?.let {
            mBind.layoutLogin.visibility = View.GONE
            mBind.layoutTop.visibility = View.VISIBLE
            GlideUtils.setImg(requireActivity(), it.imgUrl, mBind.imageIcon)
            mBind.tvUser.text = it.nickName
        }
    }

    override fun initData() {
        //正式版才请求登录接口
        if (SharedPreferencesHelper.helper.getValue(WAY, "") != WAY_VERIFY) {
            viewModel.userInfo()
        }
        setFeedbackUi()
        initAD()
    }

    private fun initAD(){
        BaseApplication.configBean?.let { it ->
            it.ad?.let {
                when(it.centerTop?.type){
                    TYPE_TT_AD ->{
                        initTTAd(it.centerTop!!.value)
                    }
                    TYPE_GDT_AD ->{
                        mBind.layoutTTAd.visibility = View.GONE
                        mBind.layoutGdt.visibility = View.VISIBLE
                        initGDTAd(it.centerTop!!.value)
                    }
                }
            }
        }
    }

    /**
     * 初始化穿山甲广告
     * @param posId String
     */
    private fun initTTAd(posId: String) {
        if (null == ttAd ){
            ttAd = TTInfoAd()
        }else{ ttAd?.destroyInfoAd() }
        ttAd?.initTTInfoAd(requireActivity(), posId, width, 0f, mBind.layoutTTAd)
    }

    private fun initGDTAd(posId: String) {
        if (null == gdtAd) {
            gdtAd = GDTInfoAdForSelfRender()
        }else{
            gdtAd?.onDestroy()
        }
        gdtAd?.initInfoAd(requireActivity(), posId, mBind.adImgCover, mBind.mediaView, mBind.layoutGdt)
    }

    override fun onDestroy() {
        super.onDestroy()
        ttAd?.destroyInfoAd()
        gdtAd?.onDestroy()
    }

    /**
     * QQ登录授权成功
     * @param userInfo TentUserInfo?
     */
    private fun qqAuthSuccess(userInfo: TentUserInfo?) {
        userInfo?.let {
            viewModel.login(1, it.openId, it.nickName, it.sex, it.headerUrl)
        }
    }

    /**
     * 登录成功
     * @param bean LoginBean?
     */
    private fun loginSuccess(bean: LoginBean?) {
        bean?.let {
            if (!StringUtils.isEmpty(it.token)) {
                //登录成功了
                SharedPreferencesHelper.helper.setValue(TOKEN, it.token)
                mBind.layoutLogin.visibility = View.GONE
                mBind.layoutTop.visibility = View.VISIBLE
                GlideUtils.setImg(requireActivity(), it.img, mBind.imageIcon)
                mBind.tvUser.text = it.nickName
            } else {
                //登录失败
            }
        }
    }

    /**
     * 获取微信用户信息
     * @param infoBean WeiChatUserInfoBean?
     */
    private fun setUserInfo(infoBean: WeiChatUserInfoBean?) {
        infoBean?.let {
            viewModel.login(2, it.openId, it.nickName, it.sex, it.imgUrl ?: "", it.unionId)
        }
    }

    /**
     * 获取用户信息
     * @param bean AccessTokenValidBean?
     */
    private fun accessTokenValid(bean: AccessTokenValidBean?) {
        bean?.let {
            if (it.errCode == 0) {
                //如果accessToken有效  获取用户信息
                viewModel.weiCharUserInfo(
                    url = weiChatUserInfoUrl,
                    accessToken = it.accessToken,
                    openId = it.openId
                )
            }
        }
    }

    /**
     * 成功获取了accessToken
     * @param bean AccessTokenBean
     */
    private fun accessToken(bean: AccessTokenBean?) {
        //刷新token
        bean?.let {
            viewModel.refreshToken(
                url = refreshTokenUrl,
                appId = weiChatAppId,
                token = it.refreshToken
            )
        }
    }

    /**
     * 成功的刷新了token
     * @param bean RefreshTokenBean
     */
    private fun refreshToken(bean: RefreshTokenBean?) {
        bean?.let {
            //检验accessToken是否有效
            viewModel.accessTokenValid(
                url = accessTokenValidUrl,
                accessToken = it.accessToken,
                openId = it.openId
            )
        }
    }

    /**
     * 微信登录
     */
    private fun weiChatLogin() {
        WeiChatTool.regToWx(BaseApplication.baseCtx)
        WeiChatTool.weiChatLogin(requireContext())
    }

    /**
     * QQ登录
     */
    private fun qqLogin() {
        WeiChatTool.regToQQ(BaseApplication.baseCtx)
        WeiChatTool.loginListener = TentLoginListener(requireActivity())
        WeiChatTool.qqLogin(requireActivity(), WeiChatTool.loginListener!!)
    }

    private var hasObserve = false

    /**
     * token过期
     */
    override fun tokenValid() {
        mBind.layoutLogin.visibility = View.VISIBLE
        mBind.layoutTop.visibility = View.GONE

        if (!hasObserve) {
            hasObserve = true
            LiveDataBus.get().with("wxCode", String::class.java).observe(this, {
                //请求微信的accessToken
                viewModel.accessToken(
                    url = accessTokenUrl,
                    appId = weiChatAppId,
                    secret = weiChatSecret,
                    code = it
                )
            })
            viewModel.getAccessToken()
                .observe(this, { accessToken(viewModel.getAccessToken().value) })
            viewModel.getRefreshToken()
                .observe(this, { refreshToken(viewModel.getRefreshToken().value) })
            viewModel.getAccessTokenValid()
                .observe(this, { accessTokenValid(viewModel.getAccessTokenValid().value) })
            viewModel.getWeiChatUseInfo()
                .observe(this, { setUserInfo(viewModel.getWeiChatUseInfo().value) })
            viewModel.getToken().observe(this, { loginSuccess(viewModel.getToken().value) })

            //注册QQ登录的监听
            LiveDataBus.get().with("tentUserInfo", TentUserInfo::class.java).observe(this, {
                qqAuthSuccess(it)
            })
        }
    }

    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPersonalBinding.inflate(inflater, container, false)

    /**
     * 分享
     */
    private fun onShareClick() {
        val shareDialog = ShareDialogFragment(shareClickListener)
        shareDialog.showNow(childFragmentManager, "share")
    }

    private val shareClickListener = object : ShareDialogFragment.OnShareClickListener {
        override fun onQQShareClick(flag: Int) {
            WeiChatTool.regToQQ(BaseApplication.baseCtx)
            WeiChatTool.shareToQQ(
                requireActivity(),
                SHARE_TITLE,
                SHARE_CONTENT,
                SHARE_LINK,
                resources.getString(R.string.app_name),
                flag
            )
        }

        override fun onCopyClick() {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(null, SHARE_LINK)
            clipboard.setPrimaryClip(clipData)
            AndroidUtils.toast("复制成功，快去打开看看吧！", requireActivity())
        }

        override fun onWeiChatClick(flag: Int) {
            WeiChatTool.regToWx(BaseApplication.baseCtx)
            WeiChatTool.weiChatShareAsWeb(
                SHARE_LINK,
                SHARE_TITLE,
                SHARE_CONTENT,
                BitmapFactory.decodeResource(resources, R.drawable.share_icon),
                flag
            )
        }
    }
}