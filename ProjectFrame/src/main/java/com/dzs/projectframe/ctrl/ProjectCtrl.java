package com.dzs.projectframe.ctrl;

import android.os.AsyncTask;

import com.dzs.projectframe.bean.NetEntity;
import com.dzs.projectframe.utils.AsyncTaskUtils;

/**
 * 控制器
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/9/12.
 */
public class ProjectCtrl {
	/**
	 * 网络访问获取数据
	 */
	private void getData(NetEntity netEntity, AsyncTaskUtils.OnNetReturnListener... netReturnListeners) {
		new AsyncTaskUtils(netReturnListeners).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
}
