package com.example.wenjie.mediaplayerdm.PhiFind.mediaPlay;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wenjie.mediaplayerdm.R;

import java.lang.ref.WeakReference;


public class VideoPlayControlView extends VideoPlayControl implements View.OnClickListener {
    private static final String TAG = "VideoPlayControlView";

    private static final int MSG_SHOW_PROGRESS = 121;
    private static final int MSG_DISMISS_CONTROL = 122;
    private static final int TIME_INTERVAL = 1000;
    private int mScreenMode = VideoPlayView.MODE_NORMAL;

    private ImageView mPlayBtn;
    private TextView mCurrentTimeView;
    private TextView mEndTimeView;
    private SeekBar mSeekBar;
    private ImageView mChangeScreenView;
    private ImageView mStartPlayView;

    private Handler mHandler;
    private MediaPlayer mMediaPlayer;
    private VideoPlayView videoPlayView;

    private int mDuration;
    private boolean mIsPause = false;

    private VideoControl mVideoController;


    public VideoPlayControlView(Context context) {
        super(context);
        init();
    }

    public VideoPlayControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    protected void init() {
        View.inflate(getContext(), R.layout.video_play_control_layout, this);
        mHandler = new MyHandler(this);
        mCurrentTimeView = findViewById(R.id.progress_view);
        mEndTimeView = findViewById(R.id.duration_view);
        mPlayBtn = findViewById(R.id.play_btn);
        mPlayBtn.setOnClickListener(this);
        mChangeScreenView = findViewById(R.id.full_screen_img);
        mChangeScreenView.setOnClickListener(this);
        mSeekBar = findViewById(R.id.seek_bar);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mStartPlayView = findViewById(R.id.start_play_view);
        mStartPlayView.setOnClickListener(this);

    }

    public static class MyHandler extends Handler {
        private WeakReference<VideoPlayControlView> viewReference;

        public MyHandler(VideoPlayControlView videoPlayView) {
            viewReference = new WeakReference<>(videoPlayView);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_PROGRESS:
                    viewReference.get().showProgress();
                    break;
                case MSG_DISMISS_CONTROL:
                    viewReference.get().dismissControl();
                    break;
            }
        }
    }

    SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // called with: seekBar = [android.widget.SeekBar{33ff4aa6 VFED.... ...P..ID 145,0-685,96 #7f0800c8 app:id/seek_bar}], progress = [78], fromUser = [true]
            Log.d(TAG, "onProgressChanged() called with: seekBar = [" + seekBar + "], progress = [" + progress + "], fromUser = [" + fromUser + "]");
            setProgress(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            stopShowProgress();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            mVideoController.seekTo(progress);
         //   if (null != videoPlayView) videoPlayView.showProgress();
        }
    };

    private void stopShowProgress() {
        mHandler.removeMessages(MSG_SHOW_PROGRESS);
    }

    public void screenMode(int screenMode) {
        mScreenMode = screenMode;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_play_view:
                startPlay();
                break;
            case R.id.play_btn:
                if (mMediaPlayer.isPlaying()) {
                    setPause();
                } else {
                    setPlay();
                }
                break;
            case R.id.full_screen_img:
                changeScreen();
                break;
        }
    }


    private void startPlay() {
        mVideoController.startPlay();
        mStartPlayView.setVisibility(View.GONE);
    }


    @Override
    public void onPlayStart() {
        Log.d(TAG, "onPlayStart: ");
        mDuration = mMediaPlayer.getDuration();//1045120
        mEndTimeView.setText(NiceUtil.formatDuration(mDuration));
        mSeekBar.setMax(mDuration);
        mSeekBar.setProgress(0);
        mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_player_pause));
        showProgress();
    }

    public void setPlayer(VideoPlayView player) {
        this.videoPlayView = player;
    }

    private void showProgress() {
        int progress = mMediaPlayer.getCurrentPosition();
        mCurrentTimeView.setText(NiceUtil.formatDuration(progress));
        mHandler.sendEmptyMessageDelayed(MSG_SHOW_PROGRESS, TIME_INTERVAL);

    }

    private void dismissControl() {
        Log.d(TAG, "dismissControl: ");
    }

    public void setVideoPlayView(VideoPlayView videoPlayView) {
        this.videoPlayView = videoPlayView;
    }


    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        mMediaPlayer = mediaPlayer;
        Log.d(TAG, "setMediaPlayer: " + mediaPlayer);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: ");
        setVisibility(View.VISIBLE);
        return super.onTouchEvent(event);
    }

    public void setProgress(int progress) {
        mCurrentTimeView.setText(NiceUtil.formatDuration(progress));
        //mHandler.sendEmptyMessageDelayed(MSG_SHOW_PROGRESS, TIME_INTERVAL);

        mVideoController.seekTo(progress);
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
        Log.d(TAG, "show: ");
        //mPlayControlView.setAlpha(1);
        //  ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mPlayControlView, "alpha", 0f, 1f);
        // objectAnimator.setDuration(50);
        //objectAnimator.start();

        setVisibility(View.VISIBLE);
        mHandler.removeMessages(MSG_DISMISS_CONTROL);
        mHandler.sendEmptyMessageDelayed(MSG_DISMISS_CONTROL, 3000);
    }


    private void changeScreen() {
        if (VideoPlayView.MODE_NORMAL == mScreenMode) {
            mVideoController.fullScreen();
        } else if (VideoPlayView.MODE_FULL_SCREEN == mScreenMode) {
            mVideoController.exitFullScreen();
        }
    }

    private void setPause() {
        Toast.makeText(getContext(), "暂停", Toast.LENGTH_SHORT).show();
        mVideoController.pause();
        mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_player_start));
        stopShowProgress();
        mIsPause = true;
    }

    private void setPlay() {
        Toast.makeText(getContext(), "播放", Toast.LENGTH_SHORT).show();
        setPlay(true);
        showProgress();
        mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_player_pause));
        mIsPause = false;
    }

    public void setPlay(boolean callBack) {
        if (callBack) {
            mVideoController.play();
        }
    }

    private void loadPhoto() {
        //  mPhotoImg.setVisibility(View.VISIBLE);
        //  Glide.with(getContext()).load(photoUri).into(mPhotoImg);
    }

    public interface VideoControl {
        void startPlay();

        void play();

        void pause();

        void seekTo(int pos);

        void fullScreen();

        void exitFullScreen();
    }

    public void setControlViewCallBack(VideoControl controlViewCallBack) {
        mVideoController = controlViewCallBack;
    }
}
