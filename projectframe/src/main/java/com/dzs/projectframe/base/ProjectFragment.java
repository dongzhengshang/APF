package com.dzs.projectframe.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dzs.projectframe.adapter.ViewHolder;

/**
 * Fragment 基类
 *
 * @author DZS dzsdevelop@163.com
 * @version 1.0
 * @date 2015-6-17 下午3:47:45
 */
public abstract class ProjectFragment extends Fragment {
    protected abstract int setFragmentLayout();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void loadDataVisiable();

    protected abstract void loadDataInVisiable();

    protected boolean isVisible = false;
    protected boolean isPrepared = false;
    protected ViewHolder viewUtils;
    protected View view;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisibleToUser) loadDataVisiable();
        else loadDataInVisiable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            viewUtils = ViewHolder.get(getContext(), setFragmentLayout(), container);
            view = viewUtils.getView();
        }
        // 缓存的View需要判断是否已经被加载过parent，如果有需要移除parent。否则会出现此View已经有parent的错误
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        initView();
        isPrepared = true;
        loadDataVisiable();
        initData();
        return view;
    }

}
