package com.dzs.projectframe.adapter.recyclerview;

public interface MultiItemTypeSupport<T> {

    int getLayoutId(int viewType);

    int getViewTypeCount();

    int getItemViewType(int position, T t);

}