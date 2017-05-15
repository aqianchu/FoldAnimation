package com.zqc.fold;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static com.zqc.fold.TouchFoldLayout.BETWEEN;

public class BasicActivity extends Activity implements View.OnClickListener {

    private ImageView topIv;
    private ImageView bottomIv;
    private TouchFoldLayout foldView;
    private int time = 240;
    private int first_time = 1000;
    private float density;
    private LinearLayout rootView;
    private static float DIS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
//        //透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_basic);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        rootView = (LinearLayout) findViewById(R.id.root);
        density = dm.density;
        DIS = -10 * density;
        first_time = TouchFoldLayout.LEN*BETWEEN + 100 + 20;//factors.lenght*mHandler.sendMessageDelayed(20) + mHandler.postDelayed(100) + 20(yan chi)
        topIv = (ImageView) findViewById(R.id.top_img);
        bottomIv = (ImageView) findViewById(R.id.bottom_img);

        Bitmap root = BitmapFactory.decodeResource(getResources(), R.mipmap.ad);
        Bitmap topBt = Bitmap.createBitmap(root, 0, 0, root.getWidth(), root.getHeight() / 4);
        Bitmap bottomBt = Bitmap.createBitmap(root, 0, root.getHeight() / 4, root.getWidth(), 3 * root.getHeight() / 4);
        topIv.setImageBitmap(topBt);
        bottomIv.setImageBitmap(bottomBt);
        foldView = (TouchFoldLayout) findViewById(R.id.id_fold_layout);
        foldView.setListener(new TouchFoldLayout.FoldFinishListener() {
            @Override
            public void onStart() {
                TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, DIS);
                translateAnimation.setDuration(700);//1300=factors.lenght*mHandler.sendMessageDelayed(20) + mHandler.postDelayed(100)
                translateAnimation.setFillAfter(true);
                translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        int left = topIv.getLeft();
                        int top = (int) (topIv.getTop() + DIS);
                        int width = topIv.getWidth();
                        int height = topIv.getHeight();
                        topIv.clearAnimation();
                        topIv.layout(left, top, left + width, top + height);

                        if (foldView.getVisibility() != View.VISIBLE) return;
                        int left1 = foldView.getLeft();
                        int top1 = (int) (foldView.getTop() + DIS);
                        int width1 = foldView.getWidth();
                        int height1 = foldView.getHeight();
                        foldView.layout(left1, top1, left1 + width1, top1 + height1);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                topIv.startAnimation(translateAnimation);
                foldView.startAnimation(translateAnimation);
            }

            @Override
            public void onEnd() {
                foldView.setVisibility(View.INVISIBLE);
                rootView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

//                        AnimationSet set = new AnimationSet(true);
                        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                        alphaAnimation.setDuration(time);
                        alphaAnimation.setInterpolator(new DecelerateInterpolator(0.707f));//sqrt(2)/2
//                        set.addAnimation(alphaAnimation);
//                        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -10 * density);
//                        translateAnimation.setDuration(time);
//                        set.addAnimation(translateAnimation);
                        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                topIv.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        topIv.startAnimation(alphaAnimation);
                    }
                }, 160);
            }
        });
        findViewById(R.id.root).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        foldView.setVisibility(View.VISIBLE);
        rootView.removeView(foldView);
        topIv.setVisibility(View.VISIBLE);
        rootView.addView(foldView);
    }
}
