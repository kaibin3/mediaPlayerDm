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
        entry.setPhotoUrl("https://www.baidu.com/img/bd_logo1.png");
        entry.setVideoUrl(Constants.phiVideoUrl);

        List<FindCardInfo> findCardInfos = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            FindCardInfo findCardInfo = new FindCardInfo();
            findCardInfo.setDescription("徒手胸肌初级训练");
            findCardInfo.setLabel("健身训练");
            findCardInfo.setPhotoUrl("https://gss0.bdstatic.com/5bVWsj_p_tVS5dKfpU_Y_D3/res/r/image/2017-09-27/297f5edb1e984613083a2d3cc0c5bb36.png");
            findCardInfos.add(findCardInfo);
        }
        entry.setFindCardInfos(findCardInfos);

        mView.upDate(entry);
    }
}
