package com.junliu.personal.view

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.junliu.common.util.RouterPath
import com.junliu.personal.R
import com.junliu.personal.bean.User
import com.junliu.personal.component.BirthdayDialog
import com.junliu.personal.component.PhotoDialogFragment
import com.junliu.personal.component.SexDialogFragment
import com.junliu.personal.listener.ISelectSexListener
import com.junliu.personal.listener.ITakePhotoResult
import com.junliu.personal.viewmodel.PersonViewModel
import dc.android.bridge.view.BaseViewModelActivity
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_edit_materials.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: jun.liu
 * @date: 2021/2/18 15:19
 * @des:编辑资料
 */
@Route(path = RouterPath.PATH_EDIT_MATERIALS)
class EditMaterialsActivity :BaseViewModelActivity<PersonViewModel>(),ITakePhotoResult,BirthdayDialog.OnTimeSelectListener,ISelectSexListener {
    override fun getLayoutId() = R.layout.activity_edit_materials
    override fun providerVMClass() = PersonViewModel::class.java

    override fun initView() {
        //拍照
        layoutHeader.setOnClickListener {
            val dialogFragment = PhotoDialogFragment(this)
            dialogFragment.showNow(supportFragmentManager,"photo")
        }
        //选择生日
        layoutBirthday.setOnClickListener {
            val dialog = BirthdayDialog(this)
            dialog.showTimeDialog()
            dialog.setOnTimeSelectListener(this)
        }
        //修改昵称
        layoutNickName.setOnClickListener { ARouter.getInstance().build(RouterPath.PATH_MODIFY_NICKNAME).navigation() }
        //修改签名
        layoutSign.setOnClickListener { ARouter.getInstance().build(RouterPath.PATH_MODIFY_SIGN_NAME).navigation() }
        //修改性别
        layoutSex.setOnClickListener {
            val dialog = SexDialogFragment(this)
            dialog.showNow(supportFragmentManager, "sex")
        }
        viewModel.getUserInfo().observe(this, { setUserInfo(viewModel.getUserInfo().value) })
    }

    private fun setUserInfo(user: User?) {
    }

    override fun takePhotoResult(uri: Uri?) {
        uri?.let {
            Log.i("photo","该方法执行了*********")
            imgHeader.setImageURI(it)
        }
    }

    override fun takePhotoResult(path: String?) {
        path?.let {
            Log.i("photo","该方法执行了*********")
            imgHeader.setImageBitmap(BitmapFactory.decodeFile(it))
        }
    }

    /**
     * 生日选择的回调
     * @param date Date
     */
    override fun onTimeSelect(date: Date) {
        val sdf = SimpleDateFormat("yyyy年MM月dd日")
        tvBirthday.text = sdf.format(date)
        tvBirthday.setTextColor(ContextCompat.getColor(this,R.color.color999999))
    }

    override fun selectSex(sex: String) {
        tvSex.text = sex
        tvSex.setTextColor(ContextCompat.getColor(this, R.color.color999999))
    }
}