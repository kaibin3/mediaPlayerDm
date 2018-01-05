package com.example.wenjie.mediaplayerdm.PhiFind;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.wenjie.mediaplayerdm.R;

/**
 * Created by wen.jie on 2018/1/5.
 */

public class VideoPlayControlView extends RelativeLayout {
    private static final String TAG = "VideoPlayControlView";
    private TextView mCurrentTimeView;
    private TextView duration_view;
    private SeekBar seek_bar;

    private VideoPlayView videoPlayView;
    private int mDuration;

    public VideoPlayControlView(Context context) {
        super(context);
        init();
    }

    public VideoPlayControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.video_play_control_layout, this);
        mCurrentTimeView = findViewById(R.id.progress_view);
        duration_view = findViewById(R.id.duration_view);
        seek_bar = findViewById(R.id.seek_bar);
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // called with: seekBar = [android.widget.SeekBar{33ff4aa6 VFED.... ...P..ID 145,0-685,96 #7f0800c8 app:id/seek_bar}], progress = [78], fromUser = [true]
                Log.d(TAG, "onProgressChanged() called with: seekBar = [" + seekBar + "], progress = [" + progress + "], fromUser = [" + fromUser + "]");
                setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (null != videoPlayView) videoPlayView.seekTo(progress);
            }
        });
    }

    public void setVideoPlayView(VideoPlayView videoPlayView) {
        this.videoPlayView = videoPlayView;
    }


    public void setMediaInfo(MediaPlayer mediaInfo) {
        mDuration = mediaInfo.getDuration();//1045120
        Log.d(TAG, "setMediaInfo: ");
        duration_view.setText(VideoPlayHelper.formatDuration(mDuration));
        seek_bar.setMax(mDuration);
        seek_bar.setProgress(0);
    }


    public void setProgress(int progress) {
        mCurrentTimeView.setText(VideoPlayHelper.formatDuration(progress));
    }

    public void dismiss() {
        //ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mPlayControlView, "alpha", 1f, 0f);
        //objectAnimator.start();

        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.INVISIBLE);
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

    public void show() {
        //mPlayControlView.setAlpha(1);
        //  ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mPlayControlView, "alpha", 0f, 1f);
        // objectAnimator.setDuration(50);
        //objectAnimator.start();

        setVisibility(View.VISIBLE);

    }
}
