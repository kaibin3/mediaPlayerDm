package com.example.wenjie.mediaplayerdm.onLineViedio;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.wenjie.mediaplayerdm.R;
import com.example.wenjie.mediaplayerdm.util.Constants;

/**
 * Created by wen.jie on 2018/1/2.
 */

public class VideoFragment extends Fragment {

    View mFragmentView;
    VideoView videoView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragmrnt_video, container, false);
        return mFragmentView;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        videoView = mFragmentView.findViewById(R.id.video_view);


        //网络视频
        String videoUrl2 = Constants.localVideoUrl2;

        Uri uri = Uri.parse(videoUrl2);


        //设置视频控制器
        videoView.setMediaController(new android.widget.MediaController(getActivity()));

        //播放完成回调
        videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());

        //设置视频路径
        videoView.setVideoURI(uri);

        //开始播放视频
        videoView.start();


    }


    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(getActivity(), "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }
}
