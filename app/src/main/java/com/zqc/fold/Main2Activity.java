package com.zqc.fold;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class Main2Activity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.main1).setOnClickListener(this);
        findViewById(R.id.main2).setOnClickListener(this);
        findViewById(R.id.main3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main1:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.main2:
                startActivity(new Intent(this, SimpleUseActivity.class));
                break;
            case R.id.main3:
                startActivity(new Intent(this, BasicActivity.class));
                break;
        }
    }
}
