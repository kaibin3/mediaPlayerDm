package com.example.wenjie.mediaplayerdm.PhiFind.videoplay;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wenjie.mediaplayerdm.R;
import com.example.wenjie.mediaplayerdm.util.ToastUtils;


public abstract class VideoPlayAbsControl extends FrameLayout implements VideoContract.VideoControl {
    private static final String TAG = "VideoPlayAbsControl";
    protected Context mContext;
    protected ViewGroup mContainer;
    protected LinearLayout mLoadingLayout;
    protected LinearLayout mErrorLayout;
    protected ImageView mLoadingIcon;
    protected ImageView mRetryIcon;

    protected View mMobileNetworkLayout;
    protected View mContinuePlayView;
    protected Drawable mLoadingDrawable;
    protected Drawable mBackDrawable;
    protected Drawable mReplayDrawable;

    protected boolean errorLayoutShow;

    public VideoPlayAbsControl(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public VideoPlayAbsControl(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public void startPlay() {

    }

    @Override
    public void onPlayerStart() {

    }

    @Override
    public void setVideoPlayer(VideoContract.VideoPlayer mediaPlayer) {

    }

    @Override
    public void setScreenMode(int screenMode) {

    }

    @Override
    public void show() {

    }

    @Override
    public void dismiss() {

    }

    @Override
    public void setImage(String url) {

    }

    @Override
    public void setTitle(String title) {

    }

    public abstract void onPlayCompletion();

    @Override
    public void showLoading() {
        mLoadingLayout = new LinearLayout(getContext());
        mLoadingLayout.setOrientation(LinearLayout.VERTICAL);
        ImageView imageView = new ImageView(mContext);
        imageView.setImageDrawable(mLoadingDrawable);
        mLoadingLayout.addView(imageView);
        FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        addView(mLoadingLayout, layoutParams);
        NiceUtil.rotationAnimator(imageView);
    }

    @Override
    public void dismissLoading() {
        if (null != mLoadingLayout) {
            removeView(mLoadingLayout);
            mLoadingLayout = null;
        }
    }

    @Override
    public void showNoNetwork() {
        ToastUtils.toastShow(mContext.getResources().getString(R.string.please_check_net));
    }


    @Override
    public void showOnMobileNetwork() {
        mMobileNetworkLayout = LayoutInflater.from(mContext).inflate(R.layout.on_mobile_network, this, false);
        mContinuePlayView = mMobileNetworkLayout.findViewById(R.id.continue_play_view);
        mContinuePlayView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ignoreMobileNetwork();
            }
        });
        FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mMobileNetworkLayout, layoutParams);
    }


    public void ignoreMobileNetwork() {
        removeView(mMobileNetworkLayout);
        startPlay();
    }


    @Override
    public void showOnError() {
        dismissError();
        if (null == mErrorLayout) {
            mErrorLayout = new LinearLayout(getContext());
            mErrorLayout.setOrientation(LinearLayout.VERTICAL);
            mRetryIcon = new ImageView(mContext);
            mRetryIcon.setImageDrawable(mReplayDrawable);
            mRetryIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    retryOnError();
                }
            });
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            imageParams.gravity = Gravity.CENTER;
            mErrorLayout.addView(mRetryIcon, imageParams);
            TextView textView = new TextView(mContext);
            textView.setText("不小心出错了，请点击重新加载");
            textView.setTextSize(14);
            textView.setTextColor(Color.WHITE);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            textParams.setMargins(0, NiceUtil.dp2px(mContext, 15), 0, 0);
            mErrorLayout.addView(textView, textParams);
        }
        FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        this.setVisibility(VISIBLE);
        addView(mErrorLayout, layoutParams);
        errorLayoutShow = true;
    }

    @Override
    public void dismissError() {
        if (null != mErrorLayout) {
            removeView(mErrorLayout);
            errorLayoutShow = false;
        }
    }

    protected abstract void retryOnError();

    public abstract void onPlayError();

    public abstract void cancel();
}
