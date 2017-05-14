package com.zqc.fold;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnClickListener {

    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private ImageView iv4;
    private float density;
    private int height;
    private View rootview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        density = dm.density;
        height = dm.heightPixels;
        setContentView(R.layout.activity_main);
        iv1 = (ImageView) findViewById(R.id.imageview1);
        iv2 = (ImageView) findViewById(R.id.imageview2);
        iv3 = (ImageView) findViewById(R.id.imageview3);
        iv4 = (ImageView) findViewById(R.id.imageview4);
        Bitmap root = BitmapFactory.decodeResource(getResources(), R.mipmap.ad);
        Bitmap bitmap1 = Bitmap.createBitmap(root, 0, 0, root.getWidth(), root.getHeight()/4);
        Bitmap bitmap2 = Bitmap.createBitmap(root, 0, 1*root.getHeight()/4, root.getWidth(), root.getHeight()/4);
        Bitmap bitmap3 = Bitmap.createBitmap(root, 0, 2*root.getHeight()/4, root.getWidth(), root.getHeight()/4);
        Bitmap bitmap4 = Bitmap.createBitmap(root, 0, 3*root.getHeight()/4, root.getWidth(), root.getHeight()/4);
        iv1.setImageBitmap(bitmap1);
        iv2.setImageBitmap(bitmap2);
        iv3.setImageBitmap(bitmap3);
        iv4.setImageBitmap(bitmap4);
        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
        iv4.setOnClickListener(this);
        rootview = findViewById(R.id.rootview);
        findViewById(R.id.activity_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootview.setVisibility(View.VISIBLE);
            }
        });

    }

    final int time = 1000;
    
    @Override
    public void onClick(View v) {
        TranslateAnimation t1 = new TranslateAnimation(0,0,0, -10*density);
        t1.setDuration(time);
        iv1.startAnimation(t1);
        AnimationSet set2 = new AnimationSet(true);
        Rotate3dAnimation rotate3dAnimationX2 = new Rotate3dAnimation(0, -90, iv2.getWidth()/2, iv2.getHeight()/2, 0, Rotate3dAnimation.ROTATE_X_AXIS, false);
        rotate3dAnimationX2.setDuration(time);
        TranslateAnimation t2 = new TranslateAnimation(0,0, 0, -height/8/*-10 * density*/);
        t2.setDuration(time);
        set2.addAnimation(rotate3dAnimationX2);
        set2.addAnimation(t2);
        iv2.startAnimation(set2);
        AnimationSet set3 = new AnimationSet(true);
        Rotate3dAnimation rotate3dAnimationX3 = new Rotate3dAnimation(0, 90, iv3.getWidth()/2,iv3.getHeight()/2, 0, Rotate3dAnimation.ROTATE_X_AXIS, false);
        rotate3dAnimationX3.setDuration(time);
        TranslateAnimation t3 = new TranslateAnimation(0,0, 0, -height*3/8/*-10 * density*/);
        t3.setDuration(time);
        set3.addAnimation(rotate3dAnimationX3);
        set3.addAnimation(t3);
        iv3.startAnimation(set3);
        AnimationSet set4 = new AnimationSet(true);
        Rotate3dAnimation rotate3dAnimationX4 = new Rotate3dAnimation(0, -90, iv4.getWidth()/2, iv4.getHeight()/2, 0, Rotate3dAnimation.ROTATE_X_AXIS, false);
        rotate3dAnimationX4.setDuration(time);
        TranslateAnimation t4 = new TranslateAnimation(0,0, 0, -5*height/8/*-10 * density*/);
        t4.setDuration(time);
        set4.addAnimation(rotate3dAnimationX4);
        set4.addAnimation(t4);
        iv4.startAnimation(set4);

        set2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rootview.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
