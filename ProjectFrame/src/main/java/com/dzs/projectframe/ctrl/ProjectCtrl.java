package com.dzs.projectframe.ctrl;

import android.os.AsyncTask;

import com.dzs.projectframe.bean.NetEntity;
import com.dzs.projectframe.utils.AsyncTaskUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 控制器
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/9/12.
 */
public class ProjectCtrl {
	private static Executor THREAD_POOL_EXECUTOR;
	private static final int CORE_POOL_SIZE = 10;
	private static final int MAXIMUM_POOL_SIZE = 40;
	private static final int KEEP_ALIVE_SECONDS = 30;
	private static ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);
		
		public Thread newThread(Runnable r) {
			return new Thread(r, "ProjectCtrl #" + mCount.getAndIncrement());
		}
	};
	
	static {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
				CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(256), sThreadFactory);
		threadPoolExecutor.allowCoreThreadTimeOut(true);
		THREAD_POOL_EXECUTOR = threadPoolExecutor;
	}
	
	/**
	 * 网络访问获取数据
	 */
	private void getData(NetEntity netEntity, AsyncTaskUtils.OnNetReturnListener... netReturnListeners) {
		new AsyncTaskUtils(netReturnListeners).executeOnExecutor(THREAD_POOL_EXECUTOR);
	}
	
	/**
	 * 网络访问获取数据
	 */
	private void getData(Executor executor, NetEntity netEntity, AsyncTaskUtils.OnNetReturnListener... netReturnListeners) {
		new AsyncTaskUtils(netReturnListeners).executeOnExecutor(executor);
	}
}
