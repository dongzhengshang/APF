package com.dzs.projectframe.utils;

import android.os.AsyncTask;


import com.dzs.projectframe.bean.NetEntity;

import java.util.ArrayList;
import java.util.Collections;


public class AsyncTaskUtils extends AsyncTask<NetEntity, Integer, NetEntity> {
	
	private String taskId;
	private ArrayList<OnNetReturnListener> dataReturnListeners;
	
	public AsyncTaskUtils(OnNetReturnListener... dataReturnListener) {
		dataReturnListeners = new ArrayList<>();
		if (dataReturnListener != null) Collections.addAll(dataReturnListeners, dataReturnListener);
	}
	
	public void addDataReturnListener(OnNetReturnListener... dataReturnListener) {
		Collections.addAll(dataReturnListeners, dataReturnListener);
	}
	
	public void removeDataReturnListener(OnNetReturnListener dataReturnListener) {
		if (dataReturnListener != null && dataReturnListeners != null)
			dataReturnListeners.remove(dataReturnListener);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected NetEntity doInBackground(NetEntity... params) {
		taskId = params[0].getTaskId();
		return HttpUtils.getData(params[0]);
	}
	
	@Override
	protected void onPostExecute(NetEntity result) {
		super.onPostExecute(result);
		for (OnNetReturnListener on : dataReturnListeners) {
			result.setTaskId(taskId);
			on.onDateReturn(result);
		}
	}
	
	public interface OnNetReturnListener {
		void onDateReturn(NetEntity netEntity);
	}
}
