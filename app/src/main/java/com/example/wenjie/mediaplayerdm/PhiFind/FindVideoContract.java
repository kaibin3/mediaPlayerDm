package com.example.wenjie.mediaplayerdm.PhiFind;

import com.example.wenjie.mediaplayerdm.PhiFind.entry.FindVideoEntry;

/**
 * Created by wen.jie on 2018/1/3.
 */

public interface FindVideoContract {

    interface Presenter {
        void loadInfo(String id);
    }

    interface View {
        void upDate(FindVideoEntry videoEntry);
    }
}
