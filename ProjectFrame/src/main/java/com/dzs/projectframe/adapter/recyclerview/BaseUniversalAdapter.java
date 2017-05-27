package com.dzs.projectframe.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dzs.projectframe.adapter.ViewHolder;
import com.dzs.projectframe.adapter.abslistview.MultiItemTypeSupport;

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
    protected MultiItemTypeSupport multiItemTypeSupport;

    public BaseUniversalAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public BaseUniversalAdapter(Context context, int layoutResId, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : new ArrayList<>(data);
        this.context = context;
        this.layoutResId = layoutResId;
    }

    public BaseUniversalAdapter(Context context, List<T> data, MultiItemTypeSupport multiItemTypeSupport) {
        this.data = data == null ? new ArrayList<T>() : new ArrayList<>(data);
        this.context = context;
        if (multiItemTypeSupport == null)
            throw new IllegalArgumentException("the multiItemTypeSupport can not be null.");
        this.multiItemTypeSupport = multiItemTypeSupport;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (multiItemTypeSupport != null) {
            layoutResId = multiItemTypeSupport.getLayoutId(viewType, data.get(viewType));
            viewHolder = ViewHolder.get(context, null, parent, layoutResId);
        } else {
            viewHolder = ViewHolder.get(context, null, parent, layoutResId);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.updatePosition(position);
        convert(holder, data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (multiItemTypeSupport != null)
            return multiItemTypeSupport.getItemViewType(position, data.get(position));
        return super.getItemViewType(position);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
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

    public boolean contains(T elem) {
        return data.contains(elem);
    }

    public T getItem(int index) {
        return data.get(index);
    }

    /**
     * Clear data list
     */
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }
    protected abstract void convert(ViewHolder holder, T t);
}
