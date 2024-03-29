package com.duoduovv.movie.component

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.duoduovv.common.component.NoLeakDialog
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.MovieEpisodesArtAdapter
import com.duoduovv.movie.bean.MovieItem
import com.duoduovv.movie.databinding.ItemMovieDetailSelectBinding

/**
 * @author: jun.liu
 * @date: 2021/3/31 17:19
 * @des:综艺类型的更多选集
 */
class MovieDetailArtSelectDialog() : DialogFragment() {
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return NoLeakDialog(requireContext(),theme)
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
        mBind.rvList.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = MovieEpisodesArtAdapter(dataList as MutableList)
        mBind.rvList.adapter = adapter
        adapter.setOnItemClickListener { ad, _, position ->
            val data = (ad as MovieEpisodesArtAdapter).data
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