package com.example.wenjie.mediaplayerdm.ui.widget.drop;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.example.wenjie.mediaplayerdm.R;


/**
 * http://blog.csdn.net/huangmayi/article/details/72670105
 */
public class CircleImageView extends android.support.v7.widget.AppCompatImageView {

    private static final int MIN_WIDTH = 30;
    private static final int MIN_HEIGHT = 30;

    private boolean isCircle;
    private float radius;
    private int centerX;
    private int centerY;

    private float strokeWidth;
    private int strokeColor;

    private Paint mStrockPaint;
    private Paint mBitmapPaint;
    private BitmapShader bitmapShader;
    private Bitmap bitmap;

    public CircleImageView(Context context) {
        super(context);
        init(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        int bitmapW = bitmap.getWidth();
        int bitmapH = bitmap.getHeight();

        if (wMode == MeasureSpec.AT_MOST) {
            wSize = Math.min(wSize, Math.max(bitmapW, MIN_WIDTH));
        }
        if (hMode == MeasureSpec.AT_MOST) {
            hSize = Math.min(hSize, Math.max(bitmapH, MIN_HEIGHT));
        }

        if (isCircle) {
            centerX = wSize / 2;
            centerY = hSize / 2;
            radius = wSize > hSize ? hSize / 2 : wSize / 2;
        }

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(wSize, wMode),
                MeasureSpec.makeMeasureSpec(hSize, hMode));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setUpBitmapShader();
        if (isCircle) {
            canvas.drawCircle(centerX, centerY, radius - strokeWidth, mBitmapPaint);
            canvas.drawCircle(centerX, centerY, radius - strokeWidth, mStrockPaint);
        } else {
            RectF rectF = new RectF(0 + strokeWidth, 0 + strokeWidth, getWidth() - strokeWidth, getHeight() - strokeWidth);
            canvas.drawRoundRect(rectF, radius, radius, mBitmapPaint);
            canvas.drawRoundRect(rectF, radius, radius, mStrockPaint);
        }
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray ta = context.getResources().obtainAttributes(attrs, R.styleable.CircleImageView);
        strokeWidth = ta.getDimension(R.styleable.CircleImageView_strokeWidth, 0);
        if (strokeWidth > 0) {
            strokeColor = ta.getColor(R.styleable.CircleImageView_strokeColor, 0x66000000);
        }
        Drawable drawable = ta.getDrawable(R.styleable.CircleImageView_image);
        bitmap = drawable2Bitmap(drawable);

        isCircle = ta.getBoolean(R.styleable.CircleImageView_isCircle, false);
        radius = ta.getDimension(R.styleable.CircleImageView_radius, 0);
        ta.recycle();

        mStrockPaint = new Paint();
        mStrockPaint.setAntiAlias(true);
        mStrockPaint.setStyle(Paint.Style.STROKE);
        mStrockPaint.setStrokeWidth(strokeWidth);
        mStrockPaint.setColor(strokeColor);

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
    }

    private Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable != null && drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 设置图片缩放比例
     */
    private void setUpBitmapShader() {
        float scale = 1.0f;
        Matrix m = new Matrix();
        m.postTranslate(getWidth() / 2 - bitmap.getWidth() / 2, getHeight() / 2 - bitmap.getHeight() / 2);
        if (isCircle) {
            int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
            scale = (2.0f * radius) / size;
        } else {
            scale = Math.max(1.0f * getWidth() / bitmap.getWidth(),
                    1.0f * getHeight() / bitmap.getHeight());
        }
        m.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        bitmapShader.setLocalMatrix(m);
        mBitmapPaint.setShader(bitmapShader);
    }

    public void setImage(Bitmap b) {
        if (b == null) {
            return;
        }
        bitmap = b;
        invalidate();
    }

    public void setImage(Drawable d) {
        if (d == null) {
            return;
        }

        bitmap = drawable2Bitmap(d);
        invalidate();
    }

}
