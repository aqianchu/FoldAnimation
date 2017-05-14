package com.zqc.fold;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhangqianchu on 2017/5/12.
 */

public class PolyToPolyView extends ViewGroup {

    private static final int NUM_OF_POINT = 8;
    private int mFlodHeight;
    /**
     * 图片的折叠后的总宽度
     */
    private int mTranslateDis;
    private int time[] = new int[30];
    /**
     * 折叠后的总宽度与原图宽度的比例
     */
    private float mFactor = 0.3f;
    /**
     * 折叠块的个数
     */
    private int mNumOfFolds = 3;

    private Matrix[] mMatrices = new Matrix[mNumOfFolds];

    private Bitmap mBitmap;

    /**
     * 绘制黑色透明区域
     */
    private Paint mSolidPaint;

    /**
     * 绘制阴影
     */
    private Matrix mShadowGradientMatrix;

    /***
     * 原图每块的宽度
     */
    private int mFlodWidth;
    /**
     * 折叠时，每块的宽度
     */
    private int mTranslateDisPerFlod;
    private boolean isReady;
    private Canvas mCanvas = new Canvas();

    public PolyToPolyView(Context context) {
        super(context);
        init();
    }

    public PolyToPolyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        Bitmap map = BitmapFactory.decodeResource(getResources(), R.mipmap.ad);
        mBitmap = Bitmap.createBitmap(map, 0, map.getHeight() / 4, map.getWidth(), 3 * map.getHeight() / 4);
        for (int i = 0; i < time.length; i++) {
            time[i] = i;
        }
        updateFold();
    }

    private void updateFold() {

        //折叠后的总宽度
        mTranslateDis = (int) (mBitmap.getWidth() * mFactor);
        //原图每块的宽度
        mFlodWidth = mBitmap.getWidth() / mNumOfFolds;
        mFlodHeight = mBitmap.getHeight() / mNumOfFolds;
        //折叠时，每块的宽度
        mTranslateDisPerFlod = mTranslateDis / mNumOfFolds;

        //初始化matrix
        for (int i = 0; i < mNumOfFolds; i++) {
            mMatrices[i] = new Matrix();
        }

        mSolidPaint = new Paint();
        int alpha = (int) (255 * mFactor * 0.8f);
        mSolidPaint
                .setColor(Color.argb((int) (alpha * 0.8F), 0, 0, 0));

        mShadowGradientMatrix = new Matrix();
        mShadowGradientMatrix.setScale(mFlodWidth, 1);
        //纵轴减小的那个高度，用勾股定理计算下
        int depth = (int) Math.sqrt(mFlodWidth * mFlodWidth
                - mTranslateDisPerFlod * mTranslateDisPerFlod) / 2;
        //转换点
        float[] src = new float[NUM_OF_POINT];
        float[] dst = new float[NUM_OF_POINT];

        /**
         * 原图的每一块，对应折叠后的每一块，方向为左上、右上、右下、左下，大家在纸上自己画下
         */
        for (int i = 0; i < mNumOfFolds; i++) {
            src[0] = 0;
            src[1] = i * mFlodHeight;
            src[2] = mBitmap.getWidth();
            src[3] = src[1];
            src[4] = src[2];
            src[5] = (i + 1) * mFlodHeight;
            src[6] = 0;
            src[7] = src[5];

            boolean isEven = i % 2 == 0;

            dst[0] = isEven ? 0 : depth;
            dst[1] = i * mFlodHeight;
            dst[2] = isEven ? mBitmap.getWidth() : mBitmap.getWidth() - depth;
            dst[3] = dst[1];
            dst[4] = isEven ? mBitmap.getWidth() - depth : mBitmap.getWidth();
            dst[5] = (i + 1) * mFlodHeight;
            dst[6] = isEven ? depth : 0;
            dst[7] = dst[5];

            //setPolyToPoly
            mMatrices[i].setPolyToPoly(src, 0, dst, 0, src.length >> 1);
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        View child = getChildAt(0);
        measureChild(child, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(child.getMeasuredWidth(),
                child.getMeasuredHeight());

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        View child = getChildAt(0);
        child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(mBitmap);
        updateFold();

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制mNumOfFolds次

        for (int i = 0; i < mNumOfFolds; i++) {
            canvas.save();
            //将matrix应用到canvas
            canvas.concat(mMatrices[i]);
            //控制显示的大小
//				canvas.clipRect(mFlodWidth * i, 0, mFlodWidth * i + mFlodWidth,
//						mBitmap.getHeight());
            canvas.clipRect(0, i * mFlodHeight, mBitmap.getWidth(),
                    (i + 1) * mFlodHeight);

            //绘制图片
            canvas.drawBitmap(mBitmap, 0, 0, null);
            if (isReady)
            {
                canvas.drawBitmap(mBitmap, 0, 0, null);
            } else
            {
                // super.dispatchDraw(canvas);
                super.dispatchDraw(mCanvas);
                canvas.drawBitmap(mBitmap, 0, 0, null);
                isReady = true;
            }
            canvas.translate(0, -mFlodHeight * i);
            //移动绘制阴影
//				canvas.translate(mFlodWidth * i, 0);
            canvas.restore();
        }
    }



}