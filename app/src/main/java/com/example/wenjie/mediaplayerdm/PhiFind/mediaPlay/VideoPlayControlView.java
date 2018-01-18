package com.example.wenjie.mediaplayerdm.PhiFind.mediaPlay;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
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
import com.example.wenjie.mediaplayerdm.base.Utils;

import java.lang.ref.WeakReference;


public class VideoPlayControlView extends VideoPlayAbsControl implements View.OnClickListener {
    private static final String TAG = "VideoPlayControlView";

    protected static final int MSG_SHOW_PROGRESS = 121;
    protected static final int MSG_DISMISS_CONTROL = 122;
    protected static final int TIME_INTERVAL = 1000;

    protected TextView mCurrentTimeView;
    protected TextView mEndTimeView;
    protected SeekBar mSeekBar;
    protected ImageView mBottomPlayView;
    protected ImageView mChangeScreenView;
    protected ImageView mImageView;
    protected ImageView mCenterPlayView;
    protected LinearLayout mBottomLayout;

    protected Handler mHandler;
    protected VideoContract.VideoPlayer mVideoPlayer;

    protected String mImageUri;
    protected int mDuration;
    protected int mScreenMode = VideoPlayView.MODE_NORMAL;


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
        mContainer = this;
        mContainer.setOnClickListener(this);
        mImageView = findViewById(R.id.video_img);
        mCurrentTimeView = findViewById(R.id.progress_view);
        mEndTimeView = findViewById(R.id.end_time_view);
        mBottomPlayView = findViewById(R.id.bottom_play_btn);
        mBottomPlayView.setOnClickListener(this);
        mChangeScreenView = findViewById(R.id.full_screen_img);
        mChangeScreenView.setOnClickListener(this);
        mSeekBar = findViewById(R.id.seek_bar);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mCenterPlayView = findViewById(R.id.center_play_view);
        mCenterPlayView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_player_center_start));
        mCenterPlayView.setOnClickListener(this);
        mBottomLayout = findViewById(R.id.bottom_bar);
        mBottomLayout.setVisibility(View.INVISIBLE);
        mHandler = new UpdateHandler(this);
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
                    Log.d(TAG, "handleMessage: viewReference" + viewReference);
                    Log.d(TAG, "handleMessage: viewReference VideoPlayControlView " + viewReference.get());
                    if (null != viewReference.get()) {
                        viewReference.get().showCurrentProgress();
                    }
                    break;
                case MSG_DISMISS_CONTROL:
                    if (null != viewReference.get()) {
                        viewReference.get().dismiss();
                    }
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
        }
    };

    private void stopShowProgress() {
        mHandler.removeMessages(MSG_SHOW_PROGRESS);
    }

    public void setScreenMode(int screenMode) {
        mScreenMode = screenMode;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.center_play_view:
                centerPlayClick();
                break;
            case R.id.bottom_play_btn:
                bottomPlayClick();
                break;
            case R.id.full_screen_img:
                changeScreen();
                break;
            default:
                showOrDismiss();
        }
    }

    private void bottomPlayClick() {
        if (mVideoPlayer.isPlaying()) {
            setPause();
        } else {
            setPlay();
        }
    }


    private void centerPlayClick() {
        if (mVideoPlayer.isNetVideo()) {
            if (!Utils.isNetworkAvailable(mContext)) {
                showNoNetwork();
                return;
            }
            if (Utils.isOnlyMobileNetworkConnect(mContext)) {
                mCenterPlayView.setVisibility(View.INVISIBLE);
                showOnMobileNetwork();
                return;
            }
        }
        startPlay();

    }

    @Override
    public void startPlay() {
        mVideoPlayer.startPlay();
        mCenterPlayView.setVisibility(View.INVISIBLE);
        showLoading();
    }


    @Override
    public void onPlayerStart() {
        Log.d(TAG, "onPlayerStart: ");
        dismissLoading();
        mImageView.setVisibility(View.GONE);
        //mContainer.setVisibility(View.INVISIBLE);
        mDuration = mVideoPlayer.getDuration();
        mEndTimeView.setText(NiceUtil.formatDuration(mDuration));
        mSeekBar.setMax(mDuration);
        mSeekBar.setProgress(0);
        mCenterPlayView.setVisibility(View.GONE);
        mBottomPlayView.setImageDrawable(getResources().getDrawable(R.drawable.ic_player_pause));
        showCurrentProgress();
    }


    private void showCurrentProgress() {
        Log.d(TAG, "showCurrentProgress: mVideoPlayer " + mVideoPlayer);
        int progress = mVideoPlayer.getCurrentPosition();
        mCurrentTimeView.setText(NiceUtil.formatDuration(progress));
        mHandler.sendEmptyMessageDelayed(MSG_SHOW_PROGRESS, TIME_INTERVAL);
        mSeekBar.setProgress(progress);
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
        mBottomLayout.setVisibility(View.VISIBLE);
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
        Log.d(TAG, "setImage: " + uri);
        mImageUri = uri;
        loadImage();
    }

    @Override
    public void showProgress() {
        showCurrentProgress();
    }


    private void showOrDismiss() {
        if (!mVideoPlayer.isStarted()) {
            return;
        }
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
        mBottomPlayView.setImageDrawable(getResources().getDrawable(R.drawable.ic_player_start));
        stopShowProgress();
        mHandler.removeMessages(MSG_DISMISS_CONTROL);
    }

    private void setPlay() {
        Toast.makeText(getContext(), "播放", Toast.LENGTH_SHORT).show();
        setPlay(true);
        showCurrentProgress();
        mBottomPlayView.setImageDrawable(getResources().getDrawable(R.drawable.ic_player_pause));
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
