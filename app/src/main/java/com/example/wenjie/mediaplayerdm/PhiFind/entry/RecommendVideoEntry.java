package com.example.wenjie.mediaplayerdm.PhiFind.entry;

import java.util.List;

/**
 * Created by wen.jie on 2018/1/4.
 */

public class RecommendVideoEntry {

    private String photoUri;
    private String videoUri;
    private List<VideoCardInfo> recommendInfo;
    //private VideoPlayView.VideoPlayInfo mediaInfo;

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

    public List<VideoCardInfo> getRecommendInfo() {
        return recommendInfo;
    }

    public void setRecommendInfo(List<VideoCardInfo> findCardInfos) {
        this.recommendInfo = findCardInfos;
    }

    /*public VideoPlayView.VideoPlayInfo getMediaInfo() {
        return mediaInfo;
    }

    public void setMediaInfo(VideoPlayView.VideoPlayInfo mediaInfo) {
        this.mediaInfo = mediaInfo;
    }*/

    @Override
    public String toString() {
        return "FindVideoEntry{" +
                "photoUrl='" + photoUri + '\'' +
                ", VideoUrl='" + videoUri + '\'' +
                ", recommendInfo=" + recommendInfo +
                '}';
    }
}
