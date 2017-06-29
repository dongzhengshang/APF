package com.dzs.projectframe.adapter.abslistview;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.dzs.projectframe.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用适配器基类
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/7/9.
 */
public abstract class BaseUniversalAdapter<T> extends BaseAdapter {
    protected final Context context;
    protected final int layoutResId;
    protected List<T> data;
    protected boolean displayIndeterminateProgress = false;
    protected onChangeLoadMoreView changeLoadMoreView;

    protected abstract void convert(ViewHolder helper, T item);

    public BaseUniversalAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public BaseUniversalAdapter(Context context, int layoutResId, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : new ArrayList<>(data);
        this.context = context;
        this.layoutResId = layoutResId;
    }

    @Override
    public int getCount() {
        int extra = displayIndeterminateProgress ? 1 : 0;
        return data.size() + extra;
    }

    @Override
    public T getItem(int position) {
        if (position >= data.size()) return null;
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position >= data.size() ? 0 : 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) != 0) {
            final ViewHolder helper = ViewHolder.get(context, convertView, parent, layoutResId, position);
            T item = getItem(position);
            convert(helper, item);
            helper.setAssociatedObject(item);
            return helper.getView();
        }
        if (changeLoadMoreView != null) {
            return changeLoadMoreView.getView(convertView, parent);
        } else {
            return createIndeterminateProgressView(convertView, parent);
        }
    }


    protected View createIndeterminateProgressView(View convertView, ViewGroup parent) {
        if (convertView == null) {
            FrameLayout container = new FrameLayout(context);
            container.setForegroundGravity(Gravity.CENTER);
            ProgressBar progress = new ProgressBar(context);
            container.addView(progress);
            convertView = container;
        }
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return position < data.size();
    }

    public void add(T elem) {
        data.add(elem);
        notifyDataSetChanged();
    }

    public void addAll(List<T> elem) {
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public void set(T oldElem, T newElem) {
        set(data.indexOf(oldElem), newElem);
    }

    public void set(int index, T elem) {
        data.set(index, elem);
        notifyDataSetChanged();
    }

    public void remove(T elem) {
        data.remove(elem);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> elem) {
        data.clear();
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public ArrayList<T> getAllData() {
        return (ArrayList<T>) data;
    }

    public boolean contains(T elem) {
        return data.contains(elem);
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void showIndeterminateProgress(boolean display) {
        if (display == displayIndeterminateProgress) return;
        displayIndeterminateProgress = display;
        notifyDataSetChanged();
    }

    public void setChangeLoadMoreView(onChangeLoadMoreView changeLoadMoreView) {
        this.changeLoadMoreView = changeLoadMoreView;
    }

    public interface onChangeLoadMoreView {
        View getView(View convertView, ViewGroup parent);
    }
}
