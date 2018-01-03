package com.dzs.projectframe.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.dzs.projectframe.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView 适配器
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/8/19.
 */
public abstract class BaseUniversalAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context context;
    protected List<T> data;
    protected int layoutResId;
    protected ViewHolder viewHolder;
    protected View loadMoreView;
    protected boolean showLoadMore = false;
    protected MultiItemTypeSupport<T> multiItemTypeSupport;

    protected abstract void convert(ViewHolder holder, T t);

    public BaseUniversalAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public BaseUniversalAdapter(Context context, int layoutResId, List<T> data) {
        this.data = (data == null ? new ArrayList<T>() : new ArrayList<>(data));
        this.context = context;
        this.layoutResId = layoutResId;
    }

    public BaseUniversalAdapter(Context context, List<T> data, MultiItemTypeSupport<T> multiItemTypeSupport) {
        this.data = (data == null ? new ArrayList<T>() : new ArrayList<>(data));
        this.context = context;
        if (multiItemTypeSupport == null) {
            throw new IllegalArgumentException("the multiItemTypeSupport can not be null.");
        }
        this.multiItemTypeSupport = multiItemTypeSupport;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != 0) {
            if (multiItemTypeSupport != null) {
                layoutResId = multiItemTypeSupport.getLayoutId(viewType);
                viewHolder = ViewHolder.get(context, null, parent, layoutResId);
            } else {
                viewHolder = ViewHolder.get(context, null, parent, layoutResId);
            }
        } else {
            viewHolder = (loadMoreView != null ? ViewHolder.get(context, loadMoreView) : createLoadMoreViewHolder());
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.updatePosition(position);
        if (getItemViewType(position) != 0) {
            convert(holder, data.get(position));
        }
    }

    @Override
    public int getItemCount() {
        int extra = showLoadMore ? 1 : 0;
        return data.size() + extra;
    }

    @Override
    public int getItemViewType(int position) {
        return position >= data.size() ? 0 : (multiItemTypeSupport != null ? multiItemTypeSupport.getItemViewType(position, data.get(position)) : 1);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public boolean isEnabled(int position) {
        return position < data.size();
    }

    public void showLoadMoreView(boolean display) {
        if (display == showLoadMore) {
            return;
        }
        showLoadMore = display;
        notifyDataSetChanged();
    }

    /*设置加载更多布局*/
    public void setLoadMoreView(View view) {
        this.loadMoreView = view;
    }

    /*创建加载更多*/
    private ViewHolder createLoadMoreViewHolder() {
        FrameLayout container = new FrameLayout(context);
        container.setForegroundGravity(Gravity.CENTER);
        ProgressBar progress = new ProgressBar(context);
        container.addView(progress);
        return ViewHolder.get(context, container);
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

    public T getItem(int index) {
        return data.get(index);
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }
}
