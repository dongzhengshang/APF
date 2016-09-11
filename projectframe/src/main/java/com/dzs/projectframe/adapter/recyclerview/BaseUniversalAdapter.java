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
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/8/19.
 */
public abstract class BaseUniversalAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context context;
    protected List<T> data;
    protected int layoutResId;
    protected ViewHolder viewHolder;
    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;
    protected MultiItemTypeSupport multiItemTypeSupport;

    public BaseUniversalAdapter(Context context, int layoutResId, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : new ArrayList<>(data);
        this.context = context;
        this.layoutResId = layoutResId;
    }

    public BaseUniversalAdapter(Context context, List<T> data, MultiItemTypeSupport multiItemTypeSupport) {
        this.data = data == null ? new ArrayList<T>() : new ArrayList<>(data);
        this.context = context;
        if (multiItemTypeSupport == null) throw new IllegalArgumentException("the multiItemTypeSupport can not be null.");
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
        initListener(parent);
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
        if (multiItemTypeSupport != null) return multiItemTypeSupport.getItemViewType(position, data.get(position));
        return super.getItemViewType(position);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    /**
     * 设置监听
     */
    private void initListener(final ViewGroup parent) {
        viewHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(parent, v, data.get(viewHolder.getCurrentPosition()), viewHolder.getCurrentPosition());
            }
        });
        viewHolder.getView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null)
                    onItemLongClickListener.onItemLongClick(parent, v, data.get(viewHolder.getCurrentPosition()), viewHolder.getCurrentPosition());
                return false;
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(ViewGroup parent, View view, T t, int position);
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(ViewGroup parent, View view, T t, int position);
    }

    public abstract void convert(ViewHolder holder, T t);
}
