package com.example.wenjie.mediaplayerdm.PhiFind.mediaPlay;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.wenjie.mediaplayerdm.R;
import com.example.wenjie.mediaplayerdm.util.AnimationUtils;
import com.example.wenjie.mediaplayerdm.util.ToastUtils;


public abstract class VideoPlayAbsControl extends FrameLayout implements VideoContract.VideoControl {

    protected Context mContext;
    protected ViewGroup mContainer;
    protected LinearLayout mLoadingView;

    protected View mMobileNetworkLayout;
    protected View mContinuePlayView;


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
    public void setImage(String uri) {

    }

    @Override
    public void setTitle(String title) {

    }


    public void showLoading() {
        mLoadingView = new LinearLayout(getContext());
        mLoadingView.setOrientation(LinearLayout.VERTICAL);
        ImageView imageView = new ImageView(mContext);
        imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_loading2));
        mLoadingView.addView(imageView);
        FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        addView(mLoadingView, layoutParams);
        AnimationUtils.rotationAnimator(imageView);
    }


    public void dismissLoading() {
        if (null != mLoadingView) {
            removeView(mLoadingView);
        }
    }

    protected void showNoNetwork() {
        ToastUtils.toastShow(mContext.getResources().getString(R.string.please_check_net));
    }


    public void showOnMobileNetwork() {

        /*LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.parseColor("#FF000000"));

        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.gravity = Gravity.CENTER_HORIZONTAL;

        TextView textView = new TextView(mContext);
        textView.setTextSize(11);
        textView.setTextColor(Color.WHITE);
        textView.setText("在非Wi-Fi网络观看，将消耗流量");
        layout.addView(textView, layoutParams1);
        TextView keepOn = new TextView(mContext);
        keepOn.setTextSize(13);
        keepOn.setTextColor(Color.WHITE);
        keepOn.setText("继续播放");
        keepOn.setBackground(mContext.getResources().getDrawable(R.drawable.text_bg));
        layout.addView(keepOn, layoutParams1);

        FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        addView(layout, layoutParams);*/
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


 /*   public abstract void onPlayStart();

    public abstract void setVideoPlayer(VideoContract.VideoPlayer mediaPlayer);

    //public abstract void setControllerImp(VideoContract.VideoControl videoControl);
    public abstract void setScreenMode(int screenMode);

    public abstract void show();

    public abstract void hide();

    public abstract void setImage(String uri);*/



   /* public abstract void play();

    public abstract void pause();

    public abstract void fullScreen();

    public abstract void exitScreen();

    public abstract void setPhotoUri();

    public abstract void setTitle();

    public abstract void showNetError();*/


    public void dismissAlpha() {
        //ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mPlayControlView, "alpha", 1f, 0f);
        //objectAnimator.start();
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mContainer.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        startAnimation(alphaAnimation);

    }

}
