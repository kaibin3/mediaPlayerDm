package com.example.wenjie.mediaplayerdm.PhiFind.entry;

import java.util.List;

/**
 * Created by wen.jie on 2018/1/4.
 */

public class RecommendVideoEntry {

    /*private String photoUri;
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

    *//*public VideoPlayView.VideoPlayInfo getMediaInfo() {
        return mediaInfo;
    }

    public void setMediaInfo(VideoPlayView.VideoPlayInfo mediaInfo) {
        this.mediaInfo = mediaInfo;
    }*//*

    @Override
    public String toString() {
        return "FindVideoEntry{" +
                "photoUrl='" + photoUri + '\'' +
                ", VideoUrl='" + videoUri + '\'' +
                ", recommendInfo=" + recommendInfo +
                '}';
    }*/


    private String photoUri;
    private String videoUri;


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

    private List<CardData> recommendCards;


    public List<CardData> getRecommendCards() {
        return recommendCards;
    }

    public void setRecommendCards(List<CardData> recommendCards) {
        this.recommendCards = recommendCards;
    }

    public static class CardData {
        private int id;
        private String title;
        private String subtitle;
        private String tags;
        private String url;
        private String summary;
        private String coverUrl;
        private String pictureUrl;
        private String type;
        private int sequence;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }
    }

}
