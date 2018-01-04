package com.example.wenjie.mediaplayerdm.ui.widget.drop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by wen.jie on 2018/1/4.
 */

@SuppressLint("AppCompatCustomView")
public class CornerImageView extends ImageView {

    float width, height;
    private int cornerRadius = 32;

    public CornerImageView(Context context) {
        this(context, null);
    }

    public CornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {


        if (width > cornerRadius && height > cornerRadius) {
            Path path = new Path();
            path.moveTo(cornerRadius, 0);
            path.lineTo(width - cornerRadius, 0);
            path.quadTo(width, 0, width, cornerRadius);
            path.lineTo(width, height - cornerRadius);
            path.quadTo(width, height, width - cornerRadius, height);
            path.lineTo(cornerRadius, height);
            path.quadTo(0, height, 0, height - cornerRadius);
            path.lineTo(0, cornerRadius);
            path.quadTo(0, 0, cornerRadius, 0);


            canvas.clipPath(path, Region.Op.REPLACE);


            // canvas.clipPath(path);
        }

        super.onDraw(canvas);
    }
}

