package com.example.wenjie.mediaplayerdm.ui.widget.drop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wen.jie on 2018/1/4.
 * 作者：前世小书童
 * 链接：https://www.jianshu.com/p/626dbd93207d
 * 來源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */

/**
 * 二. PorterDuffXfermode方式
 <p>
 首先得了解下什么是PorterDuffXfermode，PorterDuffXfermode是Xfermode的子类，
 Xfermode又称为图像混合模式，除了PorterDuffXfermode之外还有其他几个子类分别为AvoidXfermode，PixelXorXfermode，

 不做详细的介绍，会在文章后面贴上详细学习的文章，我们要知道的是我们需要用到的，
 我们通过设置PorterDuffXfermode属性PorterDuff.Mode.SRC_IN来实现需要的效果，
 那么这个PorterDuff.Mode.SRC_IN是什么意思呢？他的意思是源图像与目标图像相交地方绘制源图像，
 所以只需要我们把源图像设置为圆角矩形，目标图像绘制源图像，那么经过PorterDuffXfermode的效果就可以达到圆角矩形的效果

 这个API因为不支持硬件加速在API 16已经过时了，如果想在高于API 16的机子上测试这玩意，必须现在应用或手机设置中关闭硬件加速
 */
//可用
@SuppressLint("AppCompatCustomView")
public class CornerImageView2 extends ImageView {

    private Paint mPaint;
    private Xfermode mXfermode;
    private int mBorderRadius = 50;

    public CornerImageView2(Context context) {
        this(context, null);
    }

    public CornerImageView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerImageView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (getDrawable() == null) {
            return;
        }

        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        //画源图像，为一个圆角矩形
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), mBorderRadius, mBorderRadius,
                mPaint);
        //设置混合模式
        mPaint.setXfermode(mXfermode);
        //画目标图像
        canvas.drawBitmap(drawableToBitamp(exChangeSize(getDrawable())), 0, 0, mPaint);
        // 还原混合模式
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);

    }

    /**
     * 图片拉升
     *
     * @param drawable
     * @return
     */
    private Drawable exChangeSize(Drawable drawable) {
        float scale = 1.0f;
        scale = Math.max(getWidth() * 1.0f / drawable.getIntrinsicWidth(), getHeight()
                * 1.0f / drawable.getIntrinsicHeight());
        drawable.setBounds(0, 0, (int) (scale * drawable.getIntrinsicWidth()),
                (int) (scale * drawable.getIntrinsicHeight()));
        return drawable;
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



