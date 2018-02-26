package com.example.wenjie.mediaplayerdm.onLineViedio;

import android.app.Fragment;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wenjie.mediaplayerdm.R;
import com.example.wenjie.mediaplayerdm.util.Constants;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wen.jie on 2018/1/2.
 */

public class OnLinePlayFragment extends Fragment {
    public static final String TAG = "OnLinePlayFragment";
    private View mFragmentView;
    private SurfaceView mSurfaceView;
    @BindView(R.id.full_screen)
    ImageView mFullScreenImg;
    @BindView(R.id.current_time)
    TextView currentTimeView;
    @BindView(R.id.end_time)
    TextView endTimeView;


    private MediaPlayer mMediaPlayer;
    private SurfaceHolder mSurfaceHolder;

    private String videoUrl2 = Constants.localVideoUrl3;

    private AudioManager mAudioManager;
    private Unbinder unbinder;

    LayoutInflater mInflater;
    ViewGroup mContainer;
    Bundle mSavedInstanceState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        mContainer = container;
        mSavedInstanceState = savedInstanceState;

        mFragmentView = inflater.inflate(R.layout.fragment_on_line, container, false);
        unbinder = ButterKnife.bind(this, mFragmentView);
        Log.d(TAG, "onCreateView: ");
        return mFragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initPlayer();
    }

    private void initView() {
        mSurfaceView = mFragmentView.findViewById(R.id.on_line_surface_view);
    }

    private void initPlayer() {
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d(TAG, "surfaceCreated: ");
                onSurfaceCreated(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });


    }

    private void onSurfaceCreated(SurfaceHolder holder) {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDisplay(holder);
        Uri uri = Uri.parse(videoUrl2);
        try {
            mMediaPlayer.setDataSource(uri.toString());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            Log.d(TAG, "configSurfaceCreated: " + e);
            e.printStackTrace();
        }

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged: " + newConfig.orientation);
    }

    public boolean onKeyBack() {
        /*if (WindowUtils.isOrientationLand(getActivity())) {
            WindowUtils.setOrientationPort(getActivity());
            mFullScreenImg.setImageDrawable(getResources().getDrawable(R.drawable.little_screen));
        }*/
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


}
