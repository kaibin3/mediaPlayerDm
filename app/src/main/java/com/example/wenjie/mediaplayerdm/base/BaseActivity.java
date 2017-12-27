package com.example.wenjie.mediaplayerdm.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by wen.jie on 2017/3/15.
 */

public class BaseActivity extends Activity {


    public static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
     //   setTheme(R.style.AppTheme);//放在onCreate前
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();

        setTitle(getClass().getSimpleName());
        Log.d(TAG, "onCreate title : " + getClass().getSimpleName());
        Log.d(TAG, "onCreate mActionBar : " + getActionBar());
    }
}
