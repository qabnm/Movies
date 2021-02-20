package com.junliu.personal.view

import android.net.Uri
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.common.util.RouterPath
import com.junliu.personal.R
import com.junliu.personal.component.PhotoDialogFragment
import com.junliu.personal.listener.ITakePhotoResult
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_edit_materials.*

/**
 * @author: jun.liu
 * @date: 2021/2/18 15:19
 * @des:编辑资料
 */
@Route(path = RouterPath.PATH_EDIT_MATERIALS)
class EditMaterialsActivity :BridgeActivity(),ITakePhotoResult {
    override fun getLayoutId() = R.layout.activity_edit_materials

    override fun initView() {
        //拍照
        layoutHeader.setOnClickListener {
            val dialogFragment = PhotoDialogFragment(this)
            dialogFragment.showNow(supportFragmentManager,"photo")
        }
    }

    override fun takePhotoResult(uri: Uri?) {
        uri?.let {
            Log.i("photo","该方法执行了*********")
            imgHeader.setImageURI(it)
        }
    }
}