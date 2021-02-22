package com.junliu.personal.component

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.core.os.EnvironmentCompat
import androidx.fragment.app.DialogFragment
import com.junliu.personal.R
import com.junliu.personal.listener.ITakePhotoResult
import com.permissionx.guolindev.PermissionX
import dc.android.bridge.util.OsUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: jun.liu
 * @date: 2021/2/18 17:18
 * @des:拍照
 */
class PhotoDialogFragment(private val listener: ITakePhotoResult?) : DialogFragment() {
    //是否是安卓10
    private val isAndroidQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    //用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private var mCameraImagePath: String? = null

    //用于保存拍照图片的uri
    private var mCameraUri: Uri? = null
    private val CODE_TAKE_PHOTO = 200
    private var takePhotoResult: ITakePhotoResult? = null
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
        PermissionX.init(requireActivity()).permissions(
            Manifest.permission.CAMERA
        ).onExplainRequestReason { scope, deniedList ->
            val msg = "多多影视需要获取您以下权限"
            scope.showRequestReasonDialog(deniedList, msg, "确定", "取消")
        }.request { allGranted, _, _ ->
            if (allGranted) {
                takePhoto()
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
        }.request { allGranted, _, _ ->
            if (allGranted) {

            }
        }
    }

    /**
     * 拍照的方法
     */
    @SuppressLint("QueryPermissionsNeeded")
    private fun takePhoto() {
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //判断是否有相机
        if (captureIntent.resolveActivity(requireActivity().packageManager) != null) {
            val photoFile: File?
            var photoUri: Uri? = null
            if (isAndroidQ) {
                photoUri = createImageUri()
            } else {
                photoFile = createImageFile()
                if (null != photoFile) {
                    mCameraImagePath = photoFile.absolutePath
                    photoUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //7.0
                        FileProvider.getUriForFile(requireActivity(), "com.junliu.personal.fileProvider", photoFile)
                    } else {
                        //7.0以下
                        Uri.fromFile(photoFile)
                    }
                }
            }
            mCameraUri = photoUri
            if (photoUri != null) {
                captureIntent.apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivityForResult(this, CODE_TAKE_PHOTO)
                }
            }
        }
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     * @return Uri  图片的uri
     */
    @TargetApi(Build.VERSION_CODES.Q)
    private fun createImageUri(): Uri? {
        //设置保存参数到ContentValues中
        val contentValues = ContentValues()
        //设置文件名
        contentValues.apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "${System.currentTimeMillis()}")
            //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
            //RELATIVE_PATH是相对路径不是绝对路径;照片存储的地方为：内部存储/Pictures/duoImg
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/duoImg")
            //设置文件类型
            put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG")
        }
        return requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }

    /**
     * 创建保存图片的文件
     * @return File?
     */
    private fun createImageFile(): File? {
        val imageName =
            "${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.jpg"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (storageDir?.exists() == false) storageDir.mkdir()
        val tempFile = File(storageDir, imageName)
        if (Environment.MEDIA_MOUNTED != EnvironmentCompat.getStorageState(tempFile)) {
            return null
        }
        return tempFile
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        dismiss()
        Log.i("photo","该方法执行了")
        if (requestCode == CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            if (isAndroidQ){
                listener?.takePhotoResult(mCameraUri)
            }else{
                listener?.takePhotoResult(mCameraImagePath)
            }
        }
    }
}