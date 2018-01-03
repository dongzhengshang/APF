package com.dzs.projectframe.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dzs.projectframe.adapter.ViewHolder;
import com.dzs.projectframe.base.Bean.LibEntity;
import com.dzs.projectframe.broadcast.Receiver;

/**
 * Fragment 基类
 *
 * @author DZS dzsdevelop@163.com
 * @version 1.0
 * @date 2015-6-17 下午3:47:45
 */
public abstract class ProjectFragment extends Fragment implements Receiver.OnBroadcastReceiverListener {
    protected abstract int setLayoutById();

    protected View setLayoutByView() {
        return null;
    }

    protected abstract void initView();

    protected abstract void initData();

    protected void layoutVisiable() {
    }

    protected void layoutInVisiable() {
    }

    protected boolean isVisible = false;
    protected boolean isPrepared = false;
    protected ViewHolder viewUtils;
    protected View view;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisibleToUser) layoutVisiable();
        else layoutInVisiable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            int layoutId = setLayoutById();
            View layout = setLayoutByView();
            if (layoutId <= 0 && layout == null) throw new NullPointerException("layout can not be null.");
            viewUtils = layoutId > 0 ? ViewHolder.get(getContext(), layoutId) : ViewHolder.get(getContext(), layout);
            view = viewUtils.getView();
        }
        // 缓存的View需要判断是否已经被加载过parent，如果有需要移除parent。否则会出现此View已经有parent的错误
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        ProjectContext.appContext.addReceiver(this);
        initView();
        isPrepared = true;
        layoutVisiable();
        initData();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ProjectContext.appContext.removeReceiver(this);
    }

    @Override
    public void onDateReceiver(LibEntity libEntity) {

    }
}
