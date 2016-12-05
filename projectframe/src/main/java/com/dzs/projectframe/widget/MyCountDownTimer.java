package com.dzs.projectframe.widget;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * 自定义CountDownTimer类，用于验证码倒计时
 *
 * @author DZS dzsdevelop@163.com
 * @date 2015-7-2 上午10:21:11
 */
public class MyCountDownTimer extends CountDownTimer {
    private TextView button;

    public MyCountDownTimer(long millisInFuture, long countDownInterval, TextView button) {
        this(millisInFuture, countDownInterval);
        this.button = button;
    }

    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        button.setEnabled(false);
        String content = String.valueOf(millisUntilFinished / 1000);
        button.setText(content);
    }

    @Override
    public void onFinish() {
        button.setEnabled(true);
        button.setText("获取验证码");
    }

}
