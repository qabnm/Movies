package com.junliu.personal.view

import android.graphics.Color
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.junliu.common.util.RouterPath
import com.junliu.personal.R
import com.junliu.personal.adapter.MyCollectionAdapter
import com.junliu.personal.bean.FavoriteBean
import com.junliu.personal.bean.MyCollectionBean
import com.junliu.personal.viewmodel.CollectionViewModel
import dc.android.bridge.view.BaseViewModelActivity
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_my_collcetion.*

/**
 * @author: jun.liu
 * @date: 2021/1/11 13:51
 * @des:我的收藏
 */
@Route(path = RouterPath.PATH_MY_COLLECTION)
class MyCollectionActivity : BaseViewModelActivity<CollectionViewModel>() {
    override fun getLayoutId() = R.layout.activity_my_collcetion
    override fun providerVMClass() = CollectionViewModel::class.java

    private var collectionAdapter: MyCollectionAdapter? = null
    private var isFirst = true
    private var selectCount = 0
    private var isAllSelect = false
    private var page = 1

    override fun initView() {
        rvList.layoutManager = LinearLayoutManager(this)
        collectionAdapter = MyCollectionAdapter()
        rvList.adapter = collectionAdapter
        collectionAdapter?.addChildClickViewIds(R.id.imgSelect)
        collectionAdapter?.setOnItemChildClickListener { adapter, view, position ->
            onClick(adapter, view, position)
        }
        layoutTopBar.setRightClick { onEditClick() }
        tvAllSelect.setOnClickListener { allSelect() }
        tvDelete.setOnClickListener { deleteCollect() }
        viewModel.deleteState().observe(this,
            { onDeleteSuccess(viewModel.deleteState().value?.movie_id) })
        viewModel.getCollection()
            .observe(this, { getCollection(viewModel.getCollection().value?.favorites) })
    }

    /**
     * 删除成功
     * @param movieId String?
     */
    private fun onDeleteSuccess(movieId: String?) {
        //删除成功 直接刷新接口
        page = 1
        viewModel.collectionList(page)
    }

    /**
     * 全部选择
     */
    private fun allSelect() {
        if (!isAllSelect) {
            isAllSelect = true //全选状态
            collectionAdapter?.let {
                for (i in 0 until it.data.size) {
                    it.data[i].isSelect = true
                }
                selectCount = it.data.size
            }
            tvAllSelect.text = "取消全选"
        } else {
            isAllSelect = false //非全选状态
            collectionAdapter?.let {
                for (i in 0 until it.data.size) {
                    it.data[i].isSelect = false
                }
                selectCount = 0
            }
            tvAllSelect.text = "全选"
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
                val isSelect = (adapter.data[position] as FavoriteBean).isSelect
                (adapter.data[position] as FavoriteBean).isSelect = !isSelect
                collectionAdapter?.notifyItemChanged(position)
                //筛选出选中个数
                for (i in 0 until adapter.data.size) {
                    if ((adapter.data[i] as FavoriteBean).isSelect) selectCount++
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
            tvDelete.text = "删除$selectCount"
            tvDelete.setTextColor(Color.parseColor("#F1303C"))
            tvDelete.isEnabled = true
        } else {
            tvDelete.text = "删除"
            tvDelete.setTextColor(Color.parseColor("#99F1303C"))
            tvDelete.isEnabled = false
        }
    }

    private fun deleteCollect() {
        //取出当前被选中的item
        val dataList = collectionAdapter?.data
        if (dataList?.isNotEmpty() == true) {
            //当前有选中删除的项目
            val builder = StringBuilder()
            for (i in dataList.indices) {
                builder.append(dataList[i].id).append(",")
            }
            var movieId = builder.toString()
            if (builder.toString().endsWith(",")) {
                movieId = builder.toString()
                    .substring(builder.toString().length - 1, builder.toString().length)
            }
            viewModel.deleteCollection(movieId)//调用删除接口
        }
    }

    /**
     * 编辑按钮点击事件
     */
    private fun onEditClick() {
        if (isFirst) {
            isFirst = false
            layoutTopBar.setRightText("取消")
            collectionAdapter?.isEdit(true)
            layoutSelect.visibility = View.VISIBLE
        } else {
            isFirst = true
            for (i in 0 until collectionAdapter!!.data.size) {
                collectionAdapter!!.data[i].isSelect = false
            }
            selectCount = 0
            layoutTopBar.setRightText("编辑")
            collectionAdapter?.isEdit(false)
            layoutSelect.visibility = View.GONE
        }
        tvDelete.text = "删除"
        tvDelete.setTextColor(Color.parseColor("#99F1303C"))
        tvDelete.isEnabled = false
        collectionAdapter?.notifyDataSetChanged()
    }

    override fun initData() {
        viewModel.collectionList(page)
    }

    private fun getCollection(dataList: List<FavoriteBean>?) {
        collectionAdapter?.setList(dataList)
    }

}