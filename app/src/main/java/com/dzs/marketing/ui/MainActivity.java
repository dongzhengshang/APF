package com.dzs.marketing.ui;

import android.view.View;

import com.dzs.marketing.R;
import com.dzs.marketing.ui.TimeScale.Scale;
import com.dzs.marketing.ui.TimeScale.TimeScaleView;
import com.dzs.projectframe.bean.NetEntity;
import com.dzs.projectframe.base.ProjectActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2017/5/26
 */
public class MainActivity extends ProjectActivity {
    private Scale scale;

    @Override
    protected int setContentById() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        scale = ((TimeScaleView) findViewById(R.id.scal)).getScale();
        List<Scale.TimePart> list = new ArrayList<>();
        Scale.TimePart timePart1 = new Scale.TimePart(0, 0, 2, 18);
        Scale.TimePart timePart4 = new Scale.TimePart(23, 40, 24, 0);
        list.add(timePart1);
        list.add(timePart4);
        scale.setRect(list);
        findViewById(R.id.Start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TimeScaleView) findViewById(R.id.scal)).showAnim();
            }
        });
        findViewById(R.id.End).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TimeScaleView) findViewById(R.id.scal)).closeAnim();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDateReturn(NetEntity netEntity) {

    }
}
