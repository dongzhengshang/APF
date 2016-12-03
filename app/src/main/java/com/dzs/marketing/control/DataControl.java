package com.dzs.marketing.control;

import com.dzs.projectframe.control.ProjectControl;

/**
 * 网络数据中心
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/9/14.
 */
public class DataControl extends ProjectControl {
    public static DataControl dataControl;

    private DataControl() {
    }

    /**
     * 单利模式
     *
     * @return datacontrol
     */
    public static DataControl getInstance() {
        if (dataControl == null) {
            dataControl = new DataControl();
        }
        return dataControl;
    }


}
