package com.junliu.personal.view

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

    override fun initView() {
        rvList.layoutManager = LinearLayoutManager(this)
        collectionAdapter = MyCollectionAdapter()
        rvList.adapter = collectionAdapter
        collectionAdapter?.addChildClickViewIds(R.id.imgSelect)
        collectionAdapter?.setOnItemChildClickListener { adapter, view, position -> onClick(adapter,view,position) }
        layoutTopBar.setRightClick {  }
    }

    private fun onClick(adapter: BaseQuickAdapter<Any?, BaseViewHolder>, view: View, position: Int) {
        when(view.id){
            R.id.imgSelect ->{}
        }
    }

    override fun initData() {
        val data = ArrayList<MyCollectionBean>()
        val coverUrl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fx0.ifengimg.com%2Fucms%2F2020_23%2F2F78331074BFA397F56525195BD6FBFD0B302F5B_w1153_h649.png&refer=http%3A%2F%2Fx0.ifengimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1612939527&t=96a0cc1d243f807991f354a4930ab4fc"
        for (i in 0 until 10){
            data.add(MyCollectionBean(coverUrl, "下一站是幸福","观看到第10集"))
        }
        collectionAdapter?.setList(data)

    }

}