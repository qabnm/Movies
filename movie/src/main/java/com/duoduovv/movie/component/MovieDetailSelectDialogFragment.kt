package com.duoduovv.movie.component

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duoduovv.movie.R
import com.duoduovv.movie.adapter.MovieDetailSelectAdapter
import com.duoduovv.movie.bean.MovieItem

/**
 * @author: jun.liu
 * @date: 2021/3/11 15:03
 * @des:
 */
class MovieDetailSelectDialogFragment(private val height: Int, private val dataList: List<MovieItem>) :
    DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val layoutView = inflater.inflate(R.layout.item_movie_detail_select, container, false)
        initViews(layoutView)
        return layoutView
    }

    private fun initViews(layoutView: View) {
        val rvList: RecyclerView = layoutView.findViewById(R.id.rvList)
        rvList.layoutManager = GridLayoutManager(requireContext(), 5)
        val adapter = MovieDetailSelectAdapter(dataList as MutableList<MovieItem>)
        rvList.adapter = adapter
        val imgCancel: ImageView = layoutView.findViewById(R.id.imgCancel)
        imgCancel.setOnClickListener { dismiss() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWindow()
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