package com.dzs.marketing.ui;

import android.content.Intent;
import android.os.Handler;

import com.dzs.marketing.R;
import com.dzs.marketing.base.BaseActivity;


/**
 * 闪屏界面
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/9/12.
 */
public class SplashActivity extends BaseActivity {
    public Handler handler = new Handler();

    @Override
    protected int setContent() {
        setFullScream();
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //SplashActivity.this.Intent(MainActivity.class, true);
//            }
//        }, 3000);
    }
}
