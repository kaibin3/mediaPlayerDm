package com.example.wenjie.mediaplayerdm.PhiFind;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wenjie.mediaplayerdm.PhiFind.entry.FindCardInfo;
import com.example.wenjie.mediaplayerdm.PhiFind.entry.FindVideoEntry;
import com.example.wenjie.mediaplayerdm.R;
import com.example.wenjie.mediaplayerdm.util.TitlebarUtils;
import com.phicomm.widgets.PhiTitleBar;

public class FindVideoActivity extends Activity implements FindVideoContract.View {
    private static final String TAG = "FindVideoActivity";
    public static final String VIDEO_ID = "video_id";
    public static final String TITLE = "title";

    private PhiTitleBar mPhiTitleBar;
    private VideoPlayView videoPlayView;
    private ListView listView;

    private FindVideoPresenter mPresenter;
    private RecommendAdapter mAdapter;
    private String mVideoId;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_video);
        mVideoId = getIntent().getStringExtra(VIDEO_ID);
        mTitle = getIntent().getStringExtra(TITLE);
        mPresenter = new FindVideoPresenter(this);
        init();
        mPresenter.loadInfo(mVideoId);
    }

    private void init() {
        mPhiTitleBar = findViewById(R.id.phiTitleBar);
        TitlebarUtils.initTitleBar(this, R.string.renyu);
        mPhiTitleBar.addAction(new PhiTitleBar.TextAction("关闭") {
            @Override
            public void performAction(View view) {
                finish();
            }
        });
        videoPlayView = findViewById(R.id.video_play_view);
        listView = findViewById(R.id.recommend_list_view);
        listView.setFocusable(false);
        mAdapter = new RecommendAdapter(this);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FindCardInfo cardInfo = mAdapter.getItem(position);
                videoPlayView.stopPlay();
                videoPlayView.setPhotoUri(cardInfo.getPhotoUrl());
                videoPlayView.setVideoUri(cardInfo.getVideoUrl());
            }
        });
    }

    @Override
    public void upDate(FindVideoEntry videoEntry) {
        videoPlayView.setMediaInfo(videoEntry.getMediaInfo());
        //videoPlayView.setPhotoUrl(videoEntry.getPhotoUrl());
        //videoPlayView.setVideoUrl(videoEntry.getVideoUrl());
        mAdapter.setData(videoEntry.getRecommendInfo());
    }
}
