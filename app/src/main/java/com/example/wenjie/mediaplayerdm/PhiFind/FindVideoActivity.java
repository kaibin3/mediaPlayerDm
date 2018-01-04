package com.example.wenjie.mediaplayerdm.PhiFind;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.wenjie.mediaplayerdm.PhiFind.entry.FindVideoEntry;
import com.example.wenjie.mediaplayerdm.R;

public class FindVideoActivity extends Activity implements FindVideoContract.View {
    private static final String TAG = "FindVideoActivity";
    public static final String VIDEO_ID = "video_id";

    private VideoPlayView videoPlayView;
    private ListView listView;

    private FindVideoPresenter mPresenter;
    private RecommendAdapter mAdapter;
    private String videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_video);
        videoId = getIntent().getStringExtra(VIDEO_ID);
        mPresenter = new FindVideoPresenter(this);
        init();
        mPresenter.loadInfo(videoId);
    }

    private void init() {
        videoPlayView = findViewById(R.id.video_play_view);
        listView = findViewById(R.id.recommend_list_view);
        listView.setFocusable(false);
        mAdapter = new RecommendAdapter(this);
        listView.setAdapter(mAdapter);
    }

    @Override
    public void upDate(FindVideoEntry videoEntry) {
        videoPlayView.setPhotoUrl(videoEntry.getPhotoUrl());
        videoPlayView.setVideoUrl(videoEntry.getVideoUrl());
        mAdapter.setData(videoEntry.getFindCardInfos());
    }
}
