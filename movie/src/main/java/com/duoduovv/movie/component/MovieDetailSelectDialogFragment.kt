package com.duoduovv.movie.component

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.MovieDetailSelectAdapter
import com.duoduovv.movie.bean.MovieItem
import com.duoduovv.movie.databinding.ItemMovieDetailSelectBinding

/**
 * @author: jun.liu
 * @date: 2021/3/11 15:03
 * @des:电视剧类型的选集
 */
class MovieDetailSelectDialogFragment() : DialogFragment() {
    private lateinit var mBind: ItemMovieDetailSelectBinding
    private var height = 0
    private lateinit var dataList: List<MovieItem>
    private var listener: OnSelectDialogItemClickListener? = null

    constructor(
        height: Int,
        dataList: List<MovieItem>,
        listener: OnSelectDialogItemClickListener?
    ) : this() {
        this.height = height
        this.dataList = dataList
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        mBind = ItemMovieDetailSelectBinding.inflate(inflater, container, false)
        initViews()
        return mBind.root
    }

    private fun initViews() {
        mBind.rvList.layoutManager = GridLayoutManager(requireContext(), 5)
        val adapter = MovieDetailSelectAdapter(dataList as MutableList<MovieItem>)
        mBind.rvList.adapter = adapter
        adapter.setOnItemClickListener { ad, _, position ->
            val data = (ad as MovieDetailSelectAdapter).data
            for (i in data.indices) {
                data[i].isSelect = false
            }
            data[position].isSelect = true
            ad.notifyDataSetChanged()
            val vid = data[position].vid
            listener?.onDialogClick(vid, data[position].title, data[position].vip)
            dismiss()
        }
        mBind.imgCancel.setOnClickListener { dismiss() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
    }

    interface OnSelectDialogItemClickListener {
        fun onDialogClick(vid: String, vidTitle: String, vip: String?)
    }

    /**
     * 设置dialog宽高
     */
    private fun initWindow() {
        val window = dialog?.window
        window?.let {
            it.decorView.setPadding(0, 0, 0, 0)
            it.attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            it.attributes.height = height
            it.attributes.gravity = Gravity.BOTTOM
            it.setBackgroundDrawableResource(R.color.colorFFFFFF)
            it.attributes.windowAnimations = R.style.BottomToTopAnim
            it.setDimAmount(0f)
        }
    }
}