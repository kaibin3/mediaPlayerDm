package com.example.wenjie.mediaplayerdm;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.wenjie.mediaplayerdm.data.local.database.MediaDao;
import com.example.wenjie.mediaplayerdm.util.MediaUtil;

/**
 * Created by wen.jie on 2017/12/27.
 */

public class MediaPlayFragment extends Fragment {
    private static final String TAG = "MediaPlayFragment";

    private View mFragmentView;
    private View view;
    private SeekBar seekBar;
    private TextView tv_second;
    private TextView tv_durate;
    private ImageView iv_play;
    private ImageView iv_forward;
    private ImageView iv_rewind;
    private Button bt_add;
    private Button bt_reduce;
    private LinearLayout play_bg;
    private SurfaceView mSurfaceView;

    private MediaPlayer mPlayer;
    private SurfaceHolder mHolder;
    private AudioManager mAudioManager;

    private String path = null;
    private String durate = null;
    private long durateTime = -1;

    private int maxVolume;
    private int currentVolume;
    private int recode = -1;
    private int mediaId = -1;
    private int playModeCheckedId = -1;
    private int currentPosition = 0;
    private int showTime = 0;

    private String playTheme;
    private MediaDao mediaDao;
    private WindowManager wmManager;
    private WindowManager.LayoutParams wmManagerParams;
    private MyListener listener;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x0000:
                    if (currentPosition < durateTime) {
                        seekBar.setProgress(currentPosition);
                        tv_second.setText(MediaUtil.formatTime(currentPosition) + "/");
                        currentPosition += 1000;
                        handler.sendEmptyMessageDelayed(0x0000, 1000);
                    } else {
                        handler.removeMessages(0x0000);
                        seekBar.setProgress(0);
                        tv_second.setText("00:00:00" + "/");
                        iv_play.setBackgroundResource(R.drawable.play);
                        currentPosition = 0;
                    }
                    break;
                case 0x0001:
                    if (showTime < 3000) {
                        showTime += 1000;
                        handler.sendEmptyMessageDelayed(0x0001, 1000);
                    } else {
                        view.setVisibility(View.INVISIBLE);
                        handler.removeMessages(0x0001);
                    }
                    break;

                default:
                    break;
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_media_play, container, false);
        return mFragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent = getActivity().getIntent();
        path = intent.getStringExtra("path");
        Log.i("path", path);
        durate = intent.getStringExtra("durate");          //
        durateTime = intent.getLongExtra("durateTime", -1);  //
        currentPosition = intent.getIntExtra("recode", 0);
        mediaId = intent.getIntExtra("id", 0);
        playModeCheckedId = intent.getIntExtra("playModeCheckedId", 0);
        playTheme = intent.getStringExtra("theme");

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        setPlayTheme(playTheme);
        addView();
        findView();
        registered();
        setData();

        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.i("surfaceCreated", "surfaceCreated");
                load(path);
                mPlayer.setDisplay(holder);
                handler.sendEmptyMessage(0x0000);
                mPlayer.start();


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {

            }
        });
        mFragmentView.setOnTouchListener(onTouchListener);
    }

    /**
     * 播放器加载文件
     *
     * @param path
     */
    private void load(String path) {
        if (mPlayer != null && mPlayer.isPlaying()) {//主要避免声音重叠
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        if (path != null) {
            Log.i("path", path);
            try {
                mPlayer = new MediaPlayer();
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        switch (playModeCheckedId + 1) {
                            case 1://单个播放
                                mPlayer.pause();
                                mPlayer.seekTo(0);
                                break;
                            case 2://单个循环
                                currentPosition = 0;
                                mPlayer.seekTo(0);
                                mPlayer.start();
                                break;
                            default://默认单个播放
                                mPlayer.pause();
                                mPlayer.seekTo(0);
                                break;
                        }

                    }
                });
                mPlayer.reset();
                mPlayer.setDataSource(path);
                mPlayer.prepare();
                if (currentPosition > 0) {
                    mPlayer.seekTo(currentPosition);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    /**
     * 点击事件让悬浮窗出现
     */
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                handler.removeMessages(0x0001);
                showTime = 0;
                view.setVisibility(View.VISIBLE);
                handler.sendEmptyMessage(0x0001);
            }
            return false;
        }
    };


    /**
     * 查找控件
     */
    private void findView() {
        mSurfaceView = (SurfaceView) mFragmentView.findViewById(R.id.player_view);
        play_bg = (LinearLayout) mFragmentView.findViewById(R.id.play_bg);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        tv_second = (TextView) view.findViewById(R.id.tv_second);
        tv_durate = (TextView) view.findViewById(R.id.tv_durate);
        iv_play = (ImageView) view.findViewById(R.id.play);
        iv_rewind = (ImageView) view.findViewById(R.id.rewind);
        iv_forward = (ImageView) view.findViewById(R.id.forward);
        bt_add = (Button) view.findViewById(R.id.Bt_add);
        bt_reduce = (Button) view.findViewById(R.id.Bt_reduce);


    }


    /**
     * 添加悬浮窗
     */
    private void addView() {
        wmManager = (WindowManager) getActivity().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        wmManagerParams = new WindowManager.LayoutParams();


        wmManagerParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        wmManagerParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 不接受任何按键事件
        wmManagerParams.gravity = Gravity.LEFT | Gravity.BOTTOM; // 调整悬浮窗口至左下角
        // 以屏幕左上角为原点，设置x、y初始值
        wmManagerParams.x = 0;
        wmManagerParams.y = 0;
        // 设置悬浮窗口长宽数据
        wmManagerParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmManagerParams.format = PixelFormat.RGBA_8888;
        view = View.inflate(getActivity(), R.layout.play_contorl_win, null);
        view.setVisibility(View.GONE);
        wmManager.addView(view, wmManagerParams);


    }


    /**
     * 注册监听事件
     */
    private void registered() {
        Log.i("registered", "registered");
        listener = new MyListener();
        iv_play.setOnClickListener(listener);
        iv_rewind.setOnClickListener(listener);
        iv_forward.setOnClickListener(listener);
        bt_add.setOnClickListener(listener);
        bt_reduce.setOnClickListener(listener);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mPlayer.isPlaying()) {
                    handler.sendEmptyMessage(0x0000);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(0x0000);

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    currentPosition = progress;
                    mPlayer.seekTo(progress);
                }

            }
        });

    }


    /**
     * 设置数据
     */
    private void setData() {
        seekBar.setMax((int) durateTime);
        Log.i("seekBar Max()", seekBar.getMax() + "/");
        Log.i("setdata", recode + "");
        tv_durate.setText(durate);
        tv_second.setText(MediaUtil.formatTime(currentPosition) + "/");
    }


    /**
     * 悬浮窗内控件点击事件监听类
     *
     * @author Administrator
     */
    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.play:
                    if (mPlayer.isPlaying()) {  //播放状态
                        handler.removeMessages(0x0000);
                        mPlayer.pause();
                        v.setBackgroundResource(R.drawable.play);

                    } else {              //停止状态
                        handler.sendEmptyMessage(0x0000);
                        mPlayer.start();
                        v.setBackgroundResource(R.drawable.pause);

                    }
                    break;
                case R.id.rewind:      //快退
                    Log.i("R.id.rewind", "R.id.rewind");
                    if (seekBar.getProgress() - 5000 > 0) {
                        seekBar.setProgress(seekBar.getProgress() - 5000);
                        currentPosition -= 5000;
                        mPlayer.seekTo(currentPosition);
                        tv_second.setText(MediaUtil.formatTime(currentPosition) + "/");
                    } else {
                        seekBar.setProgress(0);
                        currentPosition = 0;
                        tv_second.setText("00:00:00" + "/");

                    }

                    break;
                case R.id.forward:       //快进
                    if (seekBar.getProgress() + 5000 <= durateTime) {
                        seekBar.setProgress(seekBar.getProgress() + 5000);
                        currentPosition += 5000;
                        tv_second.setText(MediaUtil.formatTime(currentPosition) + "/");
                        mPlayer.seekTo(currentPosition);
                    } else {
                        seekBar.setProgress(seekBar.getMax());
                        currentPosition = seekBar.getMax();
                        tv_second.setText(MediaUtil.formatTime(durateTime) + "/");

                    }
                    break;
                case R.id.Bt_add:    //增加音量
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,
                            AudioManager.FLAG_PLAY_SOUND);
                    break;
                case R.id.Bt_reduce: //减少音量
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER,
                            AudioManager.FLAG_PLAY_SOUND);
                    break;
                default:
                    break;
            }

        }

    }


    /**
     * 设置主题
     *
     * @param theme
     */
    private void setPlayTheme(String theme) {
        if ("彩色".equals(theme)) {
            play_bg.setBackgroundResource(R.drawable.bg_color);
        } else if ("绿色".equals(theme)) {
            play_bg.setBackgroundResource(R.drawable.bg_green);
        } else if ("蓝色".equals(theme)) {
            play_bg.setBackgroundResource(R.drawable.bg_blur);
        } else if ("雪景".equals(theme)) {
            play_bg.setBackgroundResource(R.drawable.bg_snow);
        }

    }


    @Override
    public void onPause() {
        Log.i("PlayActivity.onPause ", "onPause");

        super.onPause();
    }


    /**
     * PlayActivity停止时 播放器暂停，移除悬浮窗，
     */
    @Override
    public void onStop() {
        Log.i("PlayActivity.onStop ", "onStop");
        mPlayer.pause();
        handler.removeMessages(0x0000);
        view.setVisibility(View.GONE);

        super.onStop();

    }

    /**
     * PlayActivity销毁时 保存播放记录到数据库，移除悬浮窗
     */
    @Override
    public void onDestroy() {
        Log.i("PlayActivity.onDestroy ", "onDestroy");
        currentPosition = mPlayer.getCurrentPosition();
        Log.i("PlayActivity.onDestroy ", currentPosition + "");
        mediaDao = new MediaDao(getActivity());
        mediaDao.updateRecode(mediaId, currentPosition);

        mPlayer.release();
        mPlayer = null;
        wmManager.removeView(view);
        super.onDestroy();
    }

}
