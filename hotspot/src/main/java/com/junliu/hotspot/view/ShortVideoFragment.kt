package com.junliu.hotspot.view

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.junliu.hotspot.R
import com.junliu.hotspot.adapter.ShortVideoAdapter
import com.junliu.hotspot.bean.ShortVideoBean
import com.shuyu.gsyvideoplayer.GSYVideoManager
import dc.android.bridge.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_short_video.*

/**
 * @author: jun.liu
 * @date: 2021/1/6 14:49
 * @des:短视频页面
 */
class ShortVideoFragment : BaseFragment() {
    private var videoAdapter: ShortVideoAdapter? = null
    override fun getLayoutId() = R.layout.fragment_short_video

    override fun initView() {
        rvList.layoutManager = LinearLayoutManager(requireActivity())
        videoAdapter = ShortVideoAdapter()
        rvList.adapter = videoAdapter
        videoAdapter?.setOnItemChildClickListener(object :OnItemChildClickListener{
            override fun onItemChildClick(
                adapter: BaseQuickAdapter<*, *>,
                view: View,
                position: Int
            ) {
            }
        })
    }

    override fun initData() {
        val coverUrlYang =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20170303%2Fd82d647a0cdc44c5949575aacb70dcbc_th.jpg&refer=http%3A%2F%2Fimg.mp.itc.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1612584192&t=0260f8594a524383579d0803129e5b17"
        val coverUrlFan =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fdesk-fd.zol-img.com.cn%2Ft_s960x600c5%2Fg5%2FM00%2F02%2F00%2FChMkJlbKw46ILWkiAASCHR8-zqwAALG5AA9Ep0ABII1400.jpg&refer=http%3A%2F%2Fdesk-fd.zol-img.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1612590075&t=c1bae58802aff8e4b3f7aae0b4a552a7"
        val url1 = "http://mudan.iii-kuyunzy.com/20200121/10197_bbd984b4/index.m3u8"
        val url2 = "http://down2.okdown10.com/20210105/2642_e5ede2d1/25岁当代单身女性尝试相亲APP的成果日记.EP03.mp4"
        val data = ArrayList<ShortVideoBean>()
        for (i in 0 until 20){
            if (i % 2 == 0){
                data.add(ShortVideoBean("25岁当代单身女性尝试相亲APP的成果日记",coverUrlYang,url1))
            }else{
                data.add(ShortVideoBean("25岁当代单身女性尝试相亲APP的成果日记",coverUrlFan,url2))
            }
        }
        videoAdapter?.setList(data)
    }
}