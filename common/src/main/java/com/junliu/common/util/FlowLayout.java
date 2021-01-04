package com.junliu.common.util;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junliu.common.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dc.android.bridge.util.OsUtils;

/**
 * @author : liujun
 * @date : 2020-03-16
 * @description:
 */
public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //   super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //遍历去调用所有子元素的measure方法（child.getMeasuredHeight()才能获取到值，否则为0）
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = 0, measuredHeight = 0;

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //由于计算子view所占宽度，这里传值需要自身减去PaddingRight宽度，PaddingLeft会在接下来计算子元素位置时加上
        Map<String, Integer> compute = compute(widthSize - getPaddingRight());

        //EXACTLY模式：对应于给定大小或者match_parent情况
        if (widthMode == MeasureSpec.EXACTLY) {
            measuredWidth = widthSize;
            //AT_MOS模式：对应wrap-content（需要手动计算大小，否则相当于match_parent）
        } else if (widthMode == MeasureSpec.AT_MOST) {
            measuredWidth = compute.get("allChildWidth");
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            measuredHeight = heightSize;
        } else {
            measuredHeight = compute.get("allChildHeight");
        }
        //设置flow的宽高
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            Rect rect = (Rect) getChildAt(i).getTag();
            child.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    public void clear(){
        removeAllViews();
    }

    public void setData(List<String> listData) {
        if (null != listData && listData.size() > 0) {
            //往容器内添加TextView数据
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, OsUtils.dip2px(getContext(), 10), OsUtils.dip2px(getContext(), 10));
            removeAllViews();
            for (int i = 0; i < listData.size(); i++) {
                TextView tv = new TextView(getContext());
                tv.setPadding(OsUtils.dip2px(getContext(), 10), 0, OsUtils.dip2px(getContext(), 10), 0);
                tv.setText(listData.get(i));
                tv.setSingleLine();
                layoutParams.height = OsUtils.dip2px(getContext(), 28);
                tv.setEllipsize(TextUtils.TruncateAt.END);
                tv.setTextColor(getResources().getColor(R.color.color000000));
                tv.setBackground(getResources().getDrawable(R.drawable.shape_radius15_solid_f5f5f5));
                tv.setLayoutParams(layoutParams);
                tv.setGravity(Gravity.CENTER);
                tv.setOnClickListener(new ItemTextClickListener(listData.get(i), i));
                addView(tv, layoutParams);
            }
        }
    }

    // TextView 设置点击事件
    private class ItemTextClickListener implements OnClickListener {
        private String result;
        private int position;

        public ItemTextClickListener(String result, int position) {
            this.result = result;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            if (null != onItemClickListener) onItemClickListener.OnItemClick(result);
//            if (null != onItemClickListener) {
//                if (getChildCount() > 0) {
//                    for (int i = 0; i < getChildCount(); i++) {
//                        TextView tvText = (TextView) getChildAt(i);
//                        if (null != tvText) {
//                            if (i == position) {
//                                tvText.setTextColor(getResources().getColor(R.color.white));
//                                tvText.setBackground(getResources().getDrawable(R.drawable.shape_radius15_3973ff));
//                            } else {
//                                tvText.setTextColor(getResources().getColor(R.color.color_303030));
//                                tvText.setBackground(getResources().getDrawable(R.drawable.shape_radius15_stroke_ebebeb));
//                            }
//                        }
//                    }
//                }
//                onItemClickListener.OnItemClick(result);
//            }
        }
    }

    /**
     * 设置选中状态
     *
     * @param index
     */
    public void setSelect(int index) {
        TextView tvText = (TextView) getChildAt(index);
//        tvText.setTextColor(getResources().getColor(R.color.white));
//        tvText.setBackground(getResources().getDrawable(R.drawable.shape_radius15_3973ff));
    }

    /**
     * Item点击回调
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void OnItemClick(String result);

        void isSingLine(boolean isSingLine);
    }

    private boolean isSingLine;

    public boolean isSingLine() {
        return isSingLine;
    }

    /**
     * 测量过程
     *
     * @param flowWidth 该view的宽度
     * @return 返回子元素总所占宽度和高度（用于计算Flowlayout的AT_MOST模式设置宽高）
     */
    private Map<String, Integer> compute(int flowWidth) {
        //是否是单行
        boolean aRow = true;
        MarginLayoutParams marginParams;//子元素margin
        int rowsWidth = getPaddingLeft();//当前行已占宽度(注意需要加上paddingLeft)
        int columnHeight = getPaddingTop();//当前行顶部已占高度(注意需要加上paddingTop)
        int rowsMaxHeight = 0;//当前行所有子元素的最大高度（用于换行累加高度）

        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            //获取元素测量宽度和高度
            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();
            //获取元素的margin
            marginParams = (MarginLayoutParams) child.getLayoutParams();
            //子元素所占宽度 = MarginLeft+ child.getMeasuredWidth+MarginRight  注意此时不能child.getWidth,因为界面没有绘制完成，此时wdith为0
            int childWidth = marginParams.leftMargin + marginParams.rightMargin + measuredWidth;
            int childHeight = marginParams.topMargin + marginParams.bottomMargin + measuredHeight;
            //判断是否换行： 该行已占大小+该元素大小>父容器宽度  则换行

            rowsMaxHeight = Math.max(rowsMaxHeight, childHeight);
            //换行
            if (rowsWidth + childWidth > flowWidth) {
                //重置行宽度
                rowsWidth = getPaddingLeft() + getPaddingRight();
                //累加上该行子元素最大高度
                columnHeight += rowsMaxHeight;
                //重置该行最大高度
                rowsMaxHeight = childHeight;
                aRow = false;
            }
            //累加上该行子元素宽度
            rowsWidth += childWidth;
            //判断时占的宽段时加上margin计算，设置顶点位置时不包括margin位置，不然margin会不起作用，这是给View设置tag,在onlayout给子元素设置位置再遍历取出
            child.setTag(new Rect(rowsWidth - childWidth + marginParams.leftMargin, columnHeight + marginParams.topMargin, rowsWidth - marginParams.rightMargin, columnHeight + childHeight - marginParams.bottomMargin));
        }
        isSingLine = aRow;
        if (null != onItemClickListener) onItemClickListener.isSingLine(isSingLine);

        //返回子元素总所占宽度和高度（用于计算Flowlayout的AT_MOST模式设置宽高）
        Map<String, Integer> flowMap = new HashMap<>();
        //单行
        if (aRow) {
            flowMap.put("allChildWidth", rowsWidth);
        } else {
            //多行
            flowMap.put("allChildWidth", flowWidth);
        }
        //FlowLayout测量高度 = 当前行顶部已占高度 +当前行内子元素最大高度+FlowLayout的PaddingBottom
        flowMap.put("allChildHeight", columnHeight + rowsMaxHeight + getPaddingBottom());
        return flowMap;
    }
}
