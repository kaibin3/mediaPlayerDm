package com.example.wenjie.mediaplayerdm.PhiFind.mediaPlay;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.wenjie.mediaplayerdm.R;
import com.example.wenjie.mediaplayerdm.util.AnimationUtils;


public abstract class VideoPlayAbsControl extends FrameLayout implements VideoContract.VideoControl {

    protected Context mContext;
    protected ViewGroup mContainer;
    protected LinearLayout mLoadingView;

    public VideoPlayAbsControl(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public VideoPlayAbsControl(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
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


    public void showWifiTips() {

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


    public void dismiss1() {
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
