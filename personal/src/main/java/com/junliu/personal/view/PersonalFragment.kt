package com.junliu.personal.view

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.junliu.common.BaseApplication
import com.junliu.common.util.RouterPath
import com.junliu.common.util.RouterPath.Companion.PATH_CONTRACT_SERVICE_ACTIVITY
import com.junliu.common.util.RouterPath.Companion.PATH_EDIT_MATERIALS
import com.junliu.common.util.RouterPath.Companion.PATH_PERSONAL
import com.junliu.common.util.RouterPath.Companion.PATH_SETTING_ACTIVITY
import com.junliu.common.util.SharedPreferencesHelper
import com.junliu.personal.R
import com.junliu.personal.bean.*
import com.junliu.personal.viewmodel.WeiChatViewModel
import com.junliu.weichat.WeiChatBridgeContext
import com.junliu.weichat.WeiChatBridgeContext.Companion.accessTokenUrl
import com.junliu.weichat.WeiChatBridgeContext.Companion.accessTokenValidUrl
import com.junliu.weichat.WeiChatBridgeContext.Companion.refreshTokenUrl
import com.junliu.weichat.WeiChatBridgeContext.Companion.weiChatAppId
import com.junliu.weichat.WeiChatBridgeContext.Companion.weiChatSecret
import com.junliu.weichat.WeiChatBridgeContext.Companion.weiChatUserInfoUrl
import com.junliu.weichat.WeiChatTool
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.TOKEN
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.StringUtils
import dc.android.bridge.view.BaseFragment
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
        if (SharedPreferencesHelper.helper.getValue(BridgeContext.isRes, 0) == 1) {
            //正式版
            layoutIsRes.visibility = View.VISIBLE
            layoutHistory.setOnClickListener {
                ARouter.getInstance().build(RouterPath.PATH_WATCH_HISTORY).navigation()
            }
            layoutDownload.setOnClickListener {
                val age = SharedPreferencesHelper.helper.getValue("age", 0) as Int
                Log.i("age", age.toString())
            }
            layoutCollection.setOnClickListener {
                ARouter.getInstance().build(RouterPath.PATH_MY_COLLECTION).navigation()
            }
            layoutShare.setOnClickListener { }
        } else {
            layoutIsRes.visibility = View.GONE
        }
        layoutContract.setOnClickListener {
            ARouter.getInstance().build(PATH_CONTRACT_SERVICE_ACTIVITY).navigation()
        }
        layoutSetting.setOnClickListener {
            ARouter.getInstance().build(PATH_SETTING_ACTIVITY).navigation()
        }
        layoutAbout.setOnClickListener { }
        layoutTop.setOnClickListener {
            ARouter.getInstance().build(PATH_EDIT_MATERIALS).navigation()
        }
        imgWeiChat.setOnClickListener { weiChatLogin() }
    }

    override fun initData() {
        LiveDataBus.get().with("wxCode", String::class.java).observe(this, {
            //请求微信的accessToken
            viewModel.accessToken(
                url = accessTokenUrl,
                appId = weiChatAppId,
                secret = weiChatSecret,
                code = it
            )
        })
        viewModel.getAccessToken().observe(this, { accessToken(viewModel.getAccessToken().value) })
        viewModel.getRefreshToken()
            .observe(this, { refreshToken(viewModel.getRefreshToken().value) })
        viewModel.getAccessTokenValid()
            .observe(this, { accessTokenValid(viewModel.getAccessTokenValid().value) })
        viewModel.getWeiChatUseInfo()
            .observe(this, { setUserInfo(viewModel.getWeiChatUseInfo().value) })
        viewModel.getToken().observe(this, { loginSuccess(viewModel.getToken().value) })
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
        WeiChatTool.regToWx(BaseApplication.baseCtx)
        WeiChatTool.weiChatLogin(requireContext())
    }
}