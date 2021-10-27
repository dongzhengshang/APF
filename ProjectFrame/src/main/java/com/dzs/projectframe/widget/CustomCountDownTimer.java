package com.dzs.projectframe.widget;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.dzs.projectframe.R;
import com.dzs.projectframe.base.ProjectContext;

/**
 * 自定义CountDownTimer类，用于验证码倒计时
 *
 * @author DZS dzsk@outlook.com
 * @date 2015-7-2 上午10:21:11
 */
public class CustomCountDownTimer extends CountDownTimer {
    private TextView button;

    public CustomCountDownTimer(long millisInFuture, long countDownInterval, TextView button) {
        this(millisInFuture, countDownInterval);
        this.button = button;
    }

    public CustomCountDownTimer(long millisInFuture, long countDownInterval) {
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
        button.setText(ProjectContext.resources.getText(R.string.GetVerificationCode));
    }


}
