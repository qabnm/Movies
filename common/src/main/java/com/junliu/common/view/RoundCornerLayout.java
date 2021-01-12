package com.junliu.common.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;

import com.junliu.common.R;

/**
 * @author : liujun
 * @date : 2020-03-05 09:53
 * @description: 圆角view
 */
public class RoundCornerLayout extends RelativeLayout {
    private float radius;
    private int solidColor;
    private GradientDrawable gradientDrawable;

    public RoundCornerLayout(Context context) {
        this(context, null);
    }

    public RoundCornerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attr) {
        if (null != attr) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attr, R.styleable.RoundCornerLayout);
            radius = typedArray.getDimension(R.styleable.RoundCornerLayout_radius, 0);
            solidColor = typedArray.getColor(R.styleable.RoundCornerLayout_solidColor, 0);
            typedArray.recycle();
        }
    }

    private void init() {
        setGravity(Gravity.CENTER);
        gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(radius);
        gradientDrawable.setColor(solidColor);
        setBackground(gradientDrawable);
    }

    public void setSolidColor(int solidColor){
        if (null != gradientDrawable){
            gradientDrawable.setColor(solidColor);
            setBackground(gradientDrawable);
        }
    }
}
