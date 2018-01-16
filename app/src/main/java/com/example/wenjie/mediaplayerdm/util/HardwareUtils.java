package com.example.wenjie.mediaplayerdm.util;


import android.app.Activity;
import android.view.WindowManager;

public class HardwareUtils {

    /**
     * Activity级 开启硬件加速
     * 应用和Activity是可以选择的，Window只能打开，View只能关闭。
     */
    public static void open(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

}
