package com.zqc.fold;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author zhy http://blog.csdn.net/lmj623565791/
 */
public class FoldLayout extends ViewGroup
{

	private static final int NUM_OF_POINT = 8;
	/**
	 * 图片的折叠后的总宽度
	 */
	private float mTranslateDis;

	protected float mFactor = 1f;

	private int mNumOfFolds = 3;

	private Matrix[] mMatrices = new Matrix[mNumOfFolds];

	private Paint mSolidPaint;

	private Paint mShadowPaint;
	private Matrix mShadowGradientMatrix;
	private LinearGradient mShadowGradientShader;

	private float mFlodWidth;
	private float mTranslateDisPerFlod;

	private float mAnchor = 0;
	private int mFlodHeight;

	public FoldLayout(Context context)
	{
		this(context, null);
	}

	public FoldLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		for (int i = 0; i < mNumOfFolds; i++)
		{
			mMatrices[i] = new Matrix();
		}

		mSolidPaint = new Paint();
		mShadowPaint = new Paint();
		mShadowPaint.setStyle(Style.FILL);
		mShadowGradientShader = new LinearGradient(0, 0, 0.5f, 0, Color.BLACK,
				Color.TRANSPARENT, TileMode.CLAMP);
		mShadowPaint.setShader(mShadowGradientShader);
		mShadowGradientMatrix = new Matrix();
		this.setWillNotDraw(false);

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
				Config.ARGB_8888);
		mCanvas.setBitmap(mBitmap);
		updateFold();

	}

	private void updateFold()
	{
		int w = getMeasuredWidth();
		int h = getMeasuredHeight();

		//折叠后的总宽度
//		mTranslateDis = (int) (mBitmap.getWidth() * mFactor);
		//原图每块的宽度
//		mFlodWidth = mBitmap.getWidth() / mNumOfFolds;
		//折叠时，每块的宽度
//		mTranslateDisPerFlod = mTranslateDis / mNumOfFolds;

		mTranslateDis = w * mFactor;
		mFlodWidth = w / mNumOfFolds;
		mFlodHeight = mBitmap.getHeight() / mNumOfFolds;
		mTranslateDisPerFlod = mTranslateDis / mNumOfFolds;

//		int alpha = (int) (255 * (1 - mFactor));
//		mSolidPaint.setColor(Color.argb((int) (alpha * 0.8F), 0, 0, 0));
//
//		mShadowGradientMatrix.setScale(mFlodWidth, 1);
//		mShadowGradientShader.setLocalMatrix(mShadowGradientMatrix);
//		mShadowPaint.setAlpha(alpha);

		//纵轴减小的那个高度，用勾股定理计算下
		int depth = (int) Math.sqrt(mFlodWidth * mFlodWidth
				- mTranslateDisPerFlod * mTranslateDisPerFlod) / 2;
		float anchorPoint = mAnchor * h;
		float midFold = (anchorPoint / mFlodHeight);

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

			dst[1] = (anchorPoint > i * mFlodHeight) ?
					anchorPoint + (i - midFold) * mTranslateDisPerFlod :
					anchorPoint - (midFold -i) * mTranslateDisPerFlod;

			dst[3] = dst[1];
			dst[4] = isEven ? mBitmap.getWidth() - depth : mBitmap.getWidth();
			dst[5] = (i + 1) * mFlodHeight;

			dst[5] = (anchorPoint > (i+1)*mFlodHeight) ?
					anchorPoint + (i +1 -midFold)*mTranslateDisPerFlod :
					anchorPoint - (midFold -i -1)* mTranslateDisPerFlod;

			dst[6] = isEven ? depth : 0;
			dst[7] = dst[5];

			//setPolyToPoly
			mMatrices[i].setPolyToPoly(src, 0, dst, 0, src.length >> 1);
		}
	}

	private Canvas mCanvas = new Canvas();
	private Bitmap mBitmap;
	private boolean isReady;

	@Override
	protected void dispatchDraw(Canvas canvas)
	{

		if (mFactor == 0)
			return;
		if (mFactor == 1)
		{
			super.dispatchDraw(canvas);
			return;
		}
		for (int i = 0; i < mNumOfFolds; i++)
		{
			canvas.save();

			canvas.concat(mMatrices[i]);
//			canvas.clipRect(mFlodWidth * i, 0, mFlodWidth * i + mFlodWidth,
//					getHeight());
			canvas.clipRect(0, i * mFlodHeight, mBitmap.getWidth(),
					(i + 1) * mFlodHeight);
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
			canvas.translate(0, i * mFlodHeight);
//			canvas.translate(mFlodWidth * i, 0);
//			if (i % 2 == 0)
//			{
//				canvas.drawRect(0, 0, mFlodWidth, getHeight(), mSolidPaint);
//			} else
//			{
//				canvas.drawRect(0, 0, mFlodWidth, getHeight(), mShadowPaint);
//			}
			canvas.restore();
		}
	}

	public void setFactor(float factor)
	{
		this.mFactor = factor;
		updateFold();
		invalidate();
	}

	public float getFactor()
	{
		return mFactor;
	}

	public void setAnchor(float anchor)
	{
		this.mAnchor = anchor;
		updateFold();
		invalidate();
	}

}
