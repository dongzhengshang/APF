package com.dzs.projectframe.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dzs.projectframe.base.bean.LibEntity;

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
        if (intent.getAction().equals(ACTION)) {
            LibEntity libEntity = (LibEntity) intent.getSerializableExtra(LibEntity.class.getName());
            for (OnBroadcastReceiverListener listener : list) listener.onDateReceiver(libEntity);
        }
    }

    public interface OnBroadcastReceiverListener {
        void onDateReceiver(LibEntity libEntity);
    }

    public void addReceiver(OnBroadcastReceiverListener listener) {
        if (list != null) list.add(listener);
    }

    public void removeReceiver(OnBroadcastReceiverListener listener) {
        if (list != null && list.contains(listener)) list.remove(listener);
    }

    public void clear() {
        list.clear();
        list = null;
    }
}
