package com.example.wenjie.mediaplayerdm.onLineViedio;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.example.wenjie.mediaplayerdm.R;
import com.example.wenjie.mediaplayerdm.util.WindowUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnLinePlayActivity extends AppCompatActivity {
    private static final String TAG = "OnLinePlayActivity";
    private static final String FRAGMENT_TAG = "fragment_tag";

    OnLinePlayFragment onLinePlayFragment = new OnLinePlayFragment();
    VideoFragment videoFragment = new VideoFragment();

    @BindView(R.id.ConstraintLayout)
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        WindowUtils.requestFullScreen(this);
        setContentView(R.layout.activity_on_line_play);

        addFragment(onLinePlayFragment);
        // addFragment(new VideoFragment());

        ButterKnife.bind(this);
    }


    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.play_fragment, fragment, FRAGMENT_TAG);
        }
        transaction.commit();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && WindowUtils.isOrientationLand(this)) {
            return onLinePlayFragment.onKeyBack();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged: " + newConfig.orientation);
        if (Configuration.ORIENTATION_LANDSCAPE == newConfig.orientation) {
            setContentView(R.layout.activity_on_line_play);
            ButterKnife.bind(this);
        } else {
            setContentView(R.layout.activity_on_line_play);
            ButterKnife.bind(this); //切换屏幕后需要再ButterKnife.bind
        }
    }

    @OnClick(R.id.ConstraintLayout)
    void change() {
        //WindowUtils.changeScreenOrientation(OnLinePlayActivity.this);
    }

}
