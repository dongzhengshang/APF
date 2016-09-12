package com.dzs.projectframe.control;

import android.media.Image;

import com.dzs.projectframe.interf.OnDataReturnListener;
import com.dzs.projectframe.utils.AsyncTaskUtils;

import java.util.Map;

/**
 * 网络数据处理
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/9/12.
 */
public class DataCenter {
    protected void getString(String taskId, String url, Map<String, Object> params,
                             boolean saveCache, boolean reflsh, int type,
                             OnDataReturnListener... dataReturnListeners) {
        AsyncTaskUtils ansyTaskUtils = new AsyncTaskUtils(taskId, type, saveCache, reflsh);
        if (dataReturnListeners != null) {
            for (OnDataReturnListener onDataReturnListener : dataReturnListeners) {
                ansyTaskUtils.addDataReturnListener(onDataReturnListener);
            }
        }
        ansyTaskUtils.execute(url, params);
    }

    protected void uploadImages(String taskId, String url, Map<String, Object> params, Image[] images,
                                OnDataReturnListener... dataReturnListeners) {
        AsyncTaskUtils ansyTaskUtils = new AsyncTaskUtils(taskId, AsyncTaskUtils.HTTP_POST_UPDATEIMAGE, false, false);
        for (OnDataReturnListener onDataReturnListener : dataReturnListeners) {
            ansyTaskUtils.addDataReturnListener(onDataReturnListener);
        }
        ansyTaskUtils.execute(url, params, images);
    }
}
