package com.example.wenjie.mediaplayerdm.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by wen.jie on 2017/6/15.
 */

public class MyApp extends Application {
    public static Context context;
    public static Handler mainHandler;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mainHandler = new Handler();
    }
}
