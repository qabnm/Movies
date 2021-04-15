package com.duoduovv.common.view;

import android.content.Context;
import android.util.AttributeSet;

import moe.codeest.enviews.ENPlayView;

/**
 * @author: jun.liu
 * @date: 2021/4/13 15:03
 * @des:
 */
public class PlayView extends ENPlayView {
    public PlayView(Context context) {
        super(context);
    }

    public PlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DEFAULT_BG_LINE_WIDTH = 3;
        DEFAULT_LINE_WIDTH = 3;
    }
}
