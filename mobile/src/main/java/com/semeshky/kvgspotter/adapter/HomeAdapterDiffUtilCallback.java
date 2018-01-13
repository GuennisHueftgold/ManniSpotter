package com.semeshky.kvgspotter.adapter;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;


final class HomeAdapterDiffUtilCallback extends DiffUtil.Callback {

    private final List<HomeAdapter.ListItem> mNewList;
    private final List<HomeAdapter.ListItem> mOldList;

    HomeAdapterDiffUtilCallback(List<HomeAdapter.ListItem> oldList, List<HomeAdapter.ListItem> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return this.mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return this.mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return this.mOldList.get(oldItemPosition).id ==
                this.mNewList.get(newItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Object oldItem = this.mOldList.get(oldItemPosition);
        final Object newItem = this.mNewList.get(newItemPosition);
        if (oldItem == null || newItem == null)
            return false;
        return oldItem.equals(newItem);
    }

    @Nullable
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return null;
    }
}