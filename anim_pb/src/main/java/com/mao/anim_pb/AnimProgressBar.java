package com.mao.anim_pb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * 定制进度动画的ProgressBar
 * Created by mpg on 2015/11/1 .
 */
public class AnimProgressBar extends ProgressBar{

    private Paint mPaint;
    private int mWidth;
    /**
     * 要绘制的bitmap
     */
    private Bitmap mBitmap;

    private Bitmap mBitmapBuffer1;
    private Bitmap mBitmapBuffer2;
    /**
     * 执行动画Runnable
     */
    private FirstRunnable mFirstRunnable;
    private SecondRunnable mSecondRunnable;
    /**
     * 第二帧动画位移
     */
    private int mTranslate = 0;

    public AnimProgressBar(Context context) {
        this(context, null);
        init();
    }

    public AnimProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setHorizontalScrollBarEnabled(true);
        this.setMax(100);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.setProgressDrawable(getResources().getDrawable(R.drawable.anim_progress_bg));
        this.setBackgroundResource(R.drawable.anim_progressbar_bg);
        mFirstRunnable = new FirstRunnable();
        mSecondRunnable = new SecondRunnable();

        mBitmapBuffer1 = ((BitmapDrawable) getResources().getDrawable(R.drawable.pb_anim_pic1)).getBitmap();
        mBitmapBuffer2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.pb_anim_pic2)).getBitmap();
        setPadding(getPaddingLeft(), mBitmapBuffer1.getHeight() / 2, getPaddingRight(), mBitmapBuffer1.getHeight() / 2);
    }

    /**
     * @param index
     */
    private void setDrawable(int index) {
        if (index == 1) {
            mTranslate = 0;
            mBitmap = mBitmapBuffer1;
        } else {
            mTranslate = Util.dip2px(getContext(), 4);
            mBitmap = mBitmapBuffer2;
        }
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap == null) {
            return;
        }
        float radio = getProgress() * 1.0f / getMax();
        float progressPos = mWidth * radio;
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);
        float x = progressPos + mTranslate - mBitmap.getWidth() / 2;
        // 不做小于0判断
        if (x > mWidth - mBitmap.getWidth() / 2) {
            x = mWidth - mBitmap.getWidth() / 2;
        }
        canvas.drawBitmap(mBitmap, x, -mBitmap.getHeight() / 2, mPaint);
        canvas.restore();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 默认pic1
        setDrawable(1);
        postDelayed(mFirstRunnable, 300);
    }

    /**
     * 更新动画
     * @param index 第index drawable
     */
    private void updateAnim(int index) {
        setDrawable(index);
        invalidate();
        postDelayed(index == 1 ? mFirstRunnable : mSecondRunnable, 300);
    }

    public void setBitmapBuffer1(Bitmap bitmap) {
        mBitmapBuffer1 = bitmap;
    }

    public void setBitmapBuffer2(Bitmap bitmap) {
        this.mBitmapBuffer2 = bitmap;
    }

    class FirstRunnable implements Runnable {

        @Override
        public void run() {
            updateAnim(2);
        }
    }

    class SecondRunnable implements Runnable {

        @Override
        public void run() {
            updateAnim(1);
        }
    }
}
