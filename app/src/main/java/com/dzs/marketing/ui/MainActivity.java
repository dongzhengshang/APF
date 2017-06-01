package com.dzs.marketing.ui;

import android.view.View;

import com.dzs.marketing.R;
import com.dzs.marketing.ui.widget.TimeAxisView;
import com.dzs.marketing.ui.widget.TimeScaleView;
import com.dzs.projectframe.base.Bean.LibEntity;
import com.dzs.projectframe.base.ProjectActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2017/5/26
 */
public class MainActivity extends ProjectActivity implements TimeScaleView.OnScrollListener {

    @Override
    protected int setContentById() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        TimeAxisView timeAxisView = (TimeAxisView) findViewById(R.id.scaleview2);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDateReturn(LibEntity libEntity) {

    }

    @Override
    public void onScroll(int hour, int min, int sec) {

    }

    @Override
    public void onScrollFinish(int hour, int min, int sec) {

    }
}
