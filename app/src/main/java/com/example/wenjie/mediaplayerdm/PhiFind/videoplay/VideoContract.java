package com.example.wenjie.mediaplayerdm.PhiFind.videoplay;


public interface VideoContract {

    interface VideoControl {
        void startPlay();
        void onPlayerStart();
        void setVideoPlayer(VideoContract.VideoPlayer mediaPlayer);
        void setScreenMode(int screenMode);
        void show();
        void dismiss();
        void setImage(String url);
        void setTitle(String title);
        void showProgress();
        void showLoading();
        void dismissLoading();
        void showNoNetwork();
        void showOnMobileNetwork();
        void showOnError();
        void dismissError();
    }

    interface VideoPlayer {
        void startPlay();
        void play();
        void pause();
        void seekTo(int pos);
        boolean fullScreen();
        boolean exitFullScreen();
        int getDuration();
        int getCurrentPosition();
        boolean isPlaying();
        boolean isStarted();
        boolean isNetVideo();
        boolean backPressed();
        void resume();
        void suspend();
        void phoneRing();
        void phoneIdle();
    }


}
