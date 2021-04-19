package com.duoduovv.personal.view

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.RouterPath.Companion.PATH_EDIT_MATERIALS
import com.duoduovv.common.util.RouterPath.Companion.PATH_PERSONAL
import com.duoduovv.common.util.RouterPath.Companion.PATH_SETTING_ACTIVITY
import com.duoduovv.common.util.SharedPreferencesHelper
import com.duoduovv.personal.R
import com.duoduovv.personal.bean.*
import com.duoduovv.personal.viewmodel.WeiChatViewModel
import com.duoduovv.tent.TentLoginListener
import com.duoduovv.tent.TentUserInfo
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.accessTokenUrl
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.accessTokenValidUrl
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.refreshTokenUrl
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.weiChatAppId
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.weiChatSecret
import com.duoduovv.weichat.WeiChatBridgeContext.Companion.weiChatUserInfoUrl
import com.duoduovv.weichat.WeiChatTool
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.SUCCESS
import dc.android.bridge.BridgeContext.Companion.TOKEN
import dc.android.bridge.BridgeContext.Companion.WAY
import dc.android.bridge.BridgeContext.Companion.WAY_VERIFY
import dc.android.bridge.util.AndroidUtils
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.OsUtils
import dc.android.bridge.util.StringUtils
import dc.android.bridge.view.BaseViewModelFragment
import dc.android.tools.LiveDataBus
import kotlinx.android.synthetic.main.fragment_personal.*

/**
 * @author: jun.liu
 * @date: 2020/12/29 : 14:49
 * 个人中心
 */
@Route(path = PATH_PERSONAL)
class PersonalFragment : BaseViewModelFragment<WeiChatViewModel>() {
    override fun getLayoutId() = R.layout.fragment_personal
    override fun providerVMClass() = WeiChatViewModel::class.java

    override fun initView() {
        if (SharedPreferencesHelper.helper.getValue(WAY, 0) != WAY_VERIFY) {
            //正式版
            layoutIsRes.visibility = View.VISIBLE
            layoutHistory.setOnClickListener {
                ARouter.getInstance().build(RouterPath.PATH_WATCH_HISTORY).navigation()
            }
            layoutDownload.setOnClickListener { }
            layoutCollection.setOnClickListener {
                ARouter.getInstance().build(RouterPath.PATH_MY_COLLECTION).navigation()
            }
            layoutShare.setOnClickListener {
            }
            layoutContainer.visibility = View.VISIBLE
            vLine.visibility = View.VISIBLE
        } else {
            //审核版
            layoutIsRes.visibility = View.GONE
            layoutContainer.visibility = View.GONE
            vLine.visibility = View.GONE
        }

        layoutContract.setOnClickListener {
            //问题反馈
            FeedbackAPI.openFeedbackActivity()
        }
        layoutSetting.setOnClickListener {
            ARouter.getInstance().build(PATH_SETTING_ACTIVITY).navigation()
        }
        layoutAbout.setOnClickListener {
            ARouter.getInstance().build(RouterPath.PATH_ABOUT_US).navigation()
        }
        layoutTop.setOnClickListener {
            ARouter.getInstance().build(PATH_EDIT_MATERIALS).navigation()
        }
        imgWeiChat.setOnClickListener { weiChatLogin() }
        imgQQ.setOnClickListener { qqLogin() }
        viewModel.getUserInfo().observe(this, { onGetUserInfoSuc(viewModel.getUserInfo().value) })
        LiveDataBus.get().with("logout", Int::class.java).observe(this, {
            WeiChatTool.mTenCent?.logout(requireActivity())
            if (it == SUCCESS) viewModel.userInfo()
        })
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
            layoutLogin.visibility = View.GONE
            layoutTop.visibility = View.VISIBLE
            GlideUtils.setImg(requireActivity(), it.img, imageIcon)
            tvUser.text = it.nick
        }
    }

    override fun initData() {
        //正式版才请求登录接口
        if (SharedPreferencesHelper.helper.getValue(WAY, 0) != WAY_VERIFY) {
            viewModel.userInfo()
        }
        setFeedbackUi()
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
                layoutLogin.visibility = View.GONE
                layoutTop.visibility = View.VISIBLE
                GlideUtils.setImg(requireActivity(), it.img, imageIcon)
                tvUser.text = it.nickName
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
            viewModel.login(2, it.openid, it.nickname, it.sex, it.headimgurl ?: "", it.unionid)
        }
    }

    /**
     * 获取用户信息
     * @param bean AccessTokenValidBean?
     */
    private fun accessTokenValid(bean: AccessTokenValidBean?) {
        bean?.let {
            if (it.errcode == 0) {
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
                token = it.refresh_token
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
                accessToken = it.access_token,
                openId = it.openid
            )
        }
    }

    /**
     * 微信登录
     */
    private fun weiChatLogin() {
        AndroidUtils.toast("开发完善中", requireActivity())
        return
//        WeiChatTool.regToWx(BaseApplication.baseCtx)
//        WeiChatTool.weiChatLogin(requireContext())
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
        layoutLogin.visibility = View.VISIBLE
        layoutTop.visibility = View.GONE

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
}