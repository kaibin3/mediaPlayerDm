package com.example.wenjie.mediaplayerdm.PhiFind.mediaPlay;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wenjie.mediaplayerdm.R;

import java.lang.ref.WeakReference;


public class VideoPlayControlView extends VideoPlayAbsControl implements View.OnClickListener {
    private static final String TAG = "VideoPlayControlView";

    public static final int MSG_SHOW_PROGRESS = 121;
    public static final int MSG_DISMISS_CONTROL = 122;
    protected static final int TIME_INTERVAL = 1000;
    protected int mScreenMode = VideoPlayView.MODE_NORMAL;

    protected ImageView mPlayBtn;
    protected TextView mCurrentTimeView;
    protected TextView mEndTimeView;
    protected SeekBar mSeekBar;
    protected ImageView mImageView;
    protected ImageView mChangeScreenView;
    protected ImageView mCenterPlayView;
    protected LinearLayout mBottomLayout;


    protected Handler mHandler;
    protected VideoContract.VideoPlayer mVideoPlayer;
    protected GestureDetector gestureDetector;

    protected String mImageUri;
    protected int mDuration;


    public VideoPlayControlView(Context context) {
        super(context);
        init();
    }

    public VideoPlayControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    protected void init() {

        mHandler = new UpdateHandler(this);
        View.inflate(getContext(), R.layout.video_play_control_layout, this);
        mContainer = this;
        mContainer.setOnClickListener(this);
        mImageView = findViewById(R.id.video_img);
        mCurrentTimeView = findViewById(R.id.progress_view);
        mEndTimeView = findViewById(R.id.end_time_view);
        mPlayBtn = findViewById(R.id.bottom_play_btn);
        mPlayBtn.setOnClickListener(this);
        mChangeScreenView = findViewById(R.id.full_screen_img);
        mChangeScreenView.setOnClickListener(this);
        mSeekBar = findViewById(R.id.seek_bar);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mCenterPlayView = findViewById(R.id.center_play_view);
        mCenterPlayView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_player_center_start));
        mCenterPlayView.setOnClickListener(this);
        mBottomLayout = findViewById(R.id.bottom_bar);
        mBottomLayout.setVisibility(View.INVISIBLE);
        gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {

            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: ");
                return false;
            }

            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: ");
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d(TAG, "onScroll: ");
                return false;
            }

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.d(TAG, "onFling: ");
                return false;
            }

            public void onShowPress(MotionEvent e) {
                Log.d(TAG, "onShowPress: ");
            }

            public boolean onDown(MotionEvent e) {
                Log.d(TAG, "onDown: ");
                return false;
            }

            public boolean onDoubleTap(MotionEvent e) {
                Log.d(TAG, "onDoubleTap: ");
                return false;
            }

            public boolean onDoubleTapEvent(MotionEvent e) {
                Log.d(TAG, "onDoubleTapEvent: ");
                return false;
            }

            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.d(TAG, "onSingleTapConfirmed: ");
                return false;
            }

            public boolean onContextClick(MotionEvent e) {
                Log.d(TAG, "onContextClick: ");
                return false;
            }

        });
    }

    public static class UpdateHandler extends Handler {
        private WeakReference<VideoPlayControlView> viewReference;

        public UpdateHandler(VideoPlayControlView videoPlayView) {
            viewReference = new WeakReference<>(videoPlayView);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_PROGRESS:
                    viewReference.get().showCurrentProgress();
                    break;
                case MSG_DISMISS_CONTROL:
                    viewReference.get().dismiss();
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
            mVideoPlayer.seekTo(progress);
            //   if (null != videoPlayView) videoPlayView.showProgress();
        }
    };

    private void stopShowProgress() {
        mHandler.removeMessages(MSG_SHOW_PROGRESS);
    }

    public void setScreenMode(int screenMode) {
        mScreenMode = screenMode;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: ");
        //gestureDetector.onTouchEvent(event);
        //   showOrDismiss();
        return super.onTouchEvent(event);
    }


    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        switch (v.getId()) {
            case R.id.center_play_view:
                centerPlayClick();
                break;
            case R.id.bottom_play_btn:
                if (mVideoPlayer.isPlaying()) {
                    setPause();
                } else {
                    setPlay();
                }
                break;
            case R.id.full_screen_img:
                changeScreen();
                break;
            default:
                showOrDismiss();
        }
    }


    private void centerPlayClick() {
        if (!mVideoPlayer.isStarted()) {
            mVideoPlayer.startPlay();
            mCenterPlayView.setVisibility(View.INVISIBLE);
            showLoading();
        } else {
            setPause();
        }
    }


    @Override
    public void onPlayerStart() {
        Log.d(TAG, "onPlayerStart: ");
        dismissLoading();
        mImageView.setVisibility(View.GONE);
        //mContainer.setVisibility(View.INVISIBLE);
        mDuration = mVideoPlayer.getDuration();//1045120
        mEndTimeView.setText(NiceUtil.formatDuration(mDuration));
        mSeekBar.setMax(mDuration);
        mSeekBar.setProgress(0);
        mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_player_pause));
        showCurrentProgress();
    }


    private void showCurrentProgress() {
        Log.d(TAG, "showCurrentProgress: mVideoPlayer " + mVideoPlayer);
        int progress = mVideoPlayer.getCurrentPosition();
        mCurrentTimeView.setText(NiceUtil.formatDuration(progress));
        mHandler.sendEmptyMessageDelayed(MSG_SHOW_PROGRESS, TIME_INTERVAL);
    }


    @Override
    public void setVideoPlayer(VideoContract.VideoPlayer player) {
        mVideoPlayer = player;
    }


    @Override
    public void show() {
        Log.d(TAG, "show: ");
        //mPlayControlView.setAlpha(1);
        //  ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mPlayControlView, "alpha", 0f, 1f);
        // objectAnimator.setDuration(50);
        //objectAnimator.start();

        mContainer.setVisibility(View.VISIBLE);
        mCenterPlayView.setVisibility(View.VISIBLE);
        mBottomLayout.setVisibility(View.VISIBLE);
        mCenterPlayView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_player_pause));
        mHandler.removeMessages(MSG_DISMISS_CONTROL);
        mHandler.sendEmptyMessageDelayed(MSG_DISMISS_CONTROL, 3000);
    }

    @Override
    public void dismiss() {
        Log.d(TAG, "dismiss: ");
        mBottomLayout.setVisibility(View.INVISIBLE);
        mContainer.setVisibility(View.INVISIBLE);
        // mHandler.removeMessages(MSG_DISMISS_CONTROL);
    }


    @Override
    public void setImage(String uri) {
        Log.d(TAG, "setImage: ");
        mImageUri = uri;
        loadImage();
    }


    private void showOrDismiss() {
        Log.d(TAG, "showOrDismiss: VISIBLE " + (mBottomLayout.getVisibility() == View.VISIBLE));
        if (mBottomLayout.getVisibility() == View.VISIBLE) {
            dismiss();
        } else {
            show();
        }

    }

    public void setProgress(int progress) {
        mCurrentTimeView.setText(NiceUtil.formatDuration(progress));
    }

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

    private void changeScreen() {
        if (VideoPlayView.MODE_NORMAL == mScreenMode) {
            mVideoPlayer.fullScreen();
        } else if (VideoPlayView.MODE_FULL_SCREEN == mScreenMode) {
            mVideoPlayer.exitFullScreen();
        }
    }

    private void setPause() {
        Toast.makeText(getContext(), "暂停", Toast.LENGTH_SHORT).show();
        mVideoPlayer.pause();
        mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_player_start));
        mCenterPlayView.setImageDrawable(getResources().getDrawable(R.drawable.ic_player_start));
        stopShowProgress();
        mHandler.removeMessages(MSG_DISMISS_CONTROL);
    }

    private void setPlay() {
        Toast.makeText(getContext(), "播放", Toast.LENGTH_SHORT).show();
        setPlay(true);
        showCurrentProgress();
        mPlayBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_player_pause));
    }

    public void setPlay(boolean callBack) {
        if (callBack) {
            mVideoPlayer.play();
        }
    }

    private void loadImage() {
        Glide.with(getContext()).load(mImageUri).into(mImageView);
    }


}
