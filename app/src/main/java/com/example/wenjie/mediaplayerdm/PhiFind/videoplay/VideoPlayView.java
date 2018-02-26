package com.example.wenjie.mediaplayerdm.PhiFind.videoplay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.wenjie.mediaplayerdm.util.LogUtils;

import java.io.IOException;


public class VideoPlayView extends RelativeLayout implements VideoContract.VideoPlayer {
    private static final String TAG = "VideoPlayViewT";
    public static final int MODE_FULL_SCREEN = 11;
    public static final int MODE_NORMAL = 10;
    private int mCurrentMode = MODE_NORMAL;

    private String mVideoUri;

    private VideoPlayAbsControl mPlayControl;

    private MediaPlayer mMediaPlayer;
    private NiceTextureView mTextureView;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;

    private Context mContext;
    private FrameLayout mContainer;

    private boolean mIsNetVideo = true;
    private boolean mIsStarted;
    private boolean mPlaying;
    private boolean mActive = true;
    private boolean mNeedStart;
    private boolean mIsPlayPhoneRing;

    public VideoPlayView(Context context) {
        super(context);
        init();
    }

    public VideoPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mContext = getContext();
        mContainer = new FrameLayout(mContext);
        mContainer.setBackgroundColor(Color.BLACK);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mContainer, params);

        initController();
        initTextureView();
        addTextureView();
    }

    public void initController() {
        mPlayControl = new VideoPlayControlView(getContext());
        mContainer.removeView(mPlayControl);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.addView(mPlayControl, params);
        mPlayControl.setScreenMode(mCurrentMode);
    }


    TextureView.SurfaceTextureListener mSurfaceListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            LogUtils.d(TAG, "onSurfaceTextureAvailable: " + mSurfaceTexture);
            if (null == mSurfaceTexture) {
                openVideo(surfaceTexture);
            } else {
                mTextureView.setSurfaceTexture(mSurfaceTexture);
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            LogUtils.d(TAG, "onSurfaceTextureSizeChanged: ");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            LogUtils.d(TAG, "onSurfaceTextureDestroyed: ");
            return mSurfaceTexture == null;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };


    private void saveData() {
        mPlaying = isPlaying();
    }


    private void openVideo(SurfaceTexture surfaceTexture) {
        mSurfaceTexture = surfaceTexture;
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            setPlayerListener();
        }

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            setPlayerListener();
        }
        mPlayControl.setVideoPlayer(VideoPlayView.this);
        if (mSurface == null) {
            mSurface = new Surface(mSurfaceTexture);
        }
        mMediaPlayer.setSurface(mSurface);
        setDataSource();
    }

    private void setDataSource() {
        try {
            if (null != mVideoUri) {
                Uri uri = Uri.parse(mVideoUri);
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(getContext().getApplicationContext(), uri);
            }
        } catch (IOException e) {
            LogUtils.d(TAG, "setDataSource error: " + e);
            e.printStackTrace();
        }
    }


    private void initTextureView() {
        if (mTextureView == null) {
            mTextureView = new NiceTextureView(mContext);
            mTextureView.setSurfaceTextureListener(mSurfaceListener);
        }
    }

    private void addTextureView() {
        mContainer.removeView(mTextureView);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mContainer.addView(mTextureView, 0, params);
    }


    private void initPlayer() {
        Log.d(TAG, "initPlayer: ");
    }


    private void setPlayerListener() {
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mActive) {
                    prepareFinishVideoStart();
                } else {
                    mNeedStart = true;
                }
            }
        });
        //播放完监听
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtils.d(TAG, "onCompletion: ");
                onPlayCompletion(mp);
            }

        });
        //视频大小改变监听
        mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

            }
        });
        //错误监听
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LogUtils.d(TAG, "onError() called with: mp = [" + mp + "], what = [" + what + "], extra = [" + extra + "]");
                //出错后，从新开始播放
                releasePlayer();
                mPlayControl.onPlayError();
                if (null != mSurfaceTexture) {
                    openVideo(mSurfaceTexture);
                }
                return false;
            }
        });
        //事件监听
        mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        //缓冲变化监听
        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {

            }
        });
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                if (mMediaPlayer.getDuration() == 0) {
                    mPlayControl.onPlayError();
                    return;
                }
                mPlayControl.showProgress();
            }
        });
    }

    private void prepareFinishVideoStart() {
        Log.d(TAG, "prepareFinishVideoStart: ");
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            mPlayControl.onPlayerStart();
            mIsStarted = true;
        }
        mNeedStart = false;
    }

    private void onPlayCompletion(MediaPlayer mp) {
        if (null != mp) {
            mp.stop();
        }
        mPlayControl.onPlayCompletion();
        mIsStarted = false;
    }

    @Override
    public void play() {
        if (null != mMediaPlayer) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    @Override
    public void seekTo(int progress) {
        if (null != mMediaPlayer) {
            mMediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void startPlay() {
        try {
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            mPlayControl.onPlayError();
            LogUtils.d(TAG, "startPlay: exception " + e);
        }
    }

    @Override
    public boolean fullScreen() {
        Activity activity = NiceUtil.scanForActivity(mContext);
        NiceUtil.hideActionBar(mContext);
        NiceUtil.hideBottomUIMenu(activity);
        NiceUtil.setOrientationLand(activity);

        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        removeView(mContainer);
        contentView.addView(mContainer, params);

        mCurrentMode = MODE_FULL_SCREEN;
        mPlayControl.setScreenMode(mCurrentMode);
        return true;
    }

    @Override
    public boolean exitFullScreen() {
        if (mCurrentMode == MODE_FULL_SCREEN) {
            Activity activity = NiceUtil.scanForActivity(mContext);
            NiceUtil.setOrientationPort(activity);
            NiceUtil.showActionBar(mContext);


            ViewGroup contentView = (ViewGroup) NiceUtil.scanForActivity(mContext).findViewById(android.R.id.content);
            contentView.removeView(mContainer);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView(mContainer, params);
            mCurrentMode = MODE_NORMAL;
            mPlayControl.setScreenMode(mCurrentMode);
            return true;
        }
        return false;
    }

    public void setVideoUri(String videoUri) {
        LogUtils.d(TAG, "setVideoUri: " + videoUri);
        mVideoUri = videoUri;
        initPlayer();
    }

    public void setImage(String photoUri) {
        LogUtils.d(TAG, "setImage: " + photoUri);
        mPlayControl.setImage(photoUri);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mPlayControl.onTouchEvent(event);
    }

    public void release() {
        releasePlayer();
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        mCurrentMode = MODE_NORMAL;
        mPlayControl.cancel();
    }

    private void releasePlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mIsStarted = false;
    }

    @Override
    public int getDuration() {
        if (null != mMediaPlayer) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (null != mMediaPlayer) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public boolean isPlaying() {
        return null != mMediaPlayer && mMediaPlayer.isPlaying();
    }

    @Override
    public boolean isStarted() {
        return mIsStarted;
    }


    public void setIsNetVideo(boolean isNetVideo) {
        mIsNetVideo = isNetVideo;
    }

    @Override
    public boolean isNetVideo() {
        return mIsNetVideo;
    }

    @Override
    public boolean backPressed() {
        if (mCurrentMode == MODE_FULL_SCREEN) {
            exitFullScreen();
            return true;
        }
        return false;
    }

    @Override
    public void resume() {
        LogUtils.d(TAG, "resume mPlaying    " + mPlaying + "    mNeedStart  " + mNeedStart);
        mActive = true;
        if (null != mMediaPlayer) {
            if (mPlaying && !mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
                mPlayControl.showProgress();
            }
        }
        if (mNeedStart) {
            prepareFinishVideoStart();
        }
    }

    @Override
    public void suspend() {
        mActive = false;
        saveData();
        pause();
        mPlayControl.cancel();
        LogUtils.d(TAG, "suspend: mPlaying " + mPlaying);
    }

    @Override
    public void phoneRing() {
        mActive = false;
        pause();
        mPlayControl.cancel();
        mIsPlayPhoneRing = isPlaying();
        LogUtils.d(TAG, "phoneRing: mIsPlayPhoneRing " + mIsPlayPhoneRing);
    }


    @Override
    public void phoneIdle() {
        LogUtils.d(TAG, "phoneIdle: mIsPlayPhoneRing " + mIsPlayPhoneRing + "   mNeedStart " + mNeedStart);
        mActive = true;
        if (null != mMediaPlayer) {
            if (mIsPlayPhoneRing && !mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
                mPlayControl.showProgress();
            }
        }
        if (mNeedStart) {
            prepareFinishVideoStart();
        }
    }
}
