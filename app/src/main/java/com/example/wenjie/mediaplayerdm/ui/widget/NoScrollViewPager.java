package com.example.wenjie.mediaplayerdm.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class NoScrollViewPager extends ViewPager {

    private static final String TAG = "NoScrollViewPager";

    private boolean mScrollEnable = true;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setScrollEnable(boolean scrollEnable) {
        this.mScrollEnable = scrollEnable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (mScrollEnable) {
            //super:false
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (mScrollEnable) {
            //super:true
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }
    }


}