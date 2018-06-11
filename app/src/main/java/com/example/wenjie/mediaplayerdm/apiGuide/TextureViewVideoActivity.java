package com.example.wenjie.mediaplayerdm.apiGuide;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.TextureView;

import com.example.wenjie.mediaplayerdm.R;

/**
 * @author wen.jie
 */
public class TextureViewVideoActivity extends AppCompatActivity {


    private TextureView mTextureView;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_view_video);
        initView();
        initTextureView();
    }

    private void initView() {
        mTextureView = findViewById(R.id.texture_view);
    }




    private void initTextureView() {
        mTextureView.setSurfaceTextureListener(mSurfaceListener);
    }

    TextureView.SurfaceTextureListener mSurfaceListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

}
