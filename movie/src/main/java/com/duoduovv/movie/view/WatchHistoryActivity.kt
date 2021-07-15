package com.duoduovv.movie.view

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.advert.gdtad.GDTInfoAdForSelfRender
import com.duoduovv.advert.ttad.TTInfoAd
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.SharedPreferencesHelper
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.WatchHistoryAdapter
import com.duoduovv.movie.databinding.ActivityWatchHistoryBinding
import com.duoduovv.room.database.WatchHistoryDatabase
import com.duoduovv.room.domain.VideoWatchHistoryBean
import dc.android.bridge.BridgeContext
import dc.android.bridge.BridgeContext.Companion.ID
import dc.android.bridge.BridgeContext.Companion.TYPE_ID
import dc.android.bridge.util.OsUtils
import dc.android.bridge.view.BridgeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: jun.liu
 * @date: 2021/1/6 13:28
 * @des:观看历史
 */
@Route(path = RouterPath.PATH_WATCH_HISTORY)
class WatchHistoryActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_watch_history
    private lateinit var mBind: ActivityWatchHistoryBinding

    private var isFirst = true
    private var selectCount = 0
    private var historyAdapter: WatchHistoryAdapter? = null
    private var isAllSelect = false
    private var ttAd: TTInfoAd? = null
    private var gdtAd: GDTInfoAdForSelfRender? = null

    override fun initView() {
        mBind = ActivityWatchHistoryBinding.bind(layoutView)
        historyAdapter = WatchHistoryAdapter()
        mBind.rvList.adapter = historyAdapter
        historyAdapter?.addChildClickViewIds(R.id.imgSelect)
        historyAdapter?.setOnItemChildClickListener { adapter, view, position ->
            onClick(adapter, view, position)
        }
        mBind.layoutTopBar.setRightClick { onEditClick() }
        mBind.tvAllSelect.setOnClickListener { allSelect() }
        mBind.tvDelete.setOnClickListener { onDeleteClick() }
        historyAdapter?.setOnItemClickListener { adapter, _, position ->
            val bean = (adapter as WatchHistoryAdapter).data[position]
            val way = SharedPreferencesHelper.helper.getValue(BridgeContext.WAY, "")
            val path = if (way == BridgeContext.WAY_VERIFY) {
                RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG
            } else {
                RouterPath.PATH_MOVIE_DETAIL
            }
            ARouter.getInstance().build(path)
                .withString(ID, bean.movieId).withString(TYPE_ID, bean.vid).navigation()
        }
        initAd()
    }

    private fun initAd() {
        BaseApplication.configBean?.ad?.centerTop?.let {
            mBind.layoutAd.visibility = View.VISIBLE
            when (it.type) {
                BridgeContext.TYPE_TT_AD -> {
                    val width =
                        OsUtils.px2dip(this, OsUtils.getScreenWidth(this).toFloat()).toFloat()
                    ttAd = TTInfoAd()
                    ttAd?.initTTInfoAd(this, it.value, width, 0f, mBind.layoutTTAd)
                }
                BridgeContext.TYPE_GDT_AD -> {
                    mBind.layoutGdt.visibility = View.VISIBLE
                    gdtAd = GDTInfoAdForSelfRender()
                    gdtAd?.initInfoAd(
                        this,
                        it.value,
                        mBind.adImgCover,
                        mBind.mediaView,
                        mBind.layoutGdt
                    )
                }
                else -> {
                    mBind.layoutAd.visibility = View.GONE
                }
            }
        } ?: also {
            mBind.layoutAd.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        ttAd?.destroyInfoAd()
        gdtAd?.onDestroy()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        gdtAd?.onResume()
    }

    /**
     * 删除观看历史数据
     */
    private fun onDeleteClick() {
        //取出当前被选中的item
        val dataList = historyAdapter?.data
        if (dataList?.isNotEmpty() == true) {
            //当前有选中删除的项目
            GlobalScope.launch(Dispatchers.Main) {
                for (i in dataList.indices) {
                    if (dataList[i].isSelect) {
                        deleteMovie(dataList[i])
                    }
                }
                onDeleteSuccess()
            }
        }
    }

    /**
     * 删除成功
     */
    private fun onDeleteSuccess() {
        historyAdapter?.isEdit(false)
        //删除成功 直接刷新接口
        initData()
        selectCount = 0
        isFirst = true
        mBind.layoutSelect.visibility = View.GONE
        mBind.layoutTopBar.setRightText("编辑")
    }

    override fun initData() {
        GlobalScope.launch(Dispatchers.Main) {
            val dataList = getDB()
            if (dataList?.isEmpty() == true) {
                //观看历史为空
                mBind.layoutEmpty.setEmptyVisibility(1)
                mBind.layoutSelect.visibility = View.GONE
                mBind.layoutTopBar.setRightVisibility(View.GONE)
                isFirst = true
            } else {
//                Collections.sort(dataList) { o1, o2 -> (o2.currentTime - o1.currentTime).toInt() }
                mBind.layoutEmpty.setEmptyVisibility(0)
                mBind.layoutTopBar.setRightVisibility(View.VISIBLE)
                mBind.layoutTopBar.setRightText("编辑")
            }
            historyAdapter?.setList(dataList)
        }
    }

    /**
     * 删除数据
     * @param bean VideoWatchHistoryBean
     */
    private suspend fun deleteMovie(bean: VideoWatchHistoryBean) = withContext(Dispatchers.IO) {
        WatchHistoryDatabase.getInstance(BaseApplication.baseCtx).history().delete(bean)
    }

    /**
     * 获取数据库数据
     * @return List<VideoWatchHistoryBean>
     */
    private suspend fun getDB() = withContext(Dispatchers.IO) {
        WatchHistoryDatabase.getInstance(BaseApplication.baseCtx).history().queryAllByDate()
    }

    /**
     * 全部选择
     */
    private fun allSelect() {
        if (!isAllSelect) {
            isAllSelect = true //全选状态
            historyAdapter?.let {
                for (i in 0 until it.data.size) {
                    it.data[i].isSelect = true
                }
                selectCount = it.data.size
            }
            mBind.tvAllSelect.text = "取消全选"
        } else {
            isAllSelect = false //非全选状态
            historyAdapter?.let {
                for (i in 0 until it.data.size) {
                    it.data[i].isSelect = false
                }
                selectCount = 0
            }
            mBind.tvAllSelect.text = "全选"
        }
        historyAdapter?.notifyDataSetChanged()
        setDeleteState()
    }

    private fun onClick(
        adapter: BaseQuickAdapter<Any?, BaseViewHolder>,
        view: View,
        position: Int
    ) {
        when (view.id) {
            R.id.imgSelect -> {
                selectCount = 0
                val isSelect = (adapter.data[position] as VideoWatchHistoryBean).isSelect
                (adapter.data[position] as VideoWatchHistoryBean).isSelect = !isSelect
                historyAdapter?.notifyItemChanged(position)
                //筛选出选中个数
                for (i in 0 until adapter.data.size) {
                    if ((adapter.data[i] as VideoWatchHistoryBean).isSelect) selectCount++
                }
                setDeleteState()
            }
        }
    }

    /**
     * 编辑按钮点击事件
     */
    private fun onEditClick() {
        if (historyAdapter?.data?.isEmpty() == true) return
        if (isFirst) {
            isFirst = false
            mBind.layoutTopBar.setRightText("取消")
            historyAdapter?.isEdit(true)
            mBind.layoutSelect.visibility = View.VISIBLE
        } else {
            isFirst = true
            for (i in 0 until historyAdapter!!.data.size) {
                historyAdapter!!.data[i].isSelect = false
            }
            selectCount = 0
            mBind.layoutTopBar.setRightText("编辑")
            historyAdapter?.isEdit(false)
            mBind.layoutSelect.visibility = View.GONE
        }
        mBind.tvDelete.text = "删除"
        mBind.tvDelete.setTextColor(Color.parseColor("#99F1303C"))
        mBind.tvDelete.isEnabled = false
        historyAdapter?.notifyDataSetChanged()
    }

    /**
     * 设置删除按钮的状态
     */
    private fun setDeleteState() {
        if (selectCount > 0) {
            mBind.tvDelete.text = "删除$selectCount"
            mBind.tvDelete.setTextColor(Color.parseColor("#F1303C"))
            mBind.tvDelete.isEnabled = true
        } else {
            mBind.tvDelete.text = "删除"
            mBind.tvDelete.setTextColor(Color.parseColor("#99F1303C"))
            mBind.tvDelete.isEnabled = false
        }
    }
}