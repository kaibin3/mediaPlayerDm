package com.example.wenjie.mediaplayerdm.PhiFind;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.wenjie.mediaplayerdm.PhiFind.entry.RecommendVideoEntry;
import com.example.wenjie.mediaplayerdm.PhiFind.videoplay.VideoPlayView;
import com.example.wenjie.mediaplayerdm.R;
import com.example.wenjie.mediaplayerdm.util.Constants;
import com.example.wenjie.mediaplayerdm.util.PermissionUtils;
import com.example.wenjie.mediaplayerdm.util.TitlebarUtils;
import com.phicomm.widgets.PhiTitleBar;

public class RecommendVideoActivity extends Activity implements RecommendVideoContract.View {
    private static final String TAG = "FindVideoActivity";
    private static final int REQUEST_NEW_PAGE = 1;
    public static final String VIDEO_ID = "video_id";
    public static final String TITLE = "title";
    public static final String IMAGE_URL = "image_url";
    public static final String VIDEO_URL = "video_url";
    public static final String TYPE = "type";

    private PhiTitleBar mPhiTitleBar;
    private VideoPlayView mVideoPlayView;
    private RecyclerView mRecyclerView;
    private LinearLayout noNetworkLayout;
    private TextView tvNoNetwork;
    private ScrollView scrollView;
    private TextView mRecommendTitle;

    private RecommendVideoContract.Presenter mPresenter;
    private RecommendVideoAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private String videoId;
    private String title;
    private String imageUrl;
    private String videoUrl;

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            if (requestCode == PermissionUtils.CODE_READ_PHONE_STATE) {
                registerPhoneStateListener();
            }
        }
    };

    private BroadcastReceiver mPhoneStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int currentCallState = telephonyManager.getCallState();
            if (currentCallState == TelephonyManager.CALL_STATE_IDLE) {
                //空闲
                mVideoPlayView.phoneIdle();
            } else if (currentCallState == TelephonyManager.CALL_STATE_RINGING) {
                //响铃
                mVideoPlayView.phoneRing();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_video);
        videoId = getIntent().getStringExtra(VIDEO_ID);
        title = getIntent().getStringExtra(TITLE);
        imageUrl = getIntent().getStringExtra(IMAGE_URL);
        videoUrl = getIntent().getStringExtra(VIDEO_URL);
        mPresenter = new RecommendVideoPresenter(this);
        // mPresenter.setCompositeSubscription(mCompositeSubscription);

        imageUrl = Constants.baiduPhotoUrl;
        videoUrl = Constants.localVideoUrl2;

        init();
        initPlayer();
        mPresenter.loadInfo(videoId);
        checkPhoneStatePermission();
    }

    private void init() {
        mPhiTitleBar = (PhiTitleBar) findViewById(R.id.phiTitleBar);
        TitlebarUtils.initTitleBar(this, R.string.empty_null);
        mPhiTitleBar.setTitle(title);
        mPhiTitleBar.setActionTextColor(R.color.white);
        mPhiTitleBar.addAction(new PhiTitleBar.TextAction("关闭") {
            @Override
            public void performAction(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
        mVideoPlayView = (VideoPlayView) findViewById(R.id.video_play_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.video_rlv);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        mAdapter = new RecommendVideoAdapter(0, null);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RecommendVideoEntry.CardData cardData = (RecommendVideoEntry.CardData) adapter.getItem(position);
                loadVideoDetail(cardData);
            }
        });
        mRecommendTitle = (TextView) findViewById(R.id.recommend_title);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        noNetworkLayout = (LinearLayout) findViewById(R.id.no_network_layout);
        tvNoNetwork = (TextView) findViewById(R.id.tv_no_network);
        tvNoNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadInfo(videoId);
            }
        });
    }

    private void initPlayer() {
        mVideoPlayView.setImage(imageUrl);
        mVideoPlayView.setVideoUri(videoUrl);
    }

    @Override
    public void upDate(RecommendVideoEntry videoEntry) {
        noNetworkLayout.setVisibility(View.GONE);
        mVideoPlayView.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        mAdapter.setNewData(videoEntry.getRecommendCards());
        if (videoEntry.getRecommendCards() != null
                && videoEntry.getRecommendCards().size() > 0) {
            mRecommendTitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNoNetwork() {
        noNetworkLayout.setVisibility(View.VISIBLE);
        mVideoPlayView.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void loadVideoDetail(RecommendVideoEntry.CardData cardData) {
        Intent intent = new Intent(this, RecommendVideoActivity.class);
        intent.putExtra(RecommendVideoActivity.TITLE, cardData.getTitle());
        intent.putExtra(RecommendVideoActivity.VIDEO_URL, cardData.getUrl());
        intent.putExtra(RecommendVideoActivity.IMAGE_URL, cardData.getPictureUrl());
        intent.putExtra(RecommendVideoActivity.VIDEO_ID, "" + cardData.getId());
        startActivityForResult(intent, REQUEST_NEW_PAGE);
    }

    private void checkPhoneStatePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                        PermissionUtils.CODE_READ_PHONE_STATE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NEW_PAGE && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            TitlebarUtils.initTitleBar(this, R.string.empty_null);
            mPhiTitleBar.setTitle(title);
        }
    }

    private void registerPhoneStateListener() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        registerReceiver(mPhoneStateReceiver, intentFilter);
    }

    @Override
    public void onBackPressed() {
        if (mVideoPlayView.backPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mVideoPlayView.resume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerPhoneStateListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVideoPlayView.suspend();
        unregisterReceiver(mPhoneStateReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVideoPlayView.release();
    }
}
