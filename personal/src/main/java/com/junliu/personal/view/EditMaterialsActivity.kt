package com.junliu.personal.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.junliu.common.util.RouterPath
import com.junliu.personal.R
import com.junliu.personal.component.PhotoDialogFragment
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_edit_materials.*

/**
 * @author: jun.liu
 * @date: 2021/2/18 15:19
 * @des:编辑资料
 */
@Route(path = RouterPath.PATH_EDIT_MATERIALS)
class EditMaterialsActivity :BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_edit_materials

    override fun initView() {
        //拍照
        layoutHeader.setOnClickListener {
            PhotoDialogFragment().showNow(supportFragmentManager,"photo")
        }
    }
}