package com.example.wenjie.mediaplayerdm.PhiFind;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.wenjie.mediaplayerdm.R;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by wen.jie on 2018/1/3.
 */

public class VideoPlayView extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = "VideoPlayView";
    private static final int MSG_SHOW_PROGRESS = 121;
    private static final int MSG_DISMISS_CONTROL = 122;
    private static final int TIME_INTERVAL = 1000;
    private VideoPlayInfo mediaInfo;

    private String videoUri;
    private String photoUri;

    private SurfaceView mSurfaceView;
    private ImageView mPhotoImg;
    private ImageView mPlayImg;
    private VideoPlayControlView mPlayControlView;

    private MediaPlayer mMediaPlayer;
    private SurfaceHolder mSurfaceHolder;
    private Handler mHandler;

    private boolean mSetDataSource;
    private boolean mStartPlay;


    public static class MyHandler extends Handler {
        private WeakReference<VideoPlayView> viewReference;

        public MyHandler(VideoPlayView videoPlayView) {
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

    private void showProgress() {
        int currentPosition = mMediaPlayer.getCurrentPosition();
        Log.d(TAG, "handleMessage: currentPosition " + currentPosition);
        mPlayControlView.setProgress(currentPosition);
        mHandler.sendEmptyMessageDelayed(MSG_SHOW_PROGRESS, TIME_INTERVAL);
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
        View.inflate(getContext(), R.layout.video_play_view, this);
        mPhotoImg = findViewById(R.id.video_previous_img);
        mPlayImg = findViewById(R.id.play_img);
        mPlayImg.setOnClickListener(this);
        mSurfaceView = findViewById(R.id.surface_view);
        mSurfaceView.setOnTouchListener(onTouchListener);
        mPlayControlView = findViewById(R.id.play_control_view);
        mPlayControlView.setVideoPlayView(this);
        mPlayControlView.setVisibility(View.INVISIBLE);
        mHandler = new MyHandler(this);
    }


    private void initPlayer() {
        Log.d(TAG, "initPlayer: ");
        mMediaPlayer = new MediaPlayer();
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d(TAG, "onSurfaceCreated: ");
                onSurfaceCreated(holder);
                Log.d(TAG, "onPrepared: isPlaying 111 " + mMediaPlayer.isPlaying());
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d(TAG, "surfaceDestroyed: ");
                Log.d(TAG, "onPrepared: isPlaying 120 " + mMediaPlayer.isPlaying());
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mHandler.removeMessages(MSG_SHOW_PROGRESS);
                }
            }
        });
        mMediaPlayer.setOnCompletionListener(onCompletionListener);
        mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
        mMediaPlayer.setOnErrorListener(onErrorListener);
        mMediaPlayer.setOnInfoListener(onInfoListener);
        mMediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
    }

    //播放完监听
    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.d(TAG, "onCompletion: ");

        }
    };

    //视频大小改变监听
    MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            Log.d(TAG, "onVideoSizeChanged() called with: mp = [" + mp + "], width = [" + width + "], height = [" + height + "]");
        }
    };

    //错误监听
    MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.d(TAG, "onError() called with: mp = [" + mp + "], what = [" + what + "], extra = [" + extra + "]");
            return false;
        }
    };

    //事件监听
    MediaPlayer.OnInfoListener onInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            Log.d(TAG, "onInfo() called with: mp = [" + mp + "], what = [" + what + "], extra = [" + extra + "]");
            return false;
        }
    };

    //缓冲变化监听
    MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            Log.d(TAG, "onBufferingUpdate() called with: mp = [" + mp + "], percent = [" + percent + "]");
        }
    };

    private void onSurfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "onSurfaceCreated: ");
        mMediaPlayer.setDisplay(holder);
        if (!mSetDataSource) {
            String videoUrl2 = videoUri;
            Uri uri = Uri.parse(videoUrl2);
            try {
                mMediaPlayer.setDataSource(uri.toString());
            } catch (IOException e) {
                Log.e(TAG, "onSurfaceCreated: " + e);
                e.printStackTrace();
            }
            mSetDataSource = true;
        }
        if (mStartPlay) {
            mMediaPlayer.start();
            mHandler.sendEmptyMessageDelayed(MSG_SHOW_PROGRESS, TIME_INTERVAL);
        }
    }

    private void startPlay() {
        mPhotoImg.setVisibility(View.INVISIBLE);
        mPlayImg.setVisibility(View.INVISIBLE);
        try {
            mMediaPlayer.prepareAsync();//prepareAsync 非阻塞
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mPlayControlView.setMediaInfo(mMediaPlayer);
                    mMediaPlayer.start();
                    showProgress();
                    mStartPlay = true;
                }
            });

        } catch (IllegalStateException e) {
            Log.d(TAG, "startPlay: IllegalStateException " + e);
            e.printStackTrace();
        }
    }


    public void stopPlay() {
        mMediaPlayer.stop();
    }

    public void seekTo(int progress) {
        if (null != mMediaPlayer) {
            mMediaPlayer.seekTo(progress);
        }
    }

    OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            showControl();
            return true;
        }
    };

    private void showControl() {
        mPlayControlView.show();
        mHandler.removeMessages(MSG_DISMISS_CONTROL);
        mHandler.sendEmptyMessageDelayed(MSG_DISMISS_CONTROL, 3000);
    }

    private void dismissControl() {
        // mPlayControlView.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_img:
                startPlay();
                break;
        }
    }

    public void setMediaInfo(VideoPlayInfo mediaInfo) {
        this.mediaInfo = mediaInfo;
        setPhotoUri(mediaInfo.getPhotoUri());
        setVideoUri(mediaInfo.getVideoUri());
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
        initPlayer();
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
        loadPhoto();
    }

    private void loadPhoto() {
        mPhotoImg.setVisibility(View.VISIBLE);
        Glide.with(getContext()).load(photoUri).into(mPhotoImg);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroy();
    }

    public void destroy() {
        mHandler.removeCallbacksAndMessages(null);
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }

    public static class VideoPlayInfo {
        public VideoPlayInfo() {
        }

        String photoUri;
        String videoUri;

        public String getPhotoUri() {
            return photoUri;
        }

        public void setPhotoUri(String photoUri) {
            this.photoUri = photoUri;
        }

        public String getVideoUri() {
            return videoUri;
        }

        public void setVideoUri(String videoUri) {
            this.videoUri = videoUri;
        }
    }


}
