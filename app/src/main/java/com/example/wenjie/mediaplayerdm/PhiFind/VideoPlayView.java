package com.example.wenjie.mediaplayerdm.PhiFind;

import android.content.Context;
import android.graphics.SurfaceTexture;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wenjie.mediaplayerdm.R;
import com.example.wenjie.mediaplayerdm.util.RotationUitls;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by wen.jie on 2018/1/3.
 */

public class VideoPlayView extends RelativeLayout implements View.OnClickListener
        , VideoPlayControlView.ControlViewCallBack, View.OnTouchListener {
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
    private NiceTextureView mTextureView;
    private SurfaceTexture mSurfaceTexture;

    private Handler mHandler;
    private Context mContext;

    private boolean mSetDataSource;
    private boolean mStartPlay;
    private int progressTo;


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


    public void showProgress(int progress) {
        mPlayControlView.setProgress(progress);
        mHandler.sendEmptyMessageDelayed(MSG_SHOW_PROGRESS, TIME_INTERVAL);
    }

    public void showProgress() {
        int currentPosition = mMediaPlayer.getCurrentPosition();
        Log.d(TAG, "handleMessage: currentPosition " + currentPosition);
        mPlayControlView.setProgress(currentPosition);
        mHandler.removeMessages(MSG_SHOW_PROGRESS);
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
        mContext = getContext();
        mPhotoImg = findViewById(R.id.video_previous_img);
        mPlayImg = findViewById(R.id.play_img);
        mPlayImg.setOnClickListener(this);
        mSurfaceView = findViewById(R.id.surface_view);
        mSurfaceView.setOnTouchListener(this);
        mPlayControlView = findViewById(R.id.play_control_view);
        mPlayControlView.setVideoPlayView(this);
        mPlayControlView.setVisibility(View.INVISIBLE);
        mHandler = new MyHandler(this);
    }


    private void initPlayer() {
        Log.d(TAG, "initPlayer: ");
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d(TAG, "onSurfaceCreated: ");
                onSurfaceCreated(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d(TAG, "surfaceDestroyed: ");
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mHandler.removeMessages(MSG_SHOW_PROGRESS);
                }
            }
        });
        mPlayControlView.setControlViewCallBack(this);
    }

    private void addMediaListener() {
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

        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                Log.d(TAG, "onSeekComplete: " + mMediaPlayer.getCurrentPosition() + "  /  " + mMediaPlayer.getDuration());
                if (mMediaPlayer.getDuration() == 0) {
                    onPlayError();
                    return;
                }

                if (mStartPlay && !mMediaPlayer.isPlaying()) {
                    start();
                    mPlayControlView.setPlay(false);
                }
                // showProgress();
            }
        });
    }

    private void onPlayError() {
        Toast.makeText(getContext(), "无法播放次视频数据", Toast.LENGTH_SHORT).show();
        //mHandler.removeMessages(MSG_SHOW_PROGRESS);
        mPlayControlView.setProgress(progressTo);
    }

    private void onSurfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "onSurfaceCreated: ");
        if (null == mMediaPlayer) {
            mMediaPlayer = new MediaPlayer();
            if (!mSetDataSource) {
                Uri uri = Uri.parse(videoUri);
                try {
                    mMediaPlayer.setDataSource(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mSetDataSource = true;
            }
            addMediaListener();
        }
        mMediaPlayer.setDisplay(holder);

        if (mStartPlay) {
            mMediaPlayer.start();
            mHandler.sendEmptyMessageDelayed(MSG_SHOW_PROGRESS, TIME_INTERVAL);
        }
    }

    private void openVideo() {
        mPhotoImg.setVisibility(View.INVISIBLE);
        mPlayImg.setVisibility(View.INVISIBLE);
        try {
            mMediaPlayer.prepareAsync();//prepareAsync 非阻塞
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mPlayControlView.setMediaPlayer(mMediaPlayer);
                    mMediaPlayer.start();
                    showProgress();
                    mStartPlay = true;
                }
            });
        } catch (IllegalStateException e) {
            Log.d(TAG, "startPlay: error " + e);
            e.printStackTrace();
        }
    }

    public void stopPlay() {
        mMediaPlayer.stop();
    }

    @Override
    public void start() {
        mMediaPlayer.start();
        showProgress();
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
        removeMgsShowProgress();
    }

    @Override
    public void seekTo(int progress) {
        progressTo = progress;
        mMediaPlayer.seekTo(progress);
    }

    @Override
    public void fullScreen() {
        Toast.makeText(getContext(), "全屏", Toast.LENGTH_SHORT).show();
        RotationUitls.setRotation(mSurfaceView, 180);
    }


    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void removeMgsShowProgress() {
        mHandler.removeMessages(MSG_SHOW_PROGRESS);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        showControl();
        return false;
    }

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
                openVideo();
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
        release();
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
