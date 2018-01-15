package com.example.wenjie.mediaplayerdm.PhiFind.mediaPlay;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;



public abstract class VideoPlayControl extends FrameLayout {

    public VideoPlayControl(@NonNull Context context) {
        super(context);
    }

    public VideoPlayControl(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void onPlayStart();
}
