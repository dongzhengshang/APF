package com.dzs.marketing.ui;

import android.os.Handler;

import com.dzs.marketing.R;
import com.dzs.marketing.app.AppContext;
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
    private int time = 3;

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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewUtils.setText(R.id.TV_Splash_Jump, AppContext.resources.getString(R.string.splash_jump, time));
                time--;
                if (time < 0) {
                    SplashActivity.this.Intent(MainActivity.class, true);
                } else {
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }
}
