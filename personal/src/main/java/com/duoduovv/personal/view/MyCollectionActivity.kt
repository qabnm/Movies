package com.duoduovv.personal.view

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duoduovv.common.BaseApplication
import com.duoduovv.common.util.RouterPath
import com.duoduovv.common.util.SharedPreferencesHelper
import com.duoduovv.personal.R
import com.duoduovv.personal.adapter.MyCollectionAdapter
import com.duoduovv.personal.databinding.ActivityMyCollcetionBinding
import com.duoduovv.room.database.CollectionDatabase
import com.duoduovv.room.domain.CollectionBean
import dc.android.bridge.BridgeContext
import dc.android.bridge.view.BridgeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author: jun.liu
 * @date: 2021/1/11 13:51
 * @des:我的收藏
 */
@Route(path = RouterPath.PATH_MY_COLLECTION)
class MyCollectionActivity : BridgeActivity() {
    override fun getLayoutId() = R.layout.activity_my_collcetion
    private lateinit var mBind: ActivityMyCollcetionBinding

    private var collectionAdapter: MyCollectionAdapter? = null
    private var isFirst = true
    private var selectCount = 0
    private var isAllSelect = false

    override fun initView() {
        mBind = ActivityMyCollcetionBinding.bind(layoutView)
        mBind.rvList.layoutManager = LinearLayoutManager(this)
        collectionAdapter = MyCollectionAdapter()
        mBind.rvList.adapter = collectionAdapter
        collectionAdapter?.addChildClickViewIds(R.id.imgSelect)
        collectionAdapter?.setOnItemClickListener { adapter, _, position ->
            val movieId = (adapter as MyCollectionAdapter).data[position].strId
            val way = SharedPreferencesHelper.helper.getValue(BridgeContext.WAY, "")
            val path = if (way == BridgeContext.WAY_VERIFY) {
                RouterPath.PATH_MOVIE_DETAIL_FOR_DEBUG
            } else {
                RouterPath.PATH_MOVIE_DETAIL
            }
            ARouter.getInstance().build(path)
                .withString(BridgeContext.ID, movieId).navigation()
        }
        collectionAdapter?.setOnItemChildClickListener { adapter, view, position ->
            onClick(adapter, view, position)
        }
        mBind.layoutTopBar.setRightClick { onEditClick() }
        mBind.tvAllSelect.setOnClickListener { allSelect() }
        mBind.tvDelete.setOnClickListener { deleteCollect() }
    }

    /**
     * 删除成功
     */
    private fun onDeleteSuccess() {
        collectionAdapter?.isEdit(false)
        //删除成功 直接刷新接口
        initData()
        selectCount = 0
        isFirst = true
        mBind.layoutSelect.visibility = View.GONE
        mBind.layoutTopBar.setRightText("编辑")
    }

    /**
     * 全部选择
     */
    private fun allSelect() {
        if (!isAllSelect) {
            isAllSelect = true //全选状态
            collectionAdapter?.let {
                for (i in 0 until it.data.size) {
                    it.data[i].isCollect = true
                }
                selectCount = it.data.size
            }
            mBind.tvAllSelect.text = "取消全选"
        } else {
            isAllSelect = false //非全选状态
            collectionAdapter?.let {
                for (i in 0 until it.data.size) {
                    it.data[i].isCollect = false
                }
                selectCount = 0
            }
            mBind.tvAllSelect.text = "全选"
        }
        collectionAdapter?.notifyDataSetChanged()
        setDeleteState()
    }

    private fun onClick(
        adapter: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, position: Int
    ) {
        when (view.id) {
            R.id.imgSelect -> {
                selectCount = 0
                val isSelect = (adapter.data[position] as CollectionBean).isCollect
                (adapter.data[position] as CollectionBean).isCollect = !isSelect
                collectionAdapter?.notifyItemChanged(position)
                //筛选出选中个数
                for (i in 0 until adapter.data.size) {
                    if ((adapter.data[i] as CollectionBean).isCollect) selectCount++
                }
                setDeleteState()
            }
        }
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

    private fun deleteCollect() {
        //取出当前被选中的item
        val dataList = collectionAdapter?.data
        if (dataList?.isNotEmpty() == true) {
            //当前有选中删除的项目
            GlobalScope.launch(Dispatchers.Main) {
                for (i in dataList.indices) {
                    if (dataList[i].isCollect) {
                        deleteCollections(dataList[i])
                    }
                }
                onDeleteSuccess()
            }
        }
    }

    /**
     * 编辑按钮点击事件
     */
    private fun onEditClick() {
        if (collectionAdapter?.data?.isEmpty() == true) return
        for (i in 0 until collectionAdapter!!.data.size) {
            collectionAdapter!!.data[i].isCollect = false
        }
        if (isFirst) {
            isFirst = false
            mBind.layoutTopBar.setRightText("取消")
            collectionAdapter?.isEdit(true)
            mBind.layoutSelect.visibility = View.VISIBLE
        } else {
            isFirst = true
            selectCount = 0
            mBind.layoutTopBar.setRightText("编辑")
            collectionAdapter?.isEdit(false)
            mBind.layoutSelect.visibility = View.GONE
        }
        mBind.tvDelete.text = "删除"
        mBind.tvDelete.setTextColor(Color.parseColor("#99F1303C"))
        mBind.tvDelete.isEnabled = false
        collectionAdapter?.notifyDataSetChanged()
    }

    override fun initData() {
        GlobalScope.launch(Dispatchers.Main) {
            getCollection(queryAll())
        }
    }

    /**
     * 查询所有数据
     * @return List<CollectionBean>
     */
    private suspend fun queryAll() = withContext(Dispatchers.IO) {
        CollectionDatabase.getInstance(BaseApplication.baseCtx).collection().queryAllByDate()
    }

    /**
     * 删除数据
     * @param bean CollectionBean
     */
    private suspend fun deleteCollections(bean: CollectionBean) = withContext(Dispatchers.IO) {
        CollectionDatabase.getInstance(BaseApplication.baseCtx).collection().delete(bean)
    }


    private fun getCollection(dataList: List<CollectionBean>?) {
        if (dataList?.isEmpty() == true) {
            //收藏为空
            mBind.layoutEmpty.setEmptyVisibility(1)
            mBind.layoutSelect.visibility = View.GONE
            mBind.layoutTopBar.setRightVisibility(View.GONE)
            isFirst = true
        } else {
            mBind.layoutEmpty.setEmptyVisibility(0)
            mBind.layoutTopBar.setRightVisibility(View.VISIBLE)
            mBind.layoutTopBar.setRightText("编辑")
        }
        collectionAdapter?.setList(dataList)
    }
}