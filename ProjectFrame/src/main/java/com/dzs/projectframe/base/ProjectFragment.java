package com.dzs.projectframe.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.dzs.projectframe.adapter.ViewHolder;
import com.dzs.projectframe.bean.NetEntity;
import com.dzs.projectframe.broadcast.Receiver;

/**
 * Fragment 基类
 *
 * @author DZS dzsdevelop@163.com
 * @version 1.0
 * @date 2015-6-17 下午3:47:45
 */
public abstract class ProjectFragment extends Fragment implements Receiver.OnBroadcastReceiverListener {
    protected int setLayoutById() {
        return 0;
    }

    protected abstract ViewBinding setContentByViewBinding();

    protected View setLayoutByView() {
        return null;
    }

    protected abstract void initView();

    protected abstract void initData();

    protected void layoutVisible() {
    }

    protected void layoutInVisible() {
    }

    protected boolean isVisible = false;
    protected boolean isPrepared = false;
    protected ViewHolder viewUtils;
    protected View view;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisibleToUser) layoutVisible();
        else layoutInVisible();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            int layoutId = setLayoutById();
            View layout = setLayoutByView();
            ViewBinding viewBinding=setContentByViewBinding();
            if (layoutId <= 0 && layout == null&&viewBinding==null)
                throw new NullPointerException("layout can not be null.");

            viewUtils = viewBinding!=null?ViewHolder.get(getContext(), viewBinding.getRoot()):layoutId > 0 ? ViewHolder.get(getContext(), layoutId) : ViewHolder.get(getContext(), view);
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
        layoutVisible();
        initData();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ProjectContext.appContext.removeReceiver(this);
    }

    @Override
    public void onDateReceiver(NetEntity netEntity) {

    }
}
