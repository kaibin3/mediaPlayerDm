package com.example.wenjie.mediaplayerdm.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.wenjie.mediaplayerdm.base.MyApp;

import com.phicomm.widgets.PhiToast;

public class ToastUtils {
    private static PhiToast mToast = null;

    /**
     * 判断toast是否存在，如果存在则更新text，达到避免出现时间叠加的问题
     * @param context 上下文
     * @param text  显示的内容
     * @param duration  显示时长，默认值为Toast.LENGTH_SHORT (2秒)或Toast.LENGTH_LONG(3.5秒)
     */
    public static void toastShow(Context context, String text, int duration) {
        if (mToast == null) {
            mToast = PhiToast.makeText(MyApp.getContext(), text, duration);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    /**
     * 判断toast是否存在，如果存在则更新text，达到避免出现时间叠加的问题
     * @param resId  字符串资源文件ID
     */
    public static void toastShow(int resId)
            throws Resources.NotFoundException {
        if (mToast == null) {
            mToast = PhiToast.makeText(MyApp.getContext(), MyApp.getContext().getResources().getString(resId), Toast.LENGTH_SHORT);
        } else {
            mToast.setText(MyApp.getContext().getResources().getText(resId));
        }
        mToast.show();
    }

    public static void toastShow(@NonNull String str){
        if (mToast == null) {
            mToast = PhiToast.makeText(MyApp.getContext(), str, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(str);
        }
        mToast.show();
    }
}