package com.example.wenjie.mediaplayerdm.PhiFind.entry;

import com.example.wenjie.mediaplayerdm.PhiFind.VideoPlayView;

import java.util.List;

/**
 * Created by wen.jie on 2018/1/4.
 */

public class FindVideoEntry {

    private String photoUrl;
    private String VideoUrl;
    private List<FindCardInfo> recommendInfo;
    private VideoPlayView.VideoPlayInfo mediaInfo;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public List<FindCardInfo> getRecommendInfo() {
        return recommendInfo;
    }

    public void setRecommendInfo(List<FindCardInfo> findCardInfos) {
        this.recommendInfo = findCardInfos;
    }

    public VideoPlayView.VideoPlayInfo getMediaInfo() {
        return mediaInfo;
    }

    public void setMediaInfo(VideoPlayView.VideoPlayInfo mediaInfo) {
        this.mediaInfo = mediaInfo;
    }

    @Override
    public String toString() {
        return "FindVideoEntry{" +
                "photoUrl='" + photoUrl + '\'' +
                ", VideoUrl='" + VideoUrl + '\'' +
                ", recommendInfo=" + recommendInfo +
                ", mediaInfo=" + mediaInfo +
                '}';
    }
}
