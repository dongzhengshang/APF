package com.dzs.projectframe.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dzs.projectframe.bean.NetEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 广播接收者
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2017/3/23
 */
public class Receiver extends BroadcastReceiver {
	public final static String ACTION = "ProjectFrame.BROADCAST";
	private List<OnBroadcastReceiverListener> list;
	
	public Receiver() {
		list = new ArrayList<>();
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (ACTION.equals(intent.getAction())) {
			NetEntity netEntity = (NetEntity) intent.getSerializableExtra(NetEntity.class.getName());
			for (OnBroadcastReceiverListener listener : list) listener.onDateReceiver(netEntity);
		}
	}
	
	public interface OnBroadcastReceiverListener {
		void onDateReceiver(NetEntity netEntity);
	}
	
	public void addReceiver(OnBroadcastReceiverListener listener) {
		if (list != null) list.add(listener);
	}
	
	public void removeReceiver(OnBroadcastReceiverListener listener) {
		if (list != null) list.remove(listener);
	}
	
	public void clear() {
		list.clear();
		list = null;
	}
}
