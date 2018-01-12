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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wenjie.mediaplayerdm.R;
import com.example.wenjie.mediaplayerdm.util.Constants;
import com.example.wenjie.mediaplayerdm.util.ViewLog;
import com.example.wenjie.mediaplayerdm.util.WindowUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    private int measuredWidth;
    private int measuredHeight;

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

    @OnClick(R.id.full_screen)
    void fullScreenAction() {
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int heightPixels = getResources().getDisplayMetrics().heightPixels;


      /*  RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mSurfaceView.getLayoutParams();
        int width1 = params.width;
        int width = mSurfaceView.getLayoutParams().width;//LayoutParams.width可以在onMesure方法中获取  如果是wrap_content也是不能获取的


        int measuredWidth = mSurfaceView.getMeasuredWidth();//在onMeasure()执行完后才会有值


        int width2 = mSurfaceView.getWidth();//在layout执行完后才能获取宽度,在onMeasure()方法中拿不到


        mSurfaceView.getWidth();

        params.width = heightPixels;
        params.height = widthPixels;*/


        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 90, 0.5f, 0.5f);
        rotateAnimation.setDuration(1000);

        //mSurfaceView.startAnimation(animation);

        changeScreenOrientation();

        //  getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //  getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);

        //   button.startAnimation(animation);

        //  setOnLand1();

    }

    private void changeScreenOrientation() {
        if (!WindowUtils.isOrientationLand(getActivity())) {
            WindowUtils.setOrientationLand(getActivity());
            Log.d(TAG, "fullScreenAction: setOrientationLand");
            mFullScreenImg.setImageDrawable(getResources().getDrawable(R.drawable.little_screen));
        } else {
            Log.d(TAG, "fullScreenAction: setOrientationPort");
            WindowUtils.setOrientationPort(getActivity());
            mFullScreenImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_full_screen));
        }
    }

    private void setOnLand1() {
        ViewGroup.LayoutParams layoutParams = mSurfaceView.getLayoutParams();
        ViewLog.widthHeight(mSurfaceView);
        // Log.d(TAG, "setOnLand1: "+ layoutParams.width+"     "+ layoutParams.height);

        layoutParams.width = WindowUtils.screenWidth();
        layoutParams.height = WindowUtils.screenHeight();

        mSurfaceView.setLayoutParams(layoutParams);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (Configuration.ORIENTATION_PORTRAIT == newConfig.orientation) {
            //    setOnPort();
        } else if ((Configuration.ORIENTATION_LANDSCAPE == newConfig.orientation)) {
            //    setOnLand();
        }


        Log.d(TAG, "onConfigurationChanged: " + newConfig.orientation);
        // reInitView(newConfig);
    }


    private void reInitView(Configuration newConfig) {
        if (Configuration.ORIENTATION_PORTRAIT == newConfig.orientation) {
            setOnPort();
        } else if ((Configuration.ORIENTATION_LANDSCAPE == newConfig.orientation)) {
            setOnLand();
        }
    }

    private void setOnPort() {
        ViewGroup.LayoutParams layoutParams = mSurfaceView.getLayoutParams();
        layoutParams.width = measuredWidth;
        layoutParams.height = measuredHeight;

        mSurfaceView.setLayoutParams(layoutParams);
    }

    private void setOnLand() {
        ViewGroup.LayoutParams layoutParams = mSurfaceView.getLayoutParams();
        layoutParams.width = WindowUtils.screenHeight();
        layoutParams.height = WindowUtils.screenWidth();

        mSurfaceView.setLayoutParams(layoutParams);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    public boolean onKeyBack() {
        if (WindowUtils.isOrientationLand(getActivity())) {
            WindowUtils.setOrientationPort(getActivity());
            mFullScreenImg.setImageDrawable(getResources().getDrawable(R.drawable.little_screen));
        }
        return true;
    }

    public LayoutInflater getmInflater() {
        return mInflater;
    }

    public ViewGroup getmContainer() {
        return mContainer;
    }

    public Bundle getmSavedInstanceState() {
        return mSavedInstanceState;
    }
}
