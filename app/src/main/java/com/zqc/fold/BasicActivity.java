package com.zqc.fold;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class BasicActivity extends Activity implements View.OnClickListener {

    private ImageView topIv;
    private ImageView bottomIv;
    private TouchFoldLayout foldView;
    private int time = 200;
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
                translateAnimation.setDuration(1300);
                translateAnimation.setFillAfter(true);
                translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        foldView.setVisibility(View.INVISIBLE);//foldView需要占一个位置，不然topIv会扩充到整个View
                        int left = topIv.getLeft();
                        int top = (int) (topIv.getTop() + DIS);
                        int width = topIv.getWidth();
                        int height = topIv.getHeight();
                        topIv.clearAnimation();
                        topIv.layout(left, top, left + width, top + height);
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
                AnimationSet set = new AnimationSet(true);
                AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                alphaAnimation.setDuration(time);
                set.addAnimation(alphaAnimation);
                TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -10 * density);
                translateAnimation.setDuration(time);
                set.addAnimation(translateAnimation);
                set.setAnimationListener(new Animation.AnimationListener() {
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
                topIv.startAnimation(set);
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
