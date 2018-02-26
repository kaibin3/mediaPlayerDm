package com.example.wenjie.mediaplayerdm.PhiFind;

import android.content.Context;

import com.example.wenjie.mediaplayerdm.PhiFind.entry.RecommendVideoEntry;

/**
 * Created by wen.jie on 2018/1/3.
 */

public interface RecommendVideoContract {




    interface Presenter {
        void loadInfo(String id);
    }

    interface View {
        void upDate(RecommendVideoEntry videoEntry);
        Context getContext();
        void showNoNetwork();
    }
}
