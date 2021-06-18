package com.duoduovv.common.util;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duoduovv.common.R;
import com.qq.e.ads.nativ.MediaView;
import com.qq.e.ads.nativ.widget.NativeAdContainer;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;

import moe.codeest.enviews.ENPlayView;

/**
 * @author: jun.liu
 * @date: 2021/1/14 15:27
 * @des:
 */
public class SampleCoverVideo extends StandardGSYVideoPlayer {
    ImageView mCoverImage;

    String mCoverOriginUrl;

    int mCoverOriginId = 0;

    int mDefaultRes;

    public SampleCoverVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public SampleCoverVideo(Context context) {
        super(context);
    }

    public SampleCoverVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private ImageView imgPlayPause;
    private ImageView imgNext;
    private FrameLayout layoutLoading;
    private LottieAnimationView videoLoading;
    private ImageView imgBackLoad;
    private NativeAdContainer layoutAd;
    private TextView tvSkip;
    private MediaView mediaView;
    private ImageView adImgCover;

    @Override
    protected void init(Context context) {
        super.init(context);
        mCoverImage = findViewById(R.id.thumbImage);
        imgPlayPause = findViewById(R.id.imgPlayPause);
        imgNext = findViewById(R.id.imgPlayNext);
        layoutLoading = findViewById(R.id.layoutLoading);
        videoLoading = findViewById(R.id.videoPrepare);
        imgBackLoad = findViewById(R.id.imgBackLoad);
        layoutAd = findViewById(R.id.layoutAd);
        tvSkip = findViewById(R.id.tvSkip);
        mediaView = findViewById(R.id.mediaView);
        adImgCover = findViewById(R.id.adImgCover);

        if (mThumbImageViewLayout != null &&
                (mCurrentState == -1 || mCurrentState == CURRENT_STATE_NORMAL || mCurrentState == CURRENT_STATE_ERROR)) {
            mThumbImageViewLayout.setVisibility(VISIBLE);
        }
        imgPlayPause.setOnClickListener(v -> clickStartIcon());
        imgNext.setOnClickListener(v -> {
            if (null != onNextClickListener) onNextClickListener.onNextClick();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_layout_cover;
    }

    public void loadCoverImage(Context context,String url, int res) {
        mCoverOriginUrl = url;
        mDefaultRes = res;
        RequestOptions options = new RequestOptions().centerCrop().error(res).placeholder(res);
        Glide.with(context).load(url).apply(options).into(mCoverImage);
    }

    public void loadCoverImageBy(int id, int res) {
        mCoverOriginId = id;
        mDefaultRes = res;
        mCoverImage.setImageResource(id);
    }

    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        GSYBaseVideoPlayer gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
        SampleCoverVideo sampleCoverVideo = (SampleCoverVideo) gsyBaseVideoPlayer;
        if (mCoverOriginUrl != null) {
            sampleCoverVideo.loadCoverImage(context,mCoverOriginUrl, mDefaultRes);
        } else if (mCoverOriginId != 0) {
            sampleCoverVideo.loadCoverImageBy(mCoverOriginId, mDefaultRes);
        }
        return gsyBaseVideoPlayer;
    }


    @Override
    public GSYBaseVideoPlayer showSmallVideo(Point size, boolean actionBar, boolean statusBar) {
        //下面这里替换成你自己的强制转化
        SampleCoverVideo sampleCoverVideo = (SampleCoverVideo) super.showSmallVideo(size, actionBar, statusBar);
        sampleCoverVideo.mStartButton.setVisibility(GONE);
        sampleCoverVideo.mStartButton = null;
        return sampleCoverVideo;
    }

    @Override
    protected void cloneParams(GSYBaseVideoPlayer from, GSYBaseVideoPlayer to) {
        super.cloneParams(from, to);
        SampleCoverVideo sf = (SampleCoverVideo) from;
        SampleCoverVideo st = (SampleCoverVideo) to;
        st.mShowFullAnimation = sf.mShowFullAnimation;
    }


    /**
     * 退出window层播放全屏效果
     */
    @SuppressWarnings("ResourceType")
    @Override
    protected void clearFullscreenLayout() {
        if (!mFullAnimEnd) {
            return;
        }
        mIfCurrentIsFullscreen = false;
        int delay = 0;
        if (mOrientationUtils != null) {
            delay = mOrientationUtils.backToProtVideo();
            mOrientationUtils.setEnable(false);
            if (mOrientationUtils != null) {
                mOrientationUtils.releaseListener();
                mOrientationUtils = null;
            }
        }

        if (!mShowFullAnimation) {
            delay = 0;
        }

        final ViewGroup vp = (CommonUtil.scanForActivity(getContext())).findViewById(Window.ID_ANDROID_CONTENT);
        final View oldF = vp.findViewById(getFullId());
        if (oldF != null) {
            //此处fix bug#265，推出全屏的时候，虚拟按键问题
            SampleCoverVideo gsyVideoPlayer = (SampleCoverVideo) oldF;
            gsyVideoPlayer.mIfCurrentIsFullscreen = false;
        }

        if (delay == 0) {
            backToNormal();
        } else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    backToNormal();
                }
            }, delay);
        }

    }


    /******************* 下方两个重载方法，在播放开始前不屏蔽封面，不需要可屏蔽 ********************/
    @Override
    public void onSurfaceUpdated(Surface surface) {
        super.onSurfaceUpdated(surface);
        if (mThumbImageViewLayout != null && mThumbImageViewLayout.getVisibility() == VISIBLE) {
            mThumbImageViewLayout.setVisibility(INVISIBLE);
        }
    }

    @Override
    protected void setViewShowState(View view, int visibility) {
        if (view == mThumbImageViewLayout && visibility != VISIBLE) {
            return;
        }
        super.setViewShowState(view, visibility);
    }

    @Override
    public void onSurfaceAvailable(Surface surface) {
        super.onSurfaceAvailable(surface);
        if (GSYVideoType.getRenderType() != GSYVideoType.TEXTURE) {
            if (mThumbImageViewLayout != null && mThumbImageViewLayout.getVisibility() == VISIBLE) {
                mThumbImageViewLayout.setVisibility(INVISIBLE);
            }
        }
    }

    /******************* 下方重载方法，在播放开始不显示底部进度和按键，不需要可屏蔽 ********************/

    protected boolean byStartedClick;

    @Override
    protected void onClickUiToggle(MotionEvent e) {
        if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull) {
            setViewShowState(mLockScreen, VISIBLE);
            return;
        }
        byStartedClick = true;
        super.onClickUiToggle(e);

    }

    @Override
    protected void changeUiToNormal() {
        super.changeUiToNormal();
        byStartedClick = false;
    }

    @Override
    protected void changeUiToPreparingShow() {
        super.changeUiToPreparingShow();
        Debuger.printfLog("Sample changeUiToPreparingShow");
        setViewShowState(mBottomContainer, INVISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
    }

    @Override
    protected void changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow();
        Debuger.printfLog("Sample changeUiToPlayingBufferingShow");
        if (!byStartedClick) {
            setViewShowState(mBottomContainer, INVISIBLE);
            setViewShowState(mStartButton, INVISIBLE);
        }
    }

    @Override
    protected void changeUiToPauseShow() {
        super.changeUiToPauseShow();
        //父类代码495行
        if (mIfCurrentIsFullscreen){
            setViewShowState(mStartButton, INVISIBLE);
        }
    }

    @Override
    protected void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
        Debuger.printfLog("Sample changeUiToPlayingShow");
        if (!byStartedClick) {
            setViewShowState(mBottomContainer, INVISIBLE);
            setViewShowState(mStartButton, INVISIBLE);
        }
        if (mIfCurrentIsFullscreen){
            //如果是全屏
            imgPlayPause.setVisibility(View.VISIBLE);
            imgNext.setVisibility(View.VISIBLE);
            mStartButton.setVisibility(View.GONE);
        }else{
            imgPlayPause.setVisibility(View.GONE);
            imgNext.setVisibility(View.GONE);
        }
    }

    @Override
    public void startAfterPrepared() {
        super.startAfterPrepared();
        Debuger.printfLog("Sample startAfterPrepared");
        setViewShowState(mBottomContainer, INVISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
        setViewShowState(mBottomProgressBar, VISIBLE);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        byStartedClick = true;
        super.onStartTrackingTouch(seekBar);
    }

    @Override
    protected void clickStartIcon() {
        Log.i("movieDetail", "****clickStartIcon这里执行了：flag=" + flag);
        if (flag == 1) {
            super.clickStartIcon();
        } else {
            if (null != listener) listener.onStartClick();
        }
    }

    public interface OnStartClickListener {
        void onStartClick();
    }

    /**
     * 播放按钮逻辑
     * pase状态 时候会回调这个方法
     */
    @Override
    protected void updateStartImage() {
        if (mStartButton instanceof ENPlayView) {
            ENPlayView enPlayView = (ENPlayView) mStartButton;
            enPlayView.setDuration(500);
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                enPlayView.play();
            } else if (mCurrentState == CURRENT_STATE_ERROR) {
                enPlayView.pause();
            } else {
                enPlayView.pause();
            }
        } else if (mStartButton instanceof ImageView) {
            ImageView imageView = (ImageView) mStartButton;
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                imageView.setImageResource(R.drawable.selector_player_pause);
                imgPlayPause.setImageResource(R.drawable.selector_player_pause);
            } else if (mCurrentState == CURRENT_STATE_ERROR) {
                imageView.setImageResource(R.drawable.video_click_error_selector);
            } else {
                imageView.setImageResource(R.drawable.selector_player_play);
                imgPlayPause.setImageResource(R.drawable.selector_player_play);
            }
        }
    }

    /**
     * 全屏锁屏的逻辑 可以修改锁频的图片
     */
    @Override
    protected void lockTouchLogic() {
        super.lockTouchLogic();
    }

    private OnStartClickListener listener;
    private int flag;

    public void setStartClickListener(OnStartClickListener listener) {
        this.listener = listener;
    }

    public void setStartClick(int flag) {
        this.flag = flag;
    }
    private OnNextClickListener onNextClickListener;
    public void setOnNextClick(OnNextClickListener onNextClickListener){
        this.onNextClickListener = onNextClickListener;
    }

    public interface OnNextClickListener{
        void onNextClick();
    }

    public void pauseLoading(){
        videoLoading.cancelAnimation();
        layoutLoading.setVisibility(View.GONE);
    }

    public void playLoading(){
        layoutLoading.setVisibility(View.VISIBLE);
        videoLoading.playAnimation();
    }

    public ImageView getBackLoad(){
        return imgBackLoad;
    }

    public NativeAdContainer getLayoutAd(){
        return layoutAd;
    }

    public TextView getTvSkip(){
        return tvSkip;
    }

    public MediaView getMediaView(){
        return  mediaView;
    }

    public ImageView getAdImgCover(){
        return adImgCover;
    }
}
