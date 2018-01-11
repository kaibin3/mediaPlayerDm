package com.example.wenjie.mediaplayerdm.util;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wen.jie on 2018/1/11.
 */

public class ViewLog {
    private static final String TAG = "ViewLog";

    public static void widthHeight(View view) {

        ViewGroup.LayoutParams params = view.getLayoutParams();
        int width1 = params.width;////LayoutParams.width可以在onMesure方法中获取  如果是wrap_content也是不能获取的
        int height1 = params.height;
        Log.d(TAG, "LayoutParams  width: " + width1);
        Log.d(TAG, "LayoutParams  height: " + height1);

        int measuredWidth = view.getMeasuredWidth();//在onMeasure()执行完后才会有值
        int measuredHeight = view.getMeasuredHeight();
        Log.d(TAG, "measuredWidth: " + measuredWidth);
        Log.d(TAG, "measuredHeight: " + measuredHeight);

        int width2 = view.getWidth();//在layout执行完后才能获取宽度,在onMeasure()方法中拿不到
        int height2 = view.getHeight();
        Log.d(TAG, "getWidth: " + width2);
        Log.d(TAG, "getHeight: " + height2);

    }
}
