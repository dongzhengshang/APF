package com.dzs.projectframe.adapter.abslistview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.dzs.projectframe.adapter.ViewHolder;

import java.util.List;

/**
 * @author DZS dzsk@outlook.com
 * @version V1.0
 * @date 2016/8/19.
 */
public abstract class BaseMultiItemAdapter<T> extends BaseUniversalAdapter<T> {
    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

    public BaseMultiItemAdapter(Context context, MultiItemTypeSupport<T> multiItemTypeSupport) {
        super(context, -1);
        if (mMultiItemTypeSupport == null) throw new IllegalArgumentException("the mMultiItemTypeSupport can not be null.");
        this.mMultiItemTypeSupport = multiItemTypeSupport;
    }

    public BaseMultiItemAdapter(Context context, List<T> data, MultiItemTypeSupport<T> multiItemTypeSupport) {
        super(context, -1, data);
        if (mMultiItemTypeSupport == null) throw new IllegalArgumentException("the mMultiItemTypeSupport can not be null.");
        this.mMultiItemTypeSupport = multiItemTypeSupport;
    }

    @Override
    public int getViewTypeCount() {
        return mMultiItemTypeSupport.getViewTypeCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= data.size()) return 0;
        return mMultiItemTypeSupport.getItemViewType(position, data.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) != 0) {
            int layoutId = mMultiItemTypeSupport.getLayoutId(position, getItem(position));
            ViewHolder helper = ViewHolder.get(context, convertView, parent, layoutId, position);
            T item = getItem(position);
            convert(helper, item);
            helper.setAssociatedObject(item);
            return helper.getView();
        }
        if (changeLoadMoreView != null) return changeLoadMoreView.getView(convertView, parent);
        else return createIndeterminateProgressView(convertView, parent);
    }
}
