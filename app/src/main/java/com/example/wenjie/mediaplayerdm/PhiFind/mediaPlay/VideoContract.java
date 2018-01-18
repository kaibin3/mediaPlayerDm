package com.example.wenjie.mediaplayerdm.PhiFind.mediaPlay;


public interface VideoContract {

    interface VideoControl {
        void onPlayerStart();
        void setVideoPlayer(VideoContract.VideoPlayer mediaPlayer);
        void setScreenMode(int screenMode);
        void show();
        void dismiss();
        void setImage(String uri);
        void setTitle(String title);
    }

    interface VideoPlayer {
        void startPlay();
        void play();
        void pause();
        void seekTo(int pos);
        void fullScreen();
        void exitFullScreen();
        //----------------
        int getDuration();
        int getCurrentPosition();
        boolean isPlaying();
        boolean isStarted();
    }


    /*

      void    start();
        void    pause();
        int     getDuration();
        int     getCurrentPosition();
        void    seekTo(int pos);
        boolean isPlaying();
        int     getBufferPercentage();
        boolean canPause();
        boolean canSeekBackward();
        boolean canSeekForward();

        int     getAudioSessionId();

    * */
}
