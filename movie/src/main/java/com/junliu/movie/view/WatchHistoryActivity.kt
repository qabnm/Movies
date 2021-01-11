package com.junliu.movie.view

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.junliu.common.util.RouterPath
import com.junliu.movie.R
import com.junliu.movie.adapter.WatchHistoryAdapter
import com.junliu.movie.bean.WatchHistoryBean
import dc.android.bridge.view.BridgeActivity
import kotlinx.android.synthetic.main.activity_watch_history.*

/**
 * @author: jun.liu
 * @date: 2021/1/6 13:28
 * @des:观看历史
 */
@Route(path = RouterPath.PATH_WATCH_HISTORY)
class WatchHistoryActivity :BridgeActivity(){
    override fun getLayoutId() = R.layout.activity_watch_history

    private var isFirst = true
    private var selectCount = 0
    private var historyAdapter:WatchHistoryAdapter?= null
    private var isAllSelect = false

    override fun initView() {
        rvList.layoutManager = LinearLayoutManager(this)
        historyAdapter = WatchHistoryAdapter()
        rvList.adapter = historyAdapter
        historyAdapter?.addChildClickViewIds(R.id.imgSelect)
        historyAdapter?.setOnItemChildClickListener { adapter, view, position -> onClick(adapter,view,position) }
        layoutTopBar.setRightClick { onEditClick() }
        tvAllSelect.setOnClickListener { allSelect() }
    }

    override fun initData() {
        val data = ArrayList<WatchHistoryBean>()
        val coverUrl = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2328588499,1361657271&fm=26&gp=0.jpg"
        for (i in 0 until 7){
            data.add(WatchHistoryBean(coverUrl, "色戒：无删减版","观看到45分32秒"))
        }
        historyAdapter?.setList(data)
    }
    /**
     * 全部选择
     */
    private fun allSelect(){
        if (!isAllSelect){
            isAllSelect = true //全选状态
            historyAdapter?.let {
                for (i in 0 until it.data.size){
                    it.data[i].isSelect = true
                }
                selectCount = it.data.size
            }
            tvAllSelect.text = "取消全选"
        }else{
            isAllSelect = false //非全选状态
            historyAdapter?.let {
                for (i in 0 until it.data.size){
                    it.data[i].isSelect = false
                }
                selectCount = 0
            }
            tvAllSelect.text = "全选"
        }
        historyAdapter?.notifyDataSetChanged()
        setDeleteState()
    }

    private fun onClick(adapter: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, position: Int) {
        when(view.id){
            R.id.imgSelect ->{
                selectCount = 0
                val isSelect = (adapter.data[position] as WatchHistoryBean).isSelect
                (adapter.data[position] as WatchHistoryBean).isSelect = !isSelect
                historyAdapter?.notifyItemChanged(position)
                //筛选出选中个数
                for (i in 0 until adapter.data.size){
                    if ((adapter.data[i] as WatchHistoryBean).isSelect) selectCount++
                }
                setDeleteState()
            }
        }
    }

    /**
     * 编辑按钮点击事件
     */
    private fun onEditClick(){
        if (isFirst){
            isFirst = false
            layoutTopBar.setRightText("取消")
            historyAdapter?.isEdit(true)
            layoutSelect.visibility = View.VISIBLE
        }else{
            isFirst = true
            for (i in 0 until historyAdapter!!.data.size){
                historyAdapter!!.data[i].isSelect = false
            }
            selectCount = 0
            layoutTopBar.setRightText("编辑")
            historyAdapter?.isEdit(false)
            layoutSelect.visibility = View.GONE
        }
        tvDelete.text = "删除"
        tvDelete.setTextColor(Color.parseColor("#99F1303C"))
        tvDelete.isEnabled = false
        historyAdapter?.notifyDataSetChanged()
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
}