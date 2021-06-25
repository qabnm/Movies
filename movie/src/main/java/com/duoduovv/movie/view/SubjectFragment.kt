package com.duoduovv.movie.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.duoduovv.common.util.RouterPath
import com.duoduovv.movie.adapter.MovieSubjectListAdapter
import com.duoduovv.movie.bean.SubjectListBean
import com.duoduovv.movie.databinding.FragmentSubjectBinding
import dc.android.bridge.BridgeContext.Companion.TITLE
import dc.android.bridge.view.BaseFragment

/**
 * @author: jun.liu
 * @date: 2021/6/25 10:25
 * @des:专题
 */
class SubjectFragment : BaseFragment() {
    private lateinit var mbind: FragmentSubjectBinding
    override fun initBind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSubjectBinding.inflate(inflater, container, false)
    private var adapter: MovieSubjectListAdapter? = null

    override fun initView() {
        mbind = baseBinding as FragmentSubjectBinding
        adapter = MovieSubjectListAdapter()
        mbind.rvList.adapter = adapter
        adapter?.setOnItemClickListener { adapter, _, position ->
            val dataBean = (adapter as MovieSubjectListAdapter).data[position]
            ARouter.getInstance().build(RouterPath.PATH_SUBJECT_DETAIL).withString(TITLE,dataBean.title).navigation()
        }
    }

    override fun initData() {
        val dataList = ArrayList<SubjectListBean>()
        val coverUrl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fd40f9c9850ecbc9f07745c777bff4cce515978b11dd4c-HBk0O8_fw658&refer=http%3A%2F%2Fhbimg.b0.upaiyun.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1627185096&t=aa65cecbfb54693c3f533fa99fd969b1"
        for (i in 0 until 9){
            val bean = SubjectListBean("1",coverUrl,"暑假追剧系列")
            dataList.add(bean)
        }
        adapter?.setList(dataList)
    }
}