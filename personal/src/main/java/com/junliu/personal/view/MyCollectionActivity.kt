package com.junliu.personal.view

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.junliu.common.util.RouterPath
import com.junliu.personal.R
import com.junliu.personal.adapter.MyCollectionAdapter
import com.junliu.personal.bean.MyCollectionBean
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_my_collcetion.*

/**
 * @author: jun.liu
 * @date: 2021/1/11 13:51
 * @des:我的收藏
 */
@Route(path = RouterPath.PATH_MY_COLLECTION)
class MyCollectionActivity :BridgeActivity(){
    override fun getLayoutId() = R.layout.activity_my_collcetion

    private var collectionAdapter :MyCollectionAdapter? =null
    private var isFirst = true
    private var selectCount = 0

    override fun initView() {
        rvList.layoutManager = LinearLayoutManager(this)
        collectionAdapter = MyCollectionAdapter()
        rvList.adapter = collectionAdapter
        collectionAdapter?.addChildClickViewIds(R.id.imgSelect)
        collectionAdapter?.setOnItemChildClickListener { adapter, view, position -> onClick(adapter,view,position) }
        layoutTopBar.setRightClick { onEditClick() }
        tvAllSelect.setOnClickListener { allSelect() }
    }

    /**
     * 全部选择
     */
    private fun allSelect(){
        collectionAdapter?.let {
            for (i in 0 until it.data.size){
                it.data[i].isSelect = true
            }
            it.notifyDataSetChanged()
            selectCount = it.data.size
            setDeleteState()
        }
    }

    private fun onClick(adapter: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, position: Int) {
        when(view.id){
            R.id.imgSelect ->{
                selectCount = 0
                val isSelect = (adapter.data[position] as MyCollectionBean).isSelect
                (adapter.data[position] as MyCollectionBean).isSelect = !isSelect
                collectionAdapter?.notifyItemChanged(position)
                //筛选出选中个数
                for (i in 0 until adapter.data.size){
                    if ((adapter.data[i] as MyCollectionBean).isSelect) selectCount++
                }
                setDeleteState()
            }
        }
    }

    /**
     * 设置删除按钮的状态
     */
    private fun setDeleteState(){
        if (selectCount >0){
            tvDelete.text = "删除$selectCount"
            tvDelete.setTextColor(Color.parseColor("#F1303C"))
            tvDelete.isEnabled = true
        }else{
            tvDelete.text = "删除"
            tvDelete.setTextColor(Color.parseColor("#99F1303C"))
            tvDelete.isEnabled = false
        }
    }

    /**
     * 编辑按钮点击事件
     */
    private fun onEditClick(){
        if (isFirst){
            isFirst = false
            layoutTopBar.setRightText("取消")
            collectionAdapter?.isEdit(true)
            layoutSelect.visibility = View.VISIBLE
        }else{
            isFirst = true
            for (i in 0 until collectionAdapter!!.data.size){
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
        val data = ArrayList<MyCollectionBean>()
        val coverUrl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fx0.ifengimg.com%2Fucms%2F2020_23%2F2F78331074BFA397F56525195BD6FBFD0B302F5B_w1153_h649.png&refer=http%3A%2F%2Fx0.ifengimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1612939527&t=96a0cc1d243f807991f354a4930ab4fc"
        for (i in 0 until 6){
            data.add(MyCollectionBean(coverUrl, "下一站是幸福","观看到第10集"))
        }
        collectionAdapter?.setList(data)

    }

}