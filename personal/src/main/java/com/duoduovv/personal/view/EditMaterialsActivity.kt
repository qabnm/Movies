package com.duoduovv.personal.view

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.duoduovv.common.util.RouterPath
import com.duoduovv.personal.R
import com.duoduovv.personal.bean.User
import com.duoduovv.personal.component.BirthdayDialog
import com.duoduovv.personal.listener.ISelectSexListener
import com.duoduovv.personal.listener.ITakePhotoResult
import com.duoduovv.personal.viewmodel.PersonViewModel
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.util.StringUtils
import dc.android.bridge.view.BaseViewModelActivity
import kotlinx.android.synthetic.main.activity_edit_materials.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: jun.liu
 * @date: 2021/2/18 15:19
 * @des:编辑资料
 */
@Route(path = RouterPath.PATH_EDIT_MATERIALS)
class EditMaterialsActivity : BaseViewModelActivity<PersonViewModel>(), ITakePhotoResult,
    BirthdayDialog.OnTimeSelectListener, ISelectSexListener {
    override fun getLayoutId() = R.layout.activity_edit_materials
    override fun providerVMClass() = PersonViewModel::class.java
    private var normalColor = 0

    override fun initView() {
        //拍照
//        layoutHeader.setOnClickListener {
//            val dialogFragment = PhotoDialogFragment(this)
//            dialogFragment.showNow(supportFragmentManager,"photo")
//        }
//        //选择生日
//        layoutBirthday.setOnClickListener {
//            val dialog = BirthdayDialog(this)
//            dialog.showTimeDialog()
//            dialog.setOnTimeSelectListener(this)
//        }
//        //修改昵称
//        layoutNickName.setOnClickListener { ARouter.getInstance().build(RouterPath.PATH_MODIFY_NICKNAME).navigation() }
//        //修改签名
//        layoutSign.setOnClickListener { ARouter.getInstance().build(RouterPath.PATH_MODIFY_SIGN_NAME).navigation() }
//        //修改性别
//        layoutSex.setOnClickListener {
//            val dialog = SexDialogFragment(this)
//            dialog.showNow(supportFragmentManager, "sex")
//        }
        normalColor = ContextCompat.getColor(this, R.color.color999999)
        viewModel.getUserInfo().observe(this, { setUserInfo(viewModel.getUserInfo().value) })
    }

    override fun onResume() {
        super.onResume()
        viewModel.userInfo()
    }

    private fun setUserInfo(user: User?) {
        if (null == user) return
        if (!StringUtils.isEmpty(user.img)) {
            GlideUtils.setImg(this, user.img, imgHeader)
        }
        if (!StringUtils.isEmpty(user.nick)) setText(tvNickName, user.nick)
        if (user.sex == 1) {
            setText(tvSex, "男")
        } else if (user.sex == 2) {
            setText(tvSex, "女")
        }
        if (!StringUtils.isEmpty(user.province)) setText(
            tvWhere,
            "${user.province}${user.city}${user.area}"
        )
        if (!StringUtils.isEmpty(user.created_at)) setText(tvBirthday, user.created_at)
        if (!StringUtils.isEmpty(user.cellphone)) setText(tvSign, user.cellphone)
    }

    private fun setText(textView: TextView, text: String) {
        textView.text = text
        textView.setTextColor(normalColor)
    }

    override fun takePhotoResult(uri: Uri?) {
        uri?.let { imgHeader.setImageURI(it) }
    }

    override fun takePhotoResult(path: String?) {
        path?.let { imgHeader.setImageBitmap(BitmapFactory.decodeFile(it)) }
    }

    /**
     * 生日选择的回调
     * @param date Date
     */
    override fun onTimeSelect(date: Date) {
        val sdf = SimpleDateFormat("yyyy年MM月dd日")
        tvBirthday.text = sdf.format(date)
        tvBirthday.setTextColor(ContextCompat.getColor(this, R.color.color999999))
    }

    override fun selectSex(sex: String) {
        tvSex.text = sex
        tvSex.setTextColor(ContextCompat.getColor(this, R.color.color999999))
    }
}