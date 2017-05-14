package com.zqc.fold;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * @author zhy http://blog.csdn.net/lmj623565791/
 */
public class SimpleUseActivity extends Activity {

    private PolyToPolyView poly;
    private ImageView imv;
    private Bitmap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple);
        poly = (PolyToPolyView) findViewById(R.id.poly);
        imv = (ImageView) findViewById(R.id.top);
        map = BitmapFactory.decodeResource(getResources(), R.mipmap.ad);
        imv.setImageBitmap(Bitmap.createBitmap(map,0,0,map.getWidth(),map.getHeight()/4));

    }



}
