package dc.android.bridge.util;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;


import com.duoduovv.common.R;

import dc.android.bridge.view.BaseActivity;


/**
 * @author: jun.liu
 * @date: 2020/8/24 : 17:48
 * 状态栏文字显示颜色 以及状态栏颜色
 */
public class StatusBarWrapper {
    private boolean isDark;
    private BaseActivity activity;
    protected boolean FLAG_BAR_NAV = true;
    protected FrameLayout layoutContent;
    protected View vStatusBar;
    private boolean showBarView = true;

    public StatusBarWrapper(BaseActivity activity) {
        this.activity = activity;
    }

    public void onCreate(View contentView){
        activity.setContentView(R.layout.activity_content_source);
        layoutContent = activity.findViewById(R.id.layout_content_source);
        vStatusBar = activity.findViewById(R.id.v_bar_source);
        if (!showBarView){
            vStatusBar.setVisibility(View.GONE);
        }else {
            ViewGroup.LayoutParams params = vStatusBar.getLayoutParams();
            params.height = OsUtils.getStatusBarHeight(activity);
            vStatusBar.setLayoutParams(params);
        }
        layoutContent.addView(contentView);
    }

    /**
     * 设置状态栏文字颜色 和状态栏的颜色
     * 白色 setStatusBarColor(true,color)
     * 其他颜色setStatusBarColor(false,color)
     *
     * @param fontIconDark        状态栏字体和图标是否是深色
     * @param statusBarPlaceColor 状态栏颜色
     */
    public void setStatusBarColor(boolean fontIconDark, int statusBarPlaceColor) {
        if (fontIconDark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || OsUtils.isFlyme() || OsUtils.isMIUI()) {
                setStatusBarFontIconDark(true);
            } else {
                if (statusBarPlaceColor == Color.WHITE || statusBarPlaceColor == Color.TRANSPARENT) {
                    statusBarPlaceColor = 0xffcccccc;
                }
            }
        }
        if (null != vStatusBar) vStatusBar.setBackgroundColor(statusBarPlaceColor);
    }

    /**
     * 设置状态栏字体颜色，状态栏为亮色时候字体和图标是黑色，状态栏为暗色时候，字体和图标为白色
     *
     * @param isDark 状态栏字体是否为深色
     */
    private void setStatusBarFontIconDark(boolean isDark) {
//        // 小米MIUI
//        try {
//            Window window = activity.getWindow();
//            Class clazz = activity.getWindow().getClass();
//            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
//            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
//            int darkModeFlag = field.getInt(layoutParams);
//            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
//            if (isDark) {    //状态栏亮色且黑色字体
//                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
//            } else {       //清除黑色字体
//                extraFlagField.invoke(window, 0, darkModeFlag);
//            }
//        } catch (Exception e) {
////            e.printStackTrace();
//            Logger.w(e.getMessage());
//        }

        // 魅族FlymeUI
//        try {
//            Window window = activity.getWindow();
//            WindowManager.LayoutParams lp = window.getAttributes();
//            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
//            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
//            darkFlag.setAccessible(true);
//            meizuFlags.setAccessible(true);
//            int bit = darkFlag.getInt(null);
//            int value = meizuFlags.getInt(lp);
//            if (isDark) {
//                value |= bit;
//            } else {
//                value &= ~bit;
//            }
//            meizuFlags.setInt(lp, value);
//            window.setAttributes(lp);
//        } catch (Exception e) {
////            e.printStackTrace();
//            Logger.w(e.getMessage());
//        }

        // android6.0+系统
        // 这个设置和在xml的style文件中用这个<item name="android:windowLightStatusBar">true</item>属性是一样的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isDark) {
                //规避StatusBar与NavBar冲突问题
                this.isDark = true;
                setBars();
            }
        }
    }

    /**
     * 安卓6.0以上设置状态栏颜色
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private void setBars() {
        if (isDark && !FLAG_BAR_NAV) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else if (isDark) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else if (!FLAG_BAR_NAV) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void setShowNav(boolean isShow) {
        this.FLAG_BAR_NAV = isShow;
    }

    public void showStatusBarView(boolean showBarView){
        this.showBarView = showBarView;
    }

    public void setStatusBarVisible(int visible){
        vStatusBar.setVisibility(visible);
    }
}
