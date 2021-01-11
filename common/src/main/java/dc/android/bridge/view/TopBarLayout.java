package dc.android.bridge.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junliu.common.R;


/**
 * @author: jun.liu
 * @date: 2020/8/25 : 11:29
 */
public class TopBarLayout extends RelativeLayout {
    private int imgSrc;
    private String contentTitle;
    private int contentTitleColor;
    private float contentTitleSize;
    private OnBackClickListener backClickListener;
    private OnRightClickListener rightClickListener;
    private TextView tvTitle;
    private TextView tvRight;
    private int visible;//1 按钮可见  2 按钮隐藏不显示
    private String rightText;
    private int rightTextColor;
    private float rightTextSize;

    public TopBarLayout(Context context) {
        this(context, null);
    }

    public TopBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化相关属性值
     *
     * @param context
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TopBarLayout);
        imgSrc = array.getResourceId(R.styleable.TopBarLayout_backSrc, R.drawable.back);
        contentTitle = array.getString(R.styleable.TopBarLayout_contentTitle);
        contentTitleColor = array.getColor(R.styleable.TopBarLayout_contentTextColor, Color.BLACK);
        contentTitleSize = array.getDimension(R.styleable.TopBarLayout_contentTextSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,17,context.getResources().getDisplayMetrics()));
        visible = array.getInt(R.styleable.TopBarLayout_rightVisible, 2);
        rightText = array.getString(R.styleable.TopBarLayout_rightText);
        rightTextColor = array.getColor(R.styleable.TopBarLayout_rightTextColor, Color.parseColor("#333333"));
        rightTextSize = array.getDimension(R.styleable.TopBarLayout_rightTextSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,context.getResources().getDisplayMetrics()));

        array.recycle();
        initLayout(context);
    }

    private void initLayout(final Context context) {
        View layoutView = View.inflate(context, R.layout.layout_top_bar, this);
        ImageView ivBack = layoutView.findViewById(R.id.iv_back);
        tvTitle = layoutView.findViewById(R.id.tv_title);
        tvRight = layoutView.findViewById(R.id.tv_sure);
        tvRight.setVisibility(1 == visible ? View.VISIBLE : View.GONE);
        tvTitle.setText(contentTitle);
        tvTitle.setTextColor(contentTitleColor);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentTitleSize);
        ivBack.setImageResource(imgSrc);
        ivBack.setOnClickListener(v -> {
            //如果没有主动设置返回键的点击监听 默认就是finish当前页面
            if (null == backClickListener) {
                if (context instanceof Activity) ((Activity) context).finish();
            } else {
                //如果用户自己设置了返回监听，就实现自己的业务逻辑
                backClickListener.onBackClick();
            }
        });
        if (1 == visible) {
            tvRight.setText(rightText);
            tvRight.setTextColor(rightTextColor);
            tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
            tvRight.setOnClickListener(v -> {
                if (null != rightClickListener) rightClickListener.onRightClick();
            });
        }
    }

    public void setTopTitle(String topTitle) {
        tvTitle.setText(topTitle);
    }

    public void setRightText(String text){
        tvRight.setText(text);
    }

    public interface OnBackClickListener {
        void onBackClick();
    }

    public interface OnRightClickListener {
        void onRightClick();
    }

    public void setRightClick(OnRightClickListener rightClickListener) {
        this.rightClickListener = rightClickListener;
    }

    public void setBackClickListener(OnBackClickListener backClickListener) {
        this.backClickListener = backClickListener;
    }
}
