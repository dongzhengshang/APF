package com.dzs.projectframe.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.ViewUtils;
import android.view.View;
import android.view.WindowManager;

import com.dzs.projectframe.utils.AsyncTaskUtils;
import com.dzs.projectframe.utils.SharedPreferUtils;
import com.dzs.projectframe.utils.StackUtils;

/**
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/8/19.
 */
public abstract class ProjectActivity extends FragmentActivity implements View.OnClickListener, AsyncTaskUtils.OnNetReturnListener {
    protected ViewUtils viewUtils;
    protected Resources resources;
    protected SharedPreferUtils sharedPreferUtils;

    protected abstract int setContent();

    protected abstract void initView();

    protected abstract void initAnimation();

    protected abstract void initData();

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        StackUtils.getInstanse().addActivity(this);
        setTextAbout();

    }

    /**
     * 设置字体相关
     */
    private void setTextAbout() {
        Resources res = getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    /**
     * 设置全屏
     */
    public void setFullScream() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
