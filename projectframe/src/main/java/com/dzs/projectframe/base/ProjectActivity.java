package com.dzs.projectframe.base;

import android.app.Activity;
import android.view.WindowManager;

/**
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/8/19.
 */
public class ProjectActivity extends Activity {
    
    /**
     * 设置全屏
     */
    public void setFullScream() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
