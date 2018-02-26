package com.example.wenjie.mediaplayerdm.PhiFind;

import com.example.wenjie.mediaplayerdm.PhiFind.entry.RecommendVideoEntry;
import com.example.wenjie.mediaplayerdm.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wen.jie on 2018/1/3.
 */

public class RecommendVideoPresenter implements RecommendVideoContract.Presenter {

    private RecommendVideoContract.View mView;

    public RecommendVideoPresenter(RecommendVideoContract.View view) {
        mView = view;
    }


    @Override
    public void loadInfo(String id) {
        RecommendVideoEntry entry = new RecommendVideoEntry();

       /* VideoPlayView.VideoPlayInfo mediaInfo = new VideoPlayView.VideoPlayInfo();
        mediaInfo.setPhotoUri(Constants.baiduPhotoUrl);
        mediaInfo.setVideoUri(Constants.localVideoUrl2);
        entry.setMediaInfo(mediaInfo);*/
        entry.setPhotoUri(Constants.baiduPhotoUrl);
        entry.setVideoUri(Constants.localVideoUrl2);

        List<RecommendVideoEntry.CardData> findCardInfos = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            RecommendVideoEntry.CardData findCardInfo = new RecommendVideoEntry.CardData();
            findCardInfo.setUrl(Constants.phiVideoUrl2);
            findCardInfo.setPictureUrl(Constants.hao123photoUrl);
            findCardInfo.setTitle("徒手胸肌初级训练");
            findCardInfo.setTags("健身训练");
            findCardInfos.add(findCardInfo);
        }
        entry.setRecommendCards(findCardInfos);

        mView.upDate(entry);
    }
}
