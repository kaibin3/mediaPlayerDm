package com.example.wenjie.mediaplayerdm.util;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.wenjie.mediaplayerdm.base.MyApp;

/**
 * Created by wen.jie on 2018/1/10.
 */

public class WindowUtils {

    /**
     * 全屏
     * 在onCreate()方法中的super()和setContentView()两个方法之间加入
     */
    public static void requestFullScreen(Activity activity) {
        //去除title
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        //隐藏actionBar
        hideActionaBar(activity);

    }

    /**
     * 全屏
     * 在onCreate()方法中的super()和setContentView()两个方法之间加入
     */
    public static void requestFullScreenNoNavigation(Activity activity) {
        //去除title
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        //隐藏actionBar
        hideActionaBar(activity);
        //去掉虚拟按键全屏显示
        hideBottomUIMenu(activity);

    }

    /**
     * 隐藏虚拟按键，并且全屏 (可用)
     */
    public static void hideBottomUIMenu(Activity activity) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    private static void hideActionaBar(Activity activity) {
        //隐藏actionBar
        if (activity instanceof AppCompatActivity) {
            android.support.v7.app.ActionBar supportActionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.hide();
            }
        } else {
            ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }

    /**
     * 是否是横屏
     */
    public static boolean isOrientationLand(Activity activity) {
        return (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 横屏
     */
    public static void setOrientationLand(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 竖屏
     */
    public static void setOrientationPort(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    public static int screenWidth() {
        return MyApp.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int screenHeight() {
        return MyApp.getContext().getResources().getDisplayMetrics().heightPixels;
    }

    public static void changeScreenOrientation(Activity activity) {
        if (!isOrientationLand(activity)) {
            setOrientationLand(activity);
        } else {
            setOrientationPort(activity);
        }
    }

}
