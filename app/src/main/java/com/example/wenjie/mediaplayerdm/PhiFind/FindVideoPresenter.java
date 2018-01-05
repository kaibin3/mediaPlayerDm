package com.example.wenjie.mediaplayerdm.PhiFind;

import com.example.wenjie.mediaplayerdm.PhiFind.entry.FindCardInfo;
import com.example.wenjie.mediaplayerdm.PhiFind.entry.FindVideoEntry;
import com.example.wenjie.mediaplayerdm.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wen.jie on 2018/1/3.
 */

public class FindVideoPresenter implements FindVideoContract.Presenter {

    private FindVideoContract.View mView;

    public FindVideoPresenter(FindVideoContract.View view) {
        mView = view;
    }


    @Override
    public void loadInfo(String id) {
        FindVideoEntry entry = new FindVideoEntry();

        VideoPlayView.VideoPlayInfo mediaInfo = new VideoPlayView.VideoPlayInfo();
        mediaInfo.setPhotoUrl(Constants.baiduPhotoUrl);
        mediaInfo.setVideoUrl(Constants.phiVideoUrl);
        entry.setMediaInfo(mediaInfo);

        List<FindCardInfo> findCardInfos = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            FindCardInfo findCardInfo = new FindCardInfo();
            findCardInfo.setVideoUrl(Constants.phiVideoUrl2);
            findCardInfo.setPhotoUrl(Constants.hao123photoUrl);
            findCardInfo.setDescription("徒手胸肌初级训练");
            findCardInfo.setLabel("健身训练");
            findCardInfos.add(findCardInfo);
        }
        entry.setRecommendInfo(findCardInfos);

        mView.upDate(entry);
    }
}
