package com.example.wenjie.mediaplayerdm.ui.widget.drop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by wen.jie on 2018/1/4.
 * <p>
 * 作者：前世小书童
 * 链接：https://www.jianshu.com/p/626dbd93207d
 * 來源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */

/**
 * ClipPath方式
 * ClipPath是Canvas提供对画布裁剪的方法之一，除了ClipPath还有clipRect方法，
 * 画布裁剪后后面的Canvas操作，都会在对裁剪后的画布进行操作
 */
//不稳定 有锯齿
@SuppressLint("AppCompatCustomView")
public class RoundCornerImageView3 extends ImageView {

    float width, height;
    private int cornerRadius = 100;

    public RoundCornerImageView3(Context context) {
        this(context, null);
    }

    public RoundCornerImageView3(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerImageView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭单个view的硬件加速
        }
        /*
        View级别 您可以在运行时用以下的代码关闭单个view的硬件加速：
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null); 不能在view级别开启硬件加速
        **/

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
            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }
}

