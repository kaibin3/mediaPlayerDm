package com.example.wenjie.mediaplayerdm.PhiFind.entry;

import java.util.List;

/**
 * Created by wen.jie on 2018/1/4.
 */

public class FindVideoEntry {

    private String photoUrl;
    private String VideoUrl;
    private List<FindCardInfo> findCardInfos;


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

    public List<FindCardInfo> getFindCardInfos() {
        return findCardInfos;
    }

    public void setFindCardInfos(List<FindCardInfo> findCardInfos) {
        this.findCardInfos = findCardInfos;
    }

    @Override
    public String toString() {
        return "FindVideoEntry{" +
                "photoUrl='" + photoUrl + '\'' +
                ", VideoUrl='" + VideoUrl + '\'' +
                ", findCardInfos=" + findCardInfos +
                '}';
    }
}
