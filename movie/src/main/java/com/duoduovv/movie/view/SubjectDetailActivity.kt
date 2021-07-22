package com.duoduovv.movie.view

import android.annotation.TargetApi
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.component.AppBarStateChangeListener
import com.duoduovv.common.util.RouterPath
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.MovieSubjectDetailAdapter
import com.duoduovv.movie.bean.SubjectDetailListBean
import com.duoduovv.movie.databinding.ActivitySubjectDetailBinding
import com.duoduovv.movie.viewmodel.SubjectDetailViewModel
import com.google.android.material.appbar.AppBarLayout
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.TITLE
import dc.android.bridge.BridgeContext.Companion.WAY_VERIFY
import dc.android.bridge.EventContext
import dc.android.bridge.util.GlideUtils
import dc.android.bridge.view.BaseViewModelActivity


/**
 * @author: jun.liu
 * @date: 2021/6/25 13:32
 * @des:专题详情页
 */
@Route(path = RouterPath.PATH_SUBJECT_DETAIL)
class SubjectDetailActivity : BaseViewModelActivity<SubjectDetailViewModel>() {
    override fun getLayoutId() = R.layout.activity_subject_detail
    override fun providerVMClass() = SubjectDetailViewModel::class.java
    private lateinit var mBind: ActivitySubjectDetailBinding
    private var detailAdapter: MovieSubjectDetailAdapter? = null
    override fun showStatusBarView() = false
    private var coverUrl = ""

    override fun setLayout(isStatusColorDark: Boolean, statusBarColor: Int) {
        super.setLayout(false, ContextCompat.getColor(this,R.color.colorFFFFFF))
    }

    override fun initView() {
        mBind = ActivitySubjectDetailBinding.bind(layoutView)
        detailAdapter = MovieSubjectDetailAdapter()
        mBind.rvList.adapter = detailAdapter
        detailAdapter?.setOnItemClickListener { adapter, _, position ->
            val bean = (adapter as MovieSubjectDetailAdapter).data[position]
            onMovieClick(bean.strId, bean.way)
        }
        viewModel.getSubjectDetail().observe(this, { setList(viewModel.getSubjectDetail().value) })
        mBind.ivBack.setOnClickListener { finish() }
        mBind.appBar.addOnOffsetChangedListener(appbarListener)
    }

    /**
     * appbar滑动监听
     */
    private val appbarListener = object : AppBarStateChangeListener() {
        override fun onStateChanged(appBarLayout: AppBarLayout?, state: State) {
            when (state) {
                State.EXPANDED -> {
                    mBind.ivBack.setImageResource(R.drawable.back_white)
                    setStatusBar(false)
                }
                State.COLLAPSED -> {
                    mBind.ivBack.setImageResource(R.drawable.back)
                    setStatusBar(true)
                }
                else -> mBind.ivBack.setImageResource(R.drawable.back_white)
            }
        }
    }

    private fun setList(list: List<SubjectDetailListBean>?) {
        if (list?.isNotEmpty() == true) {
            detailAdapter?.setList(list)
            mBind.tvTotal.text = "共${list.size}部"
        } else {
            mBind.tvTotal.text = ""
        }
    }

    override fun initData() {
        val subjectId = intent.getStringExtra(ID) ?: ""
        val title = intent.getStringExtra(TITLE) ?: ""
        coverUrl = intent.getStringExtra("coverUrl") ?: ""
        val des = intent.getStringExtra("des")?:""
        viewModel.subjectDetail(subjectId)
        GlideUtils.setImg(this, coverUrl, mBind.imgCover)
        mBind.toolBarLayout.title = title
        mBind.tvDes.text = des
    }

    /**
     * 跳转影视详情
     * @param movieId String
     */
    private fun onMovieClick(movieId: String, way: String) {
        val path = if (way == WAY_VERIFY) {
            RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG
        } else {
            RouterPath.PATH_MOVIE_DETAIL
        }
        ARouter.getInstance().build(path).withString(ID, movieId).navigation()
        EventContext.uMenEvent(EventContext.EVENT_SUBJECT_TO_DETAIL, null)
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun setStatusBar(dark: Boolean) {
        val decor = window.decorView
        if (dark) {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
}