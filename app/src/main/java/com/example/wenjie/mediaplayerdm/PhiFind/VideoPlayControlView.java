package com.example.wenjie.mediaplayerdm.PhiFind;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.wenjie.mediaplayerdm.R;

/**
 * Created by wen.jie on 2018/1/5.
 */

public class VideoPlayControlView extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = "VideoPlayControlView";
    private TextView mCurrentTimeView;
    private TextView duration_view;
    private SeekBar seek_bar;
    private TextView play_btn;
    private ImageView fullScreen;

    private VideoPlayView videoPlayView;

    private MediaPlayer mediaPlayer;
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
        play_btn = findViewById(R.id.play_btn);
        play_btn.setOnClickListener(this);
        fullScreen = findViewById(R.id.full_screen_img);
        fullScreen.setOnClickListener(this);
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
                if (null != videoPlayView) videoPlayView.removeMgsShowProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mControlViewCallBack.seekTo(progress);
                if (null != videoPlayView) videoPlayView.showProgress();
            }
        });
    }

    public void setVideoPlayView(VideoPlayView videoPlayView) {
        this.videoPlayView = videoPlayView;
    }


    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        mDuration = mediaPlayer.getDuration();//1045120
        Log.d(TAG, "setMediaInfo: ");
        duration_view.setText(VideoPlayHelper.formatDuration(mDuration));
        seek_bar.setMax(mDuration);
        seek_bar.setProgress(0);
        play_btn.setText("暂停");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_btn:
                if (play_btn.getText().toString().equals("暂停")) {
                    setPause();
                } else {
                    setPlay();
                }
                break;
            case R.id.full_screen_img:
                fullScreenAction();
                break;
        }
    }

    private void fullScreenAction() {
        mControlViewCallBack.fullScreen();
    }

    private void setPause() {
        play_btn.setText("播放");
        mControlViewCallBack.pause();
    }

    private void setPlay() {
        setPlay(true);
    }

    public void setPlay(boolean callBack) {
        play_btn.setText("暂停");
        if (callBack) {
            mControlViewCallBack.start();
        }
    }


    private ControlViewCallBack mControlViewCallBack;

    public interface ControlViewCallBack {
        void start();
        void pause();
        void seekTo(int pos);
        void fullScreen();
    }

    public void setControlViewCallBack(ControlViewCallBack controlViewCallBack) {
        mControlViewCallBack = controlViewCallBack;
    }
}
