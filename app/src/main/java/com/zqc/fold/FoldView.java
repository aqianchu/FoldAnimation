package com.zqc.fold;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by zhangqianchu on 2017/5/11.
 */

public class FoldView {
    private final Context mContext;
    private Bitmap rootBitmap;
    private int widthPiex,heightPiex;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private Bitmap bitmap4;
    private LinearLayout rootView;
    private ImageView view1;
    private ImageView view2;
    private ImageView view3;
    private ImageView view4;

    public FoldView(Bitmap rootBitmap, Context mContext) {
        this.rootBitmap = rootBitmap;
        this.mContext = mContext;
        init();
    }

    private void init() {
        bitmap1 = Bitmap.createBitmap(rootBitmap, 0, 0, widthPiex, heightPiex/4);
        bitmap2 = Bitmap.createBitmap(rootBitmap, 0, heightPiex/4, widthPiex, heightPiex/4);
        bitmap3 = Bitmap.createBitmap(rootBitmap, 0, heightPiex/2, widthPiex, heightPiex/4);
        bitmap4 = Bitmap.createBitmap(rootBitmap, 0, 3*heightPiex/4, widthPiex, heightPiex/4);
        rootView = new LinearLayout(mContext);
        rootView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        rootView.setLayoutParams(params);

        view1 = new ImageView(mContext);
        view2 = new ImageView(mContext);
        view3 = new ImageView(mContext);
        view4 = new ImageView(mContext);
        view1.setImageBitmap(bitmap1);
        view2.setImageBitmap(bitmap2);
        view3.setImageBitmap(bitmap3);
        view4.setImageBitmap(bitmap4);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1.0f);
        view1.setLayoutParams(params);
        view2.setLayoutParams(params);
        view3.setLayoutParams(params);
        view4.setLayoutParams(params);
        rootView.addView(view1);
        rootView.addView(view2);
        rootView.addView(view3);
        rootView.addView(view4);
    }

    public void startAnimation() {
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation t1 = new TranslateAnimation(0, 0, 0, -200);
//        Rotate3dAnimation r1 = new Rotate3dAnimation(0, -180, 0, 0, 0f, Rotate3dAnimation.ROTATE_X_AXIS, true);
//        r1.setDuration(100);
//        set.addAnimation(t1);set.addAnimation(r1);


    }
}
