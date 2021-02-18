package com.junliu.personal.component

import android.Manifest
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.junliu.personal.R
import com.permissionx.guolindev.PermissionX
import dc.android.bridge.util.OsUtils

/**
 * @author: jun.liu
 * @date: 2021/2/18 17:18
 * @des:拍照
 */
class PhotoDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_photo, container, false)
        val tvTakePhoto: TextView = view.findViewById(R.id.tvTakePhoto)
        val tvPhoto: TextView = view.findViewById(R.id.tvPhoto)
        tvTakePhoto.setOnClickListener { onTakePhotoClick() }
        tvPhoto.setOnClickListener { onPhotoClick() }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
    }

    private fun initWindow() {
        val window = dialog?.window
        window?.let {
            it.attributes.width =
                OsUtils.getScreenWidth(requireContext()) - OsUtils.dip2px(requireContext(), 75f)
            it.attributes.gravity = Gravity.CENTER
        }
    }

    /**
     * 拍照
     */
    private fun onTakePhotoClick() {
        //首先检查权限
        PermissionX.init(requireActivity()).permissions(Manifest.permission.CAMERA)
            .onExplainRequestReason { scope, deniedList ->
                val msg = "多多影视需要获取您以下权限"
                scope.showRequestReasonDialog(deniedList, msg, "确定", "取消")
            }.request { allGranted, grantedList, deniedList ->
                if (allGranted) {

                }
            }
    }

    /**
     *相册
     */
    private fun onPhotoClick() {
        //首先检查权限
        PermissionX.init(requireActivity()).permissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).onExplainRequestReason { scope, deniedList ->
            val msg = "多多影视需要获取您以下权限"
            scope.showRequestReasonDialog(deniedList, msg, "确定", "取消")
        }.request { allGranted, grantedList, deniedList ->
            if (allGranted) {

            }
        }
    }
}