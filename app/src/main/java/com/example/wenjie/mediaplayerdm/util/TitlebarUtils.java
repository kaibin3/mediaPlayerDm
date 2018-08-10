package com.example.wenjie.mediaplayerdm.util;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;

import com.example.wenjie.mediaplayerdm.R;
import com.example.wenjie.mediaplayerdm.ui.widget.PhiTitleBar;


/**
 * Created by long.xiong on 2017/2/28.
 */

public class TitlebarUtils {
    public static void initTitleBar(final Activity activity, PhiTitleBar titleBar) {
        if (null != titleBar) {
            //titleBar.setTitle(strResid);
            titleBar.setTitleColor(Color.WHITE);
            titleBar.setTitleSize(TypedValue.COMPLEX_UNIT_PX,
                    49);

            setActivityImmersive(activity, titleBar);
        }
    }

    public static void initTitleBar(final Activity activity, PhiTitleBar titleBar, int strResid) {
        if (null != titleBar) {
            titleBar.setTitle(strResid);
            titleBar.setTitleColor(Color.WHITE);
            titleBar.setTitleSize(TypedValue.COMPLEX_UNIT_PX,
                    49);

            setActivityImmersive(activity, titleBar);
        }
    }

    public static void initTitleBar(final Activity activity, PhiTitleBar titleBar, String str) {
        if (null != titleBar) {
            titleBar.setTitle(str);
            titleBar.setTitleColor(Color.WHITE);
            titleBar.setTitleSize(TypedValue.COMPLEX_UNIT_PX,
                    49);

            setActivityImmersive(activity, titleBar);
        }
    }

    public static void initTitleBar(final Activity activity, int strResid) {
        PhiTitleBar phiTitleBar = (PhiTitleBar) activity.findViewById(R.id.phiTitleBar);
        if (null != phiTitleBar) {
            phiTitleBar.setTitle(strResid);
            phiTitleBar.setTitleColor(Color.WHITE);
            phiTitleBar.setTitleSize(TypedValue.COMPLEX_UNIT_PX,
                    49);
            phiTitleBar.setLeftImageResource(R.drawable.rewind);
            phiTitleBar.setLeftClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.finish();
                }
            });
            setActivityImmersive(activity, phiTitleBar);
        }
    }


    private static void setActivityImmersive(Activity activity, PhiTitleBar titleBar) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            titleBar.setImmersive(true);
        } else {
            titleBar.setImmersive(false);
        }
        titleBar.setAllCaps(true);
        ActionBar actionBar = activity.getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private static void setActivityImmersive(Activity activity, PhiTitleBar titleBar, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(color);
            titleBar.setImmersive(true);
        } else {
            titleBar.setImmersive(false);
        }
        titleBar.setAllCaps(true);
        ActionBar actionBar = activity.getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}
