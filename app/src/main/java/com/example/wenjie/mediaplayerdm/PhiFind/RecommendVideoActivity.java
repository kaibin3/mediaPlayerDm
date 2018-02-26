package com.example.wenjie.mediaplayerdm.PhiFind;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.wenjie.mediaplayerdm.PhiFind.entry.VideoCardInfo;
import com.example.wenjie.mediaplayerdm.PhiFind.entry.RecommendVideoEntry;
import com.example.wenjie.mediaplayerdm.PhiFind.videoplay.VideoPlayView;
import com.example.wenjie.mediaplayerdm.R;
import com.example.wenjie.mediaplayerdm.util.TitlebarUtils;
import com.phicomm.widgets.PhiTitleBar;

public class RecommendVideoActivity extends Activity implements RecommendVideoContract.View {
    private static final String TAG = "FindVideoActivity";
    public static final String VIDEO_ID = "video_id";
    public static final String TITLE = "title";
    public static final String IMAGE_URL = "image_url";
    public static final String VIDEO_URL = "video_url";

    private PhiTitleBar mPhiTitleBar;
    private VideoPlayView videoPlayView;
    private RecyclerView mRecyclerView;


    private RecommendVideoPresenter mPresenter;
    private RecommendVideoAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private String mVideoId;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_video);
        mVideoId = getIntent().getStringExtra(VIDEO_ID);
        mTitle = getIntent().getStringExtra(TITLE);
        mPresenter = new RecommendVideoPresenter(this);
        init();
        mPresenter.loadInfo(mVideoId);
    }

    private void init() {
        mPhiTitleBar = (PhiTitleBar) findViewById(R.id.phiTitleBar);
        TitlebarUtils.initTitleBar(this, R.string.empty_null);
        mPhiTitleBar.setTitle("人鱼线雕刻");
        mPhiTitleBar.addAction(new PhiTitleBar.TextAction("关闭") {
            @Override
            public void performAction(View view) {
                finish();
            }
        });
        videoPlayView = findViewById(R.id.video_play_view);
        mRecyclerView = findViewById(R.id.video_rlv);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecommendVideoAdapter(0,null);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VideoCardInfo cardInfo = (VideoCardInfo) adapter.getItem(position);

               // videoPlayView.setImage(cardInfo.getPhotoUrl());
              //  videoPlayView.setVideoUri(cardInfo.getVideoUrl());
            }
        });

    }

    @Override
    public void upDate(RecommendVideoEntry videoEntry) {
        videoPlayView.setImage(videoEntry.getPhotoUri());
        videoPlayView.setVideoUri(videoEntry.getVideoUri());
        videoPlayView.setIsNetVideo(true);
        mAdapter.setNewData(videoEntry.getRecommendInfo());
    }
}
