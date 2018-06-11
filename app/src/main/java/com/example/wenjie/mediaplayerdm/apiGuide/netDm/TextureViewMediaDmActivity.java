package com.example.wenjie.mediaplayerdm.apiGuide.netDm;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.wenjie.mediaplayerdm.R;
import com.example.wenjie.mediaplayerdm.util.Constants;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author wen.jie
 * @date 2018/1/12
 * <p>
 * http://blog.csdn.net/lowprofile_coding/article/details/46806783
 */

public class TextureViewMediaDmActivity extends Activity {
    private static final String TAG = "TextureViewMediaDmActiv";
    private TextureView mTextureView;
    private MediaPlayer mMediaPlayer;
    private Surface mSurface;
    private ImageView mVideoImage;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_view_media_dm);
        mTextureView = findViewById(R.id.textureview);
        //设置监听函数  重写4个方法
        mTextureView.setSurfaceTextureListener(surfaceTextureListener);

        mVideoImage = findViewById(R.id.video_image);
        findViewById(R.id.play_btn).setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startPlay();
        }
    };

    private void startPlay() {
        new PlayerVideo().start();//开启一个线程去播放视频
    }


    TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            System.out.println("onSurfaceTextureAvailable onSurfaceTextureAvailable");
            mSurface = new Surface(surfaceTexture);
            //new PlayerVideo().start();//开启一个线程去播放视频
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            System.out.println("onSurfaceTextureSizeChanged onSurfaceTextureSizeChanged");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            System.out.println("onSurfaceTextureDestroyed onSurfaceTextureDestroyed");
            surfaceTexture = null;
            mSurface = null;
            if (null != mMediaPlayer) {
                mMediaPlayer.stop();
            }
            if (null != mMediaPlayer) {
                mMediaPlayer.release();
            }
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
            System.out.println("onSurfaceTextureUpdated onSurfaceTextureUpdated");
        }
    };


    private class PlayerVideo extends Thread {
        @Override
        public void run() {
            Log.d(TAG, "PlayThread Thread: " + Thread.currentThread().getId() + " " + Thread.currentThread().getName());

            try {
              /*  File file = new File(Environment.getExternalStorageDirectory() + "/ansen.mp4");
                if (!file.exists()) {//文件不存在
                    copyFile();
                }*/
                mMediaPlayer = new MediaPlayer();
                String localVideoUrl2 = Constants.localVideoUrl2;
                Uri uri = Uri.parse(localVideoUrl2);

                mMediaPlayer.setDataSource(getApplicationContext(), uri);
                //Surface
                mMediaPlayer.setSurface(mSurface);

                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Log.d(TAG, "PlayThread Thread: " + Thread.currentThread().getId() + " " + Thread.currentThread().getName());
                        mVideoImage.setVisibility(View.GONE);
                        mMediaPlayer.start();
                    }
                });
                mMediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface PlayerController {
        public void play();
    }

    /**
     * 如果sdcard没有文件就复制过去
     */
    private void copyFile() {
        AssetManager assetManager = this.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open("ansen.mp4");
            String newFileName = Environment.getExternalStorageDirectory() + "/ansen.mp4";
            out = new FileOutputStream(newFileName);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

}
