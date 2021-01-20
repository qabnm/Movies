package com.junliu.personal.view

import android.util.Log
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.junliu.common.util.RouterPath
import com.junliu.common.util.RouterPath.Companion.PATH_CONTRACT_SERVICE_ACTIVITY
import com.junliu.common.util.RouterPath.Companion.PATH_PERSONAL
import com.junliu.common.util.RouterPath.Companion.PATH_SETTING_ACTIVITY
import com.junliu.common.util.SharedPreferencesHelper
import com.junliu.personal.R
import dc.android.bridge.BridgeContext
import dc.android.bridge.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_personal.*

/**
 * @author: jun.liu
 * @date: 2020/12/29 : 14:49
 * 个人中心
 */
@Route(path = PATH_PERSONAL)
class PersonalFragment : BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_personal

    override fun initView() {
        if (SharedPreferencesHelper.helper.getValue(BridgeContext.isRes , 0) == 1){
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
        }else{
            layoutIsRes.visibility = View.GONE
        }
        layoutContract.setOnClickListener {
            ARouter.getInstance().build(PATH_CONTRACT_SERVICE_ACTIVITY).navigation()
        }
        layoutSetting.setOnClickListener {
            ARouter.getInstance().build(PATH_SETTING_ACTIVITY).navigation()
        }
        layoutAbout.setOnClickListener { }
    }
}