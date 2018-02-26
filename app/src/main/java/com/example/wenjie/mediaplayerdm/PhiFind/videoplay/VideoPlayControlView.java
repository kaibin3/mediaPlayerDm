package com.example.wenjie.mediaplayerdm.PhiFind.videoplay;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wenjie.mediaplayerdm.R;
import com.example.wenjie.mediaplayerdm.util.LogUtils;

import java.lang.ref.WeakReference;


public class VideoPlayControlView extends VideoPlayAbsControl implements View.OnClickListener {
    private static final String TAG = "VideoPlayControl";

    protected static final int MSG_SHOW_PROGRESS = 121;
    protected static final int MSG_DISMISS_SELF = 122;
    protected static final int TIME_INTERVAL = 1000;

    protected TextView mCurrentTimeView;
    protected TextView mEndTimeView;
    protected SeekBar mSeekBar;
    protected ImageView mBottomPlayView;
    protected ImageView mChangeScreenView;
    protected ImageView mImageView;
    protected ImageView mCenterPlayView;
    protected ImageView mBackView;
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
        mBackView = (ImageView) findViewById(R.id.back_icon);
        mBackView.setOnClickListener(this);
        mImageView = (ImageView) findViewById(R.id.video_img);
        mImageView.setImageResource(R.drawable.video_bg_loading);
        mCurrentTimeView = (TextView) findViewById(R.id.progress_view);
        mEndTimeView = (TextView) findViewById(R.id.end_time_view);
        mBottomPlayView = (ImageView) findViewById(R.id.bottom_play_btn);
        mBottomPlayView.setOnClickListener(this);
        mChangeScreenView = (ImageView) findViewById(R.id.full_screen_img);
        mChangeScreenView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.video_icon_enlarge));
        mChangeScreenView.setOnClickListener(this);
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mCenterPlayView = (ImageView) findViewById(R.id.center_play_view);
        mCenterPlayView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.video_icon_play));
        mCenterPlayView.setOnClickListener(this);
        mBottomLayout = (LinearLayout) findViewById(R.id.bottom_bar);
        mBottomLayout.setVisibility(View.INVISIBLE);
        mHandler = new UpdateHandler(this);
        initRes();
    }

    protected void initRes() {
        mLoadingDrawable = getResources().getDrawable(R.drawable.toast_loading);
        mReplayDrawable = getResources().getDrawable(R.drawable.video_icon_replay);
    }

    public static class UpdateHandler extends Handler {
        private WeakReference<VideoPlayControlView> viewReference;

        UpdateHandler(VideoPlayControlView videoPlayView) {
            viewReference = new WeakReference<>(videoPlayView);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_PROGRESS:
                    LogUtils.d(TAG, "handleMessage: viewReference" + viewReference);
                    LogUtils.d(TAG, "handleMessage: viewReference VideoPlayControlView " + viewReference.get());
                    if (null != viewReference.get()) {
                        viewReference.get().showCurrentProgress();
                    }
                    break;
                case MSG_DISMISS_SELF:
                    if (null != viewReference.get()) {
                        viewReference.get().dismiss();
                    }
                    break;
                default:
            }
        }
    }

    SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            setProgress(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            stopShowProgress();
            mHandler.removeMessages(MSG_DISMISS_SELF);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            mVideoPlayer.seekTo(progress);
            dismissSelfDelay();
        }
    };

    protected void dismissSelfDelay() {
        mHandler.sendEmptyMessageDelayed(MSG_DISMISS_SELF, 2000);
    }

    protected void stopShowProgress() {
        mHandler.removeMessages(MSG_SHOW_PROGRESS);
    }

    @Override
    public void setScreenMode(int screenMode) {
        mScreenMode = screenMode;
        if (VideoPlayView.MODE_NORMAL == screenMode) {
            setOnNormalScreen();
        } else if (VideoPlayView.MODE_FULL_SCREEN == screenMode) {
            setOnFullScreen();
        }
    }

    protected void setOnFullScreen() {
        mChangeScreenView.setVisibility(View.GONE);
        mBackView.setVisibility(View.VISIBLE);
        mBottomLayout.setPadding(0, NiceUtil.dp2px(getContext(), 5), 0, NiceUtil.dp2px(getContext(), 15));
    }

    protected void setOnNormalScreen() {
        mChangeScreenView.setVisibility(View.VISIBLE);
        mBackView.setVisibility(View.INVISIBLE);
        mBottomLayout.setPadding(0, NiceUtil.dp2px(getContext(), 5), 0, NiceUtil.dp2px(getContext(), 5));
    }

    @Override
    public void onClick(View v) {
        LogUtils.d(TAG, "onClick: ");
        switch (v.getId()) {
            case R.id.center_play_view:
                centerPlayClick();
                break;
            case R.id.bottom_play_btn:
                bottomPlayClick();
                break;
            case R.id.full_screen_img:
                changeScreenMode();
                break;
            case R.id.back_icon:
                if (VideoPlayView.MODE_FULL_SCREEN == mScreenMode) {
                    mVideoPlayer.exitFullScreen();
                }
                break;
            default:
                showOrDismiss();
        }
    }

    protected void bottomPlayClick() {
        if (mVideoPlayer.isPlaying()) {
            setPause();
        } else {
            setPlay();
        }
    }

    protected void centerPlayClick() {
        if (!mVideoPlayer.isStarted()) {
            if (mVideoPlayer.isNetVideo()) {
                if (!NiceUtil.isNetworkAvailable(mContext)) {
                    showNoNetwork();
                    return;
                }
                if (NiceUtil.isOnlyMobileNetworkConnect(mContext)) {
                    mCenterPlayView.setVisibility(View.INVISIBLE);
                    showOnMobileNetwork();
                    return;
                }
            }
            startPlay();
        } else {
            if (mVideoPlayer.isPlaying()) {
                setPause();
            } else {
                setPlay();
            }
        }
    }

    @Override
    public void startPlay() {
        showLoading();
        mVideoPlayer.startPlay();
        mCenterPlayView.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onPlayerStart() {
        Log.d(TAG, "onPlayerStart: ");
        dismissLoading();
        mImageView.setVisibility(View.GONE);
        mDuration = mVideoPlayer.getDuration();
        mEndTimeView.setText(NiceUtil.formatDuration(mDuration));
        mSeekBar.setMax(mDuration);
        mSeekBar.setProgress(0);
        mCenterPlayView.setVisibility(View.GONE);
        mCenterPlayView.setImageDrawable(getResources().getDrawable(R.drawable.video_icon_pause));
        mBottomPlayView.setImageDrawable(getResources().getDrawable(R.drawable.video_icon_pause));
        showCurrentProgress();
    }

    private void showCurrentProgress() {
        LogUtils.d(TAG, "showCurrentProgress: ");
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
        mCenterPlayView.setVisibility(VISIBLE);
        mBottomLayout.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.VISIBLE);
        mHandler.removeMessages(MSG_DISMISS_SELF);
        dismissSelfDelay();
    }

    @Override
    public void dismiss() {
        LogUtils.d(TAG, "dismiss: ");
        mCenterPlayView.setVisibility(View.INVISIBLE);
        mBottomLayout.setVisibility(View.INVISIBLE);
        mContainer.setVisibility(View.INVISIBLE);
    }


    @Override
    public void setImage(String url) {
        LogUtils.d(TAG, "setImage: " + url);
        mImageUri = url;
        loadImage();
    }

    @Override
    public void onPlayCompletion() {
        if (!errorLayoutShow) {
            mContainer.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.VISIBLE);
            mCenterPlayView.setVisibility(View.VISIBLE);
        }
        mBottomLayout.setVisibility(View.INVISIBLE);
        mBackView.setVisibility(View.INVISIBLE);
        mCenterPlayView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.video_icon_replay));
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void showProgress() {
        showCurrentProgress();
    }


    @Override
    public void dismissLoading() {
        super.dismissLoading();
    }

    @Override
    protected void retryOnError() {
        dismissError();
        if (!mVideoPlayer.isStarted()) {
            startPlay();
        }
    }

    @Override
    public void onPlayError() {
        mCenterPlayView.setVisibility(View.INVISIBLE);
        mBottomLayout.setVisibility(View.INVISIBLE);
        mHandler.removeCallbacksAndMessages(null);
        dismissLoading();
        showOnError();
    }

    @Override
    public void cancel() {
        mHandler.removeCallbacksAndMessages(null);
    }

    protected void showOrDismiss() {
        if (!mVideoPlayer.isPlaying()) {
            return;
        }
        if (mBottomLayout.getVisibility() == View.VISIBLE) {
            dismiss();
        } else {
            show();
        }
    }

    public void setProgress(int progress) {
        mCurrentTimeView.setText(NiceUtil.formatDuration(progress));
    }


    protected void changeScreenMode() {
        if (VideoPlayView.MODE_NORMAL == mScreenMode) {
            mVideoPlayer.fullScreen();
        } else if (VideoPlayView.MODE_FULL_SCREEN == mScreenMode) {
            mVideoPlayer.exitFullScreen();
        }
    }

    protected void setPause() {
        mVideoPlayer.pause();
        mCenterPlayView.setImageDrawable(getResources().getDrawable(R.drawable.video_icon_play));
        mBottomPlayView.setImageDrawable(getResources().getDrawable(R.drawable.video_icon_play));
        stopShowProgress();
        mHandler.removeMessages(MSG_DISMISS_SELF);
    }

    protected void setPlay() {
        mVideoPlayer.play();
        showCurrentProgress();
        mCenterPlayView.setImageDrawable(getResources().getDrawable(R.drawable.video_icon_pause));
        mBottomPlayView.setImageDrawable(getResources().getDrawable(R.drawable.video_icon_pause));
    }

    protected void loadImage() {
        Glide.with(getContext())
                .load(mImageUri)
                .centerCrop()
                .placeholder(R.drawable.video_bg_loading)
                .into(mImageView);
    }

}
