package com.example.wenjie.mediaplayerdm.PhiFind.mediaPlay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
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
import android.widget.Toast;

import com.example.wenjie.mediaplayerdm.util.WindowUtils;

import java.io.IOException;


public class VideoPlayView extends RelativeLayout implements VideoPlayControlView.VideoControl {
    private static final String TAG = "VideoPlayView";
    public static final int MODE_FULL_SCREEN = 11;
    public static final int MODE_NORMAL = 10;
    private int screenMode;

    private String videoUri;
    private String photoUri;

    private VideoPlayControlView mPlayControlView;

    private MediaPlayer mMediaPlayer;
    private NiceTextureView mTextureView;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;

    private Context mContext;
    private boolean mStartPlay;
    private int progressTo;

    private FrameLayout mContainer;

    public void showProgress() {
        int currentPosition = mMediaPlayer.getCurrentPosition();
        mPlayControlView.setProgress(currentPosition);
    }

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

        initTextureView();
        addTextureView();
        mPlayControlView = new VideoPlayControlView(getContext());
        setPlayControlView(mPlayControlView);
    }

    public void setPlayControlView(VideoPlayControlView playControlView) {

        mPlayControlView = playControlView;
        mContainer.removeView(playControlView);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.addView(mPlayControlView, params);
        mPlayControlView.setPlayer(this);
        mPlayControlView.setControlViewCallBack(this);
    }


    private void onPlayError() {
        Toast.makeText(getContext(), "无法播放次视频数据", Toast.LENGTH_SHORT).show();
        mPlayControlView.setProgress(progressTo);
    }

    TextureView.SurfaceTextureListener mSurfaceListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            Log.d(TAG, "onSurfaceTextureAvailable: ");
            if (mSurfaceTexture == null) {
                openVideo(surfaceTexture);
            } else {
                mTextureView.setSurfaceTexture(mSurfaceTexture);
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Log.d(TAG, "onSurfaceTextureSizeChanged: ");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            System.out.println("onSurfaceTextureDestroyed onSurfaceTextureDestroyed");
           /* surfaceTexture = null;
            mSurface = null;
            mMediaPlayer.stop();
            mMediaPlayer.release();
            return true;*/

            return mSurfaceTexture == null;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            Log.d(TAG, "onSurfaceTextureUpdated: ");

        }

        private void openVideo(SurfaceTexture surfaceTexture) {
            mSurfaceTexture = surfaceTexture;
            try {
                Uri uri = Uri.parse(videoUri);
                mMediaPlayer.setDataSource(uri.toString());
                if (mSurface == null) mSurface = new Surface(mSurfaceTexture);
                mMediaPlayer.setSurface(mSurface);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayControlView.setMediaPlayer(mMediaPlayer);
        }

    };


    private void initTextureView() {
        if (mTextureView == null) {
            mTextureView = new NiceTextureView(mContext);
            mTextureView.setSurfaceTextureListener(mSurfaceListener);
        }
    }

    private void addTextureView() {
        mContainer.removeView(mTextureView);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        mContainer.addView(mTextureView, 0, params);
    }


    private void initPlayer() {
        Log.d(TAG, "initPlayer: ");
        mMediaPlayer = new MediaPlayer();
    }


    private void setMediaListener() {
        //播放完监听
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion: ");

            }
        });
        //视频大小改变监听
        mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                Log.d(TAG, "onVideoSizeChanged() called with: mp = [" + mp + "], width = [" + width + "], height = [" + height + "]");
            }
        });
        //错误监听
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d(TAG, "onError() called with: mp = [" + mp + "], what = [" + what + "], extra = [" + extra + "]");
                return false;
            }
        });
        //事件监听
        mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                Log.d(TAG, "onInfo() called with: mp = [" + mp + "], what = [" + what + "], extra = [" + extra + "]");
                return false;
            }
        });
        //缓冲变化监听
        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.d(TAG, "onBufferingUpdate() called with: mp = [" + mp + "], percent = [" + percent + "]");
            }
        });
        //
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                Log.d(TAG, "onSeekComplete: " + mMediaPlayer.getCurrentPosition() + "  /  " + mMediaPlayer.getDuration());
                if (mMediaPlayer.getDuration() == 0) {
                    onPlayError();
                    return;
                }

                if (mStartPlay && !mMediaPlayer.isPlaying()) {
                    play();
                    mPlayControlView.setPlay(false);
                }
            }
        });
    }


    public void stopPlay() {
        mMediaPlayer.stop();
    }

    @Override
    public void play() {
        mMediaPlayer.start();
        showProgress();
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
    }

    @Override
    public void seekTo(int progress) {
        progressTo = progress;
        mMediaPlayer.seekTo(progress);
    }

    @Override
    public void startPlay() {
        setMediaListener();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
                mPlayControlView.onPlayStart();
            }
        });
        mMediaPlayer.prepareAsync();
    }

    @Override
    public void fullScreen() {
        Toast.makeText(getContext(), "全屏", Toast.LENGTH_SHORT).show();
        Activity activity = NiceUtil.scanForActivity(mContext);
        NiceUtil.hideActionBar(mContext);
        WindowUtils.hideBottomUIMenu(activity);
        WindowUtils.setOrientationLand(activity);

        ViewGroup contentView = activity.findViewById(android.R.id.content);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        removeView(mContainer);
        contentView.addView(mContainer, params);

        screenMode = MODE_FULL_SCREEN;
        mPlayControlView.screenMode(screenMode);
    }

    @Override
    public void exitFullScreen() {
        Toast.makeText(getContext(), "退出全屏", Toast.LENGTH_SHORT).show();
        Activity activity = NiceUtil.scanForActivity(mContext);
        WindowUtils.setOrientationPort(activity);


        ViewGroup contentView = NiceUtil.scanForActivity(mContext).findViewById(android.R.id.content);
        contentView.removeView(mContainer);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mContainer, params);
        screenMode = MODE_NORMAL;
        mPlayControlView.screenMode(screenMode);

    }


    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void setVideoUri(String videoUri) {
        Log.d(TAG, "setVideoUri: " + videoUri);
        this.videoUri = videoUri;
        initPlayer();
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: ");
        if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
            mPlayControlView.show();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroy();
    }

    public void destroy() {
        release();
    }


}
