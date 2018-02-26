package com.example.wenjie.mediaplayerdm.ui.widget.drop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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
 * 首先简单了解下BitmapShader，BitmapShader是Shader的子类，Shader在三维软件中我们称之为着色器，所以通俗的理解，
 * Shader的作用是给图像着色或者上色，BitmapShader允许我们载入一张图片来给图像着色,具体不做过多的解释，
 * 结尾贴出关于Shader的具体使用的文章
 * <p>
 * 所以其实根据上面对于BitmapShader的描述，其实就可以对圆角ImageView有一定的思路了吧，
 * 画一个圆角矩形，然后把本来画上去的图像着色到圆角矩形上，这样就实现了圆角的ImageView
 */
//可用
@SuppressLint("AppCompatCustomView")
public class RoundCornerImageView1 extends ImageView {


    //圆角大小，默认为10
    private int mBorderRadius = 50;

    private Paint mPaint;

    // 3x3 矩阵，主要用于缩小放大
    private Matrix mMatrix;

    //渲染图像，使用图像为绘制图形着色
    private BitmapShader mBitmapShader;

    public RoundCornerImageView1(Context context) {
        this(context, null);
    }

    public RoundCornerImageView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerImageView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mMatrix = new Matrix();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        Bitmap bitmap = drawableToBitamp(getDrawable());
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (!(bitmap.getWidth() == getWidth() && bitmap.getHeight() == getHeight())) {
            // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
            scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(),
                    getHeight() * 1.0f / bitmap.getHeight());
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale(scale, scale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        // 设置shader
        mPaint.setShader(mBitmapShader);
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), mBorderRadius, mBorderRadius, mPaint);
    }


    private Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        // 当设置不为图片，为颜色时，获取的drawable宽高会有问题，所有当为颜色时候获取控件的宽高
        int w = drawable.getIntrinsicWidth() <= 0 ? getWidth() : drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight() <= 0 ? getHeight() : drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
}

